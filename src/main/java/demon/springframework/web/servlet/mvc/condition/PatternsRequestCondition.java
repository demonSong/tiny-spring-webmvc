package demon.springframework.web.servlet.mvc.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import demon.springframework.core.util.AntPathMatcher;
import demon.springframework.core.util.PathMatcher;
import demon.springframework.web.util.UrlPathHelper;

public final class PatternsRequestCondition extends AbstractRequestCondition<PatternsRequestCondition>{

	private final Set<String> patterns;
	
	private final UrlPathHelper pathHelper;

	private final PathMatcher pathMatcher;
	
	private final boolean useSuffixPatternMatch;

	private final boolean useTrailingSlashMatch;
	
	private final List<String> fileExtensions = new ArrayList<String>();
	
	public PatternsRequestCondition(String... patterns) {
		this(asList(patterns), null, null, true, true, null);
	}

	public PatternsRequestCondition(String[] patterns, UrlPathHelper urlPathHelper, PathMatcher pathMatcher,
			boolean useSuffixPatternMatch, boolean useTrailingSlashMatch) {

		this(asList(patterns), urlPathHelper, pathMatcher, useSuffixPatternMatch, useTrailingSlashMatch, null);
	}

	public PatternsRequestCondition(String[] patterns, UrlPathHelper urlPathHelper,
			PathMatcher pathMatcher, boolean useSuffixPatternMatch, boolean useTrailingSlashMatch,
			List<String> fileExtensions) {

		this(asList(patterns), urlPathHelper, pathMatcher, useSuffixPatternMatch, useTrailingSlashMatch, fileExtensions);
	}
	private PatternsRequestCondition(Collection<String> patterns, UrlPathHelper urlPathHelper,
			PathMatcher pathMatcher, boolean useSuffixPatternMatch, boolean useTrailingSlashMatch,
			List<String> fileExtensions) {

		this.patterns = Collections.unmodifiableSet(prependLeadingSlash(patterns));
		this.pathHelper = urlPathHelper != null ? urlPathHelper : new UrlPathHelper();
		this.pathMatcher = pathMatcher != null ? pathMatcher : new AntPathMatcher();
		this.useSuffixPatternMatch = useSuffixPatternMatch;
		this.useTrailingSlashMatch = useTrailingSlashMatch;
		if (fileExtensions != null) {
			for (String fileExtension : fileExtensions) {
				if (fileExtension.charAt(0) != '.') {
					fileExtension = "." + fileExtension;
				}
				this.fileExtensions.add(fileExtension);
			}
		}
	}
	
	private static List<String> asList(String... patterns) {
		return (patterns != null ? Arrays.asList(patterns) : Collections.<String>emptyList());
	}
	
	private static Set<String> prependLeadingSlash(Collection<String> patterns) {
		if (patterns == null) {
			return Collections.emptySet();
		}
		Set<String> result = new LinkedHashSet<String>(patterns.size());
		for (String pattern : patterns) {
			if (StringUtils.hasLength(pattern) && !pattern.startsWith("/")) {
				pattern = "/" + pattern;
			}
			result.add(pattern);
		}
		return result;
	}
	
	@Override
	public PatternsRequestCondition combine(PatternsRequestCondition other) {
		Set<String> result = new LinkedHashSet<String>();
		if (!this.patterns.isEmpty() && !other.patterns.isEmpty()) {
			for (String pattern1 : this.patterns) {
				for (String pattern2 : other.patterns) {
					result.add(this.pathMatcher.combine(pattern1, pattern2));
				}
			}
		}
		else if (!this.patterns.isEmpty()) {
			result.addAll(this.patterns);
		}
		else if (!other.patterns.isEmpty()) {
			result.addAll(other.patterns);
		}
		else {
			result.add("");
		}
		return new PatternsRequestCondition(result, this.pathHelper, this.pathMatcher, this.useSuffixPatternMatch,
				this.useTrailingSlashMatch, this.fileExtensions);
	}

	public Set<String> getPatterns() {
		return this.patterns;
	}

	@Override
	protected Collection<String> getContent() {
		return this.patterns;
	}

	@Override
	protected String getToStringInfix() {
		return " || ";
	}

	@Override
	public PatternsRequestCondition getMatchingCondition(HttpServletRequest request) {
		if(this.patterns.isEmpty()){
			return this;
		}
		String lookupPath = this.pathHelper.getLookupPathForRequest(request);
		List<String> matches = getMatchingPatterns(lookupPath);
		return matches.isEmpty() ? null :
			new PatternsRequestCondition(matches, this.pathHelper, this.pathMatcher, this.useSuffixPatternMatch,
					this.useTrailingSlashMatch, this.fileExtensions);
	}
	
	public List<String> getMatchingPatterns(String lookupPath) {
		List<String> matches = new ArrayList<String>();
		for (String pattern : this.patterns) {
			String match = getMatchingPattern(pattern, lookupPath);
			if (match != null) {
				matches.add(match);
			}
		}
		Collections.sort(matches, this.pathMatcher.getPatternComparator(lookupPath));
		return matches;
	}
	
	private String getMatchingPattern(String pattern, String lookupPath) {
		if (pattern.equals(lookupPath)) {
			return pattern;
		}
		if (this.useSuffixPatternMatch) {
			if (!this.fileExtensions.isEmpty() && lookupPath.indexOf('.') != -1) {
				for (String extension : this.fileExtensions) {
					if (this.pathMatcher.match(pattern + extension, lookupPath)) {
						return pattern + extension;
					}
				}
			}
			else {
				boolean hasSuffix = pattern.indexOf('.') != -1;
				if (!hasSuffix && this.pathMatcher.match(pattern + ".*", lookupPath)) {
					return pattern + ".*";
				}
			}
		}
		if (this.pathMatcher.match(pattern, lookupPath)) {
			return pattern;
		}
		if (this.useTrailingSlashMatch) {
			if (!pattern.endsWith("/") && this.pathMatcher.match(pattern + "/", lookupPath)) {
				return pattern +"/";
			}
		}
		return null;
	}
}
