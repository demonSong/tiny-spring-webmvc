package demon.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.ObjectUtils;

import demon.springframework.beans.DmnBeanDefinition;
import demon.springframework.beans.factory.AbstractBeanFactory;
import demon.springframework.beans.factory.config.TypedStringValue;

class BeanDefinitionValueResolver {
	
	private final AbstractBeanFactory beanFactory;

	private final String beanName;

	private final DmnBeanDefinition beanDefinition;
	
	public BeanDefinitionValueResolver(
			AbstractBeanFactory beanFactory, String beanName, DmnBeanDefinition beanDefinition) {

		this.beanFactory = beanFactory;
		this.beanName = beanName;
		this.beanDefinition = beanDefinition;
	}
	
	public Object resolveValueIfNecessary(Object argName, Object value) {
		
		if (value instanceof TypedStringValue) {
			// Convert value to target type here.
			TypedStringValue typedStringValue = (TypedStringValue) value;
			Object valueObject = evaluate(typedStringValue);
			try {
				Class<?> resolvedTargetType = resolveTargetType(typedStringValue);
				if (resolvedTargetType != null) {
					return null;
				}
				else {
					return valueObject;
				}
			}
			catch (Throwable ex) {
				// Improve the message by showing the context.
				throw new BeanCreationException(
						this.beanDefinition.toString(), this.beanName,
						"Error converting typed String value for " + argName, ex);
			}
		}
		else {
			return evaluate(value);
		}
	}
	
	protected Object evaluate(TypedStringValue value) {
		Object result = this.beanFactory.evaluateBeanDefinitionString(value.getValue(), this.beanDefinition);
		if (!ObjectUtils.nullSafeEquals(result, value.getValue())) {
			value.setDynamic();
		}
		return result;
	}
	
	protected Object evaluate(Object value) {
		if (value instanceof String) {
			return this.beanFactory.evaluateBeanDefinitionString((String) value, this.beanDefinition);
		}
		else {
			return value;
		}
	}
	
	protected Class<?> resolveTargetType(TypedStringValue value) throws ClassNotFoundException {
		if (value.hasTargetType()) {
			return value.getTargetType();
		}
		return value.resolveTargetType(this.beanFactory.getBeanClassLoader());
	}
}
