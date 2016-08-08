package demon.springframework.core.util;

import java.util.Comparator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class AntPathMatcher implements PathMatcher{

	
	private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{[^/]+?\\}");

	/** Default path separator: "/" */
	public static final String DEFAULT_PATH_SEPARATOR = "/";

	private String pathSeparator = DEFAULT_PATH_SEPARATOR;
	
	public boolean isPattern(String path) {
		return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
	}
	
	@Override
	public boolean matchStart(String pattern, String path) {
		return doMatch(pattern, path, false, null);
	}
	
	public String combine(String pattern1, String pattern2) {
		if (!StringUtils.hasText(pattern1) && !StringUtils.hasText(pattern2)) {
			return "";
		}
		else if (!StringUtils.hasText(pattern1)) {
			return pattern2;
		}
		else if (!StringUtils.hasText(pattern2)) {
			return pattern1;
		}
		else if (match(pattern1, pattern2)) {
			return pattern2;
		}
		else if (pattern1.endsWith("/*")) {
			if (pattern2.startsWith("/")) {
				// /hotels/* + /booking -> /hotels/booking
				return pattern1.substring(0, pattern1.length() - 1) + pattern2.substring(1);
			}
			else {
				// /hotels/* + booking -> /hotels/booking
				return pattern1.substring(0, pattern1.length() - 1) + pattern2;
			}
		}
		else if (pattern1.endsWith("/**")) {
			if (pattern2.startsWith("/")) {
				// /hotels/** + /booking -> /hotels/**/booking
				return pattern1 + pattern2;
			}
			else {
				// /hotels/** + booking -> /hotels/**/booking
				return pattern1 + "/" + pattern2;
			}
		}
		else {
			int dotPos1 = pattern1.indexOf('.');
			if (dotPos1 == -1) {
				// simply concatenate the two patterns
				if (pattern1.endsWith("/") || pattern2.startsWith("/")) {
					return pattern1 + pattern2;
				}
				else {
					return pattern1 + "/" + pattern2;
				}
			}
			String fileName1 = pattern1.substring(0, dotPos1);
			String extension1 = pattern1.substring(dotPos1);
			String fileName2;
			String extension2;
			int dotPos2 = pattern2.indexOf('.');
			if (dotPos2 != -1) {
				fileName2 = pattern2.substring(0, dotPos2);
				extension2 = pattern2.substring(dotPos2);
			}
			else {
				fileName2 = pattern2;
				extension2 = "";
			}
			String fileName = fileName1.endsWith("*") ? fileName2 : fileName1;
			String extension = extension1.startsWith("*") ? extension2 : extension1;

			return fileName + extension;
		}
	}
	
	public boolean match(String pattern, String path) {
		return doMatch(pattern, path, true, null);
	}
	
	protected boolean doMatch(String pattern, String path, boolean fullMatch,
			Map<String, String> uriTemplateVariables) {

		if (path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
			return false;
		}

		String[] pattDirs = StringUtils.tokenizeToStringArray(pattern, this.pathSeparator);
		String[] pathDirs = StringUtils.tokenizeToStringArray(path, this.pathSeparator);

		int pattIdxStart = 0;
		int pattIdxEnd = pattDirs.length - 1;
		int pathIdxStart = 0;
		int pathIdxEnd = pathDirs.length - 1;

		// Match all elements up to the first **
		while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
			String patDir = pattDirs[pattIdxStart];
			if ("**".equals(patDir)) {
				break;
			}
			if (!matchStrings(patDir, pathDirs[pathIdxStart], uriTemplateVariables)) {
				return false;
			}
			pattIdxStart++;
			pathIdxStart++;
		}

		if (pathIdxStart > pathIdxEnd) {
			// Path is exhausted, only match if rest of pattern is * or **'s
			if (pattIdxStart > pattIdxEnd) {
				return (pattern.endsWith(this.pathSeparator) ? path.endsWith(this.pathSeparator) :
						!path.endsWith(this.pathSeparator));
			}
			if (!fullMatch) {
				return true;
			}
			if (pattIdxStart == pattIdxEnd && pattDirs[pattIdxStart].equals("*") && path.endsWith(this.pathSeparator)) {
				return true;
			}
			for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
				if (!pattDirs[i].equals("**")) {
					return false;
				}
			}
			return true;
		}
		else if (pattIdxStart > pattIdxEnd) {
			// String not exhausted, but pattern is. Failure.
			return false;
		}
		else if (!fullMatch && "**".equals(pattDirs[pattIdxStart])) {
			// Path start definitely matches due to "**" part in pattern.
			return true;
		}

		// up to last '**'
		while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
			String patDir = pattDirs[pattIdxEnd];
			if (patDir.equals("**")) {
				break;
			}
			if (!matchStrings(patDir, pathDirs[pathIdxEnd], uriTemplateVariables)) {
				return false;
			}
			pattIdxEnd--;
			pathIdxEnd--;
		}
		if (pathIdxStart > pathIdxEnd) {
			// String is exhausted
			for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
				if (!pattDirs[i].equals("**")) {
					return false;
				}
			}
			return true;
		}

		while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
			int patIdxTmp = -1;
			for (int i = pattIdxStart + 1; i <= pattIdxEnd; i++) {
				if (pattDirs[i].equals("**")) {
					patIdxTmp = i;
					break;
				}
			}
			if (patIdxTmp == pattIdxStart + 1) {
				// '**/**' situation, so skip one
				pattIdxStart++;
				continue;
			}
			// Find the pattern between padIdxStart & padIdxTmp in str between
			// strIdxStart & strIdxEnd
			int patLength = (patIdxTmp - pattIdxStart - 1);
			int strLength = (pathIdxEnd - pathIdxStart + 1);
			int foundIdx = -1;

			strLoop:
			for (int i = 0; i <= strLength - patLength; i++) {
				for (int j = 0; j < patLength; j++) {
					String subPat = pattDirs[pattIdxStart + j + 1];
					String subStr = pathDirs[pathIdxStart + i + j];
					if (!matchStrings(subPat, subStr, uriTemplateVariables)) {
						continue strLoop;
					}
				}
				foundIdx = pathIdxStart + i;
				break;
			}

			if (foundIdx == -1) {
				return false;
			}

			pattIdxStart = patIdxTmp;
			pathIdxStart = foundIdx + patLength;
		}

		for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
			if (!pattDirs[i].equals("**")) {
				return false;
			}
		}

		return true;
	}
	
	private boolean matchStrings(String pattern, String str, Map<String, String> uriTemplateVariables) {
		AntPathStringMatcher matcher = new AntPathStringMatcher(pattern, str, uriTemplateVariables);
		return matcher.matchStrings();
	}
	
	public Comparator<String> getPatternComparator(String path) {
		return new AntPatternComparator(path);
	}


	private static class AntPatternComparator implements Comparator<String> {

		private final String path;

		private AntPatternComparator(String path) {
			this.path = path;
		}

		public int compare(String pattern1, String pattern2) {
			if (pattern1 == null && pattern2 == null) {
				return 0;
			}
			else if (pattern1 == null) {
				return 1;
			}
			else if (pattern2 == null) {
				return -1;
			}
			boolean pattern1EqualsPath = pattern1.equals(path);
			boolean pattern2EqualsPath = pattern2.equals(path);
			if (pattern1EqualsPath && pattern2EqualsPath) {
				return 0;
			}
			else if (pattern1EqualsPath) {
				return -1;
			}
			else if (pattern2EqualsPath) {
				return 1;
			}
			int wildCardCount1 = getWildCardCount(pattern1);
			int wildCardCount2 = getWildCardCount(pattern2);

			int bracketCount1 = StringUtils.countOccurrencesOf(pattern1, "{");
			int bracketCount2 = StringUtils.countOccurrencesOf(pattern2, "{");

			int totalCount1 = wildCardCount1 + bracketCount1;
			int totalCount2 = wildCardCount2 + bracketCount2;

			if (totalCount1 != totalCount2) {
				return totalCount1 - totalCount2;
			}

			int pattern1Length = getPatternLength(pattern1);
			int pattern2Length = getPatternLength(pattern2);

			if (pattern1Length != pattern2Length) {
				return pattern2Length - pattern1Length;
			}

			if (wildCardCount1 < wildCardCount2) {
				return -1;
			}
			else if (wildCardCount2 < wildCardCount1) {
				return 1;
			}

			if (bracketCount1 < bracketCount2) {
				return -1;
			}
			else if (bracketCount2 < bracketCount1) {
				return 1;
			}

			return 0;
		}

		private int getWildCardCount(String pattern) {
			if (pattern.endsWith(".*")) {
				pattern = pattern.substring(0, pattern.length() - 2);
			}
			return StringUtils.countOccurrencesOf(pattern, "*");
		}

		/**
		 * Returns the length of the given pattern, where template variables are considered to be 1 long.
		 */
		private int getPatternLength(String pattern) {
			Matcher m = VARIABLE_PATTERN.matcher(pattern);
			return m.replaceAll("#").length();
		}
	}

}
