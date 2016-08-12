package demon.springframework.beans.factory.config;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import demon.springframework.beans.BeanMetadataElement;

public class TypedStringValue implements BeanMetadataElement {

	private String value;
	
	private volatile Object targetType;
	
	private Object source;

	private String specifiedTypeName;

	private volatile boolean dynamic;
	
	public TypedStringValue(String value) {
		setValue(value);
	}

	public TypedStringValue(String value, Class<?> targetType) {
		setValue(value);
		setTargetType(targetType);
	}

	public TypedStringValue(String value, String targetTypeName) {
		setValue(value);
		setTargetTypeName(targetTypeName);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setTargetType(Class<?> targetType) {
		Assert.notNull(targetType, "'targetType' must not be null");
		this.targetType = targetType;
	}

	public Class<?> getTargetType() {
		Object targetTypeValue = this.targetType;
		if (!(targetTypeValue instanceof Class)) {
			throw new IllegalStateException("Typed String value does not carry a resolved target type");
		}
		return (Class<?>) targetTypeValue;
	}

	public void setTargetTypeName(String targetTypeName) {
		Assert.notNull(targetTypeName, "'targetTypeName' must not be null");
		this.targetType = targetTypeName;
	}

	public String getTargetTypeName() {
		Object targetTypeValue = this.targetType;
		if (targetTypeValue instanceof Class) {
			return ((Class<?>) targetTypeValue).getName();
		}
		else {
			return (String) targetTypeValue;
		}
	}

	public boolean hasTargetType() {
		return (this.targetType instanceof Class);
	}

	public Class<?> resolveTargetType(ClassLoader classLoader) throws ClassNotFoundException {
		if (this.targetType == null) {
			return null;
		}
		Class<?> resolvedClass = ClassUtils.forName(getTargetTypeName(), classLoader);
		this.targetType = resolvedClass;
		return resolvedClass;
	}


	public void setSource(Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}

	public void setSpecifiedTypeName(String specifiedTypeName) {
		this.specifiedTypeName = specifiedTypeName;
	}

	public String getSpecifiedTypeName() {
		return this.specifiedTypeName;
	}

	public void setDynamic() {
		this.dynamic = true;
	}

	public boolean isDynamic() {
		return this.dynamic;
	}


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TypedStringValue)) {
			return false;
		}
		TypedStringValue otherValue = (TypedStringValue) other;
		return (ObjectUtils.nullSafeEquals(this.value, otherValue.value) &&
				ObjectUtils.nullSafeEquals(this.targetType, otherValue.targetType));
	}

	@Override
	public int hashCode() {
		return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.targetType);
	}

	@Override
	public String toString() {
		return "TypedStringValue: value [" + this.value + "], target type [" + this.targetType + "]";
	}

}
