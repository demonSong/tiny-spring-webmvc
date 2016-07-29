package demon.springframework.core.type;

import java.util.Map;
import java.util.Set;

import org.springframework.core.type.MethodMetadata;

import demon.springframework.core.type.ClassMetadata;

public interface AnnotationMetadata extends ClassMetadata{
	
	Set<String> getAnnotationTypes();
	
	Set<String> getMetaAnnotationTypes(String annotationType);
	
	boolean hasAnnotation(String annotationType);
	
	boolean hasMetaAnnotation(String metaAnnotationType);
	
	boolean isAnnotated(String annotationType);
	
	Map<String, Object> getAnnotationAttributes(String annotationType);

	Map<String, Object> getAnnotationAttributes(String annotationType, boolean classValuesAsString);

	boolean hasAnnotatedMethods(String annotationType);

	Set<MethodMetadata> getAnnotatedMethods(String annotationType);
}
