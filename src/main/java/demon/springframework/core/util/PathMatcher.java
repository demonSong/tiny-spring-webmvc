package demon.springframework.core.util;

import java.util.Comparator;

public interface PathMatcher {

	String combine(String pattern1, String pattern2);
	
	boolean isPattern(String path);
	
	boolean match(String pattern, String path);
	
	boolean matchStart(String pattern, String path);
	
	Comparator<String> getPatternComparator(String path);

}
