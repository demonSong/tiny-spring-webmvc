package demon.springframework.core.convert;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import demon.springframework.core.GenericTypeResolver;
import demon.springframework.core.MethodParameter;
import demon.springframework.util.ConcurrentReferenceHashMap;

public final class Property {

	private static Map<Property, Annotation[]> annotationCache =
			new ConcurrentReferenceHashMap<Property, Annotation[]>();

	private final Class<?> objectType;

	private final Method readMethod;

	private final Method writeMethod;

	private final String name;

	private final MethodParameter methodParameter;

	private Annotation[] annotations;

	public Property(Class<?> objectType, Method readMethod, Method writeMethod) {
		this(objectType, readMethod, writeMethod, null);
	}

	public Property(Class<?> objectType, Method readMethod, Method writeMethod, String name) {
		this.objectType = objectType;
		this.readMethod = readMethod;
		this.writeMethod = writeMethod;
		this.methodParameter = resolveMethodParameter();
		this.name = (name == null ? resolveName() : name);
	}


	/**
	 * The object declaring this property, either directly or in a superclass the object extends.
	 */
	public Class<?> getObjectType() {
		return this.objectType;
	}

	/**
	 * The name of the property: e.g. 'foo'
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * The property type: e.g. {@code java.lang.String}
	 */
	public Class<?> getType() {
		return this.methodParameter.getParameterType();
	}

	/**
	 * The property getter method: e.g. {@code getFoo()}
	 */
	public Method getReadMethod() {
		return this.readMethod;
	}

	/**
	 * The property setter method: e.g. {@code setFoo(String)}
	 */
	public Method getWriteMethod() {
		return this.writeMethod;
	}


	// package private

	MethodParameter getMethodParameter() {
		return this.methodParameter;
	}

	Annotation[] getAnnotations() {
		if(this.annotations == null) {
			this.annotations = resolveAnnotations();
		}
		return this.annotations;
	}


	// internal helpers

	private String resolveName() {
		if (this.readMethod != null) {
			int index = this.readMethod.getName().indexOf("get");
			if (index != -1) {
				index += 3;
			}
			else {
				index = this.readMethod.getName().indexOf("is");
				if (index == -1) {
					throw new IllegalArgumentException("Not a getter method");
				}
				index += 2;
			}
			return StringUtils.uncapitalize(this.readMethod.getName().substring(index));
		}
		else {
			int index = this.writeMethod.getName().indexOf("set") + 3;
			if (index == -1) {
				throw new IllegalArgumentException("Not a setter method");
			}
			return StringUtils.uncapitalize(this.writeMethod.getName().substring(index));
		}
	}

	private MethodParameter resolveMethodParameter() {
		MethodParameter read = resolveReadMethodParameter();
		MethodParameter write = resolveWriteMethodParameter();
		if (write == null) {
			if (read == null) {
				throw new IllegalStateException("Property is neither readable nor writeable");
			}
			return read;
		}
		if (read != null) {
			Class<?> readType = read.getParameterType();
			Class<?> writeType = write.getParameterType();
			if (!writeType.equals(readType) && writeType.isAssignableFrom(readType)) {
				return read;
			}
		}
		return write;
	}

	private MethodParameter resolveReadMethodParameter() {
		if (getReadMethod() == null) {
			return null;
		}
		return resolveParameterType(new MethodParameter(getReadMethod(), -1));
	}

	private MethodParameter resolveWriteMethodParameter() {
		if (getWriteMethod() == null) {
			return null;
		}
		return resolveParameterType(new MethodParameter(getWriteMethod(), 0));
	}

	private MethodParameter resolveParameterType(MethodParameter parameter) {
		// needed to resolve generic property types that parameterized by sub-classes e.g. T getFoo();
		GenericTypeResolver.resolveParameterType(parameter, getObjectType());
		return parameter;
	}

	private Annotation[] resolveAnnotations() {
		Annotation[] annotations = annotationCache.get(this);
		if(annotations == null) {
			Map<Class<? extends Annotation>, Annotation> annotationMap = new LinkedHashMap<Class<? extends Annotation>, Annotation>();
			addAnnotationsToMap(annotationMap, getReadMethod());
			addAnnotationsToMap(annotationMap, getWriteMethod());
			addAnnotationsToMap(annotationMap, getField());
			annotations = annotationMap.values().toArray(new Annotation[annotationMap.size()]);
			annotationCache.put(this, annotations);
		}
		return annotations;
	}

	private void addAnnotationsToMap(
		Map<Class<? extends Annotation>, Annotation> annotationMap,
		AnnotatedElement object) {
		if (object != null) {
			for (Annotation annotation : object.getAnnotations()) {
				annotationMap.put(annotation.annotationType(), annotation);
			}
		}
	}

	private Field getField() {
		String name = getName();
		if (!StringUtils.hasLength(name)) {
			return null;
		}
		Class<?> declaringClass = declaringClass();
		Field field = ReflectionUtils.findField(declaringClass, name);
		if (field == null) {
			// Same lenient fallback checking as in CachedIntrospectionResults...
			field = ReflectionUtils.findField(declaringClass,
					name.substring(0, 1).toLowerCase() + name.substring(1));
			if (field == null) {
				field = ReflectionUtils.findField(declaringClass,
						name.substring(0, 1).toUpperCase() + name.substring(1));
			}
		}
		return field;
	}

	private Class<?> declaringClass() {
		if (getReadMethod() != null) {
			return getReadMethod().getDeclaringClass();
		}
		else {
			return getWriteMethod().getDeclaringClass();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hashCode = 1;
		hashCode = prime * hashCode + ObjectUtils.nullSafeHashCode(this.objectType);
		hashCode = prime * hashCode + ObjectUtils.nullSafeHashCode(this.readMethod);
		hashCode = prime * hashCode + ObjectUtils.nullSafeHashCode(this.writeMethod);
		hashCode = prime * hashCode + ObjectUtils.nullSafeHashCode(this.name);
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Property other = (Property) obj;
		boolean equals = true;
		equals &= ObjectUtils.nullSafeEquals(this.objectType, other.objectType);
		equals &= ObjectUtils.nullSafeEquals(this.readMethod, other.readMethod);
		equals &= ObjectUtils.nullSafeEquals(this.writeMethod, other.writeMethod);
		equals &= ObjectUtils.nullSafeEquals(this.name, other.name);
		return equals;
	}
}