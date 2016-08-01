package demon.springframework.core.type.filter;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;

import demon.springframework.core.type.AnnotationMetadata;
import demon.springframework.core.type.classreading.MetadataReader;

public class AnnotationTypeFilter extends AbstractTypeHierarchyTraversingFilter {
	
	private final Class<? extends Annotation> annotationType;
	
	private final boolean considerMetaAnnotations;

	public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
		this(annotationType, true);
	}
	
	public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations) {
		super(annotationType.isAnnotationPresent(Inherited.class), false);
		this.annotationType = annotationType;
		this.considerMetaAnnotations = considerMetaAnnotations;
	}

	@Override
	protected boolean matchSelf(MetadataReader metadataReader) {
		AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
		return metadata.hasAnnotation(this.annotationType.getName()) || 
				(this.considerMetaAnnotations && metadata.hasMetaAnnotation(this.annotationType.getName()));
	}

	@Override
	protected Boolean matchSuperClass(String superClassName) {
		if (Object.class.getName().equals(superClassName)) {
			return Boolean.FALSE;
		}
		else if (superClassName.startsWith("java.")) {
			try {
				Class<?> clazz = getClass().getClassLoader().loadClass(superClassName);
				return (clazz.getAnnotation(this.annotationType) != null);
			}
			catch (ClassNotFoundException ex) {
				// Class not found - can't determine a match that way.
			}
		}
		return null;
	}
	

}
