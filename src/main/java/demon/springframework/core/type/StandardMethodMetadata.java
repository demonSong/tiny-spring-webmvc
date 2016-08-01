package demon.springframework.core.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

public class StandardMethodMetadata implements MethodMetadata{

	private final Method introspectedMethod;

	public StandardMethodMetadata(Method introspectedMethod) {
		Assert.notNull(introspectedMethod, "Method must not be null");
		this.introspectedMethod = introspectedMethod;
	}


	public final Method getIntrospectedMethod() {
		return this.introspectedMethod;
	}

	
	public String getMethodName() {
		return this.introspectedMethod.getName();
	}
	
	public String getDeclaringClassName() {
		return this.introspectedMethod.getDeclaringClass().getName();
	}

	public boolean isStatic() {
		return Modifier.isStatic(this.introspectedMethod.getModifiers());
	}

	public boolean isFinal() {
		return Modifier.isFinal(this.introspectedMethod.getModifiers());
	}

	public boolean isOverridable() {
		return (!isStatic() && !isFinal() && !Modifier.isPrivate(this.introspectedMethod.getModifiers()));
	}

	public boolean isAnnotated(String annotationType) {
		Annotation[] anns = this.introspectedMethod.getAnnotations();
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

	public Map<String, Object> getAnnotationAttributes(String annotationType) {
		Annotation[] anns = this.introspectedMethod.getAnnotations();
		for (Annotation ann : anns) {
			if (ann.annotationType().getName().equals(annotationType)) {
				return AnnotationUtils.getAnnotationAttributes(ann, true);
			}
			for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
				if (metaAnn.annotationType().getName().equals(annotationType)) {
					return AnnotationUtils.getAnnotationAttributes(metaAnn, true);
				}
			}
		}
		return null;
	}

}
