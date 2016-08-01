package demon.springframework.core.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.core.annotation.AnnotationUtils;

public class StandardAnnotationMetadata extends StandardClassMetadata implements AnnotationMetadata {

	public StandardAnnotationMetadata(Class introspectedClass) {
		super(introspectedClass);
	}

	@Override
	public Set<String> getAnnotationTypes() {
		Set<String> types = new LinkedHashSet<String>();
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			types.add(ann.annotationType().getName());
		}
		return types;
	}

	@Override
	public Set<String> getMetaAnnotationTypes(String annotationType) {
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			if (ann.annotationType().getName().equals(annotationType)) {
				Set<String> types = new LinkedHashSet<String>();
				Annotation[] metaAnns = ann.annotationType().getAnnotations();
				for (Annotation metaAnn : metaAnns) {
					types.add(metaAnn.annotationType().getName());
					for (Annotation metaMetaAnn : metaAnn.annotationType().getAnnotations()) {
						types.add(metaMetaAnn.annotationType().getName());
					}
				}
				return types;
			}
		}
		return null;
	}

	@Override
	public boolean hasAnnotation(String annotationType) {
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			if (ann.annotationType().getName().equals(annotationType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasMetaAnnotation(String annotationType) {
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			Annotation[] metaAnns = ann.annotationType().getAnnotations();
			for (Annotation metaAnn : metaAnns) {
				if (metaAnn.annotationType().getName().equals(annotationType)) {
					return true;
				}
				for (Annotation metaMetaAnn : metaAnn.annotationType().getAnnotations()) {
					if (metaMetaAnn.annotationType().getName().equals(annotationType)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isAnnotated(String annotationType) {
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			if (ann.annotationType().getName().equals(annotationType)) {
				return true;
			}
			for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
				if (metaAnn.annotationType().getName().equals(annotationType)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Map<String, Object> getAnnotationAttributes(String annotationType) {
		return getAnnotationAttributes(annotationType, false);
	}

	@Override
	public Map<String, Object> getAnnotationAttributes(String annotationType,
			boolean classValuesAsString) {
		Annotation[] anns = getIntrospectedClass().getAnnotations();
		for (Annotation ann : anns) {
			if (ann.annotationType().getName().equals(annotationType)) {
				return AnnotationUtils.getAnnotationAttributes(ann, classValuesAsString);
			}
			for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
				if (metaAnn.annotationType().getName().equals(annotationType)) {
					return AnnotationUtils.getAnnotationAttributes(metaAnn, classValuesAsString);
				}
			}
		}
		return null;
	}

	@Override
	public boolean hasAnnotatedMethods(String annotationType) {
		Method[] methods = getIntrospectedClass().getDeclaredMethods();
		for (Method method : methods) {
			for (Annotation ann : method.getAnnotations()) {
				if (ann.annotationType().getName().equals(annotationType)) {
					return true;
				}
				else {
					for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
						if (metaAnn.annotationType().getName().equals(annotationType)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public Set<MethodMetadata> getAnnotatedMethods(String annotationType) {
		Method[] methods = getIntrospectedClass().getDeclaredMethods();
		Set<MethodMetadata> annotatedMethods = new LinkedHashSet<MethodMetadata>();
		for (Method method : methods) {
			for (Annotation ann : method.getAnnotations()) {
				if (ann.annotationType().getName().equals(annotationType)) {
					annotatedMethods.add(new StandardMethodMetadata(method));
					break;
				}
				else {
					for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
						if (metaAnn.annotationType().getName().equals(annotationType)) {
							annotatedMethods.add(new StandardMethodMetadata(method));
							break;
						}
					}
				}
			}
		}
		return annotatedMethods;
	}

}
