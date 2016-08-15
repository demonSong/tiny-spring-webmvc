package demon.springframework.core.convert;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;

import demon.springframework.core.ResolvableType;

@SuppressWarnings("serial")
public class TypeDescriptor implements Serializable{
	
	static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
	
	private final Class<?> type;
	
	private Field field;
	
	private MethodParameter methodParameter;
	
	private final Annotation[] annotations;
	
	private final ResolvableType resolvableType;
	
	public TypeDescriptor(Property property) {
		Assert.notNull(property, "Property must not be null");
		this.resolvableType =ResolvableType.forMethodParameter(property.getMethodParameter());
		this.type =this.resolvableType.resolve(property.getType());
		this.annotations = nullSafeAnnotations(property.getAnnotations());
	}
	
	public Class<?> getType() {
		if (this.type != null) {
			return this.type;
		}
		else if (this.field != null) {
			return this.field.getType();
		}
		else if (this.methodParameter != null) {
			return this.methodParameter.getParameterType();
		}
		else {
			return null;
		}
	}

	private Annotation[] nullSafeAnnotations(Annotation[] annotations) {
		return (annotations != null ? annotations : EMPTY_ANNOTATION_ARRAY);
	}
}
