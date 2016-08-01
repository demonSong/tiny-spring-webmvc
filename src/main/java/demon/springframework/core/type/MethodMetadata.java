package demon.springframework.core.type;

import java.util.Map;

public interface MethodMetadata {
	
	String getMethodName();

	public String getDeclaringClassName();

	boolean isStatic();

	boolean isFinal();

	boolean isOverridable();

	boolean isAnnotated(String annotationType);

	Map<String, Object> getAnnotationAttributes(String annotationType);
}
