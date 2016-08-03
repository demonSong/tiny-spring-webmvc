package demon.springframework.beans.factory.config;

import java.beans.PropertyDescriptor;

import demon.springframework.beans.PropertyValues;
import demon.springframework.beans.config.InstantiationAwareBeanPostProcessor;

public class InstantiationAwareBeanPostProcessorAdapter implements
		InstantiationAwareBeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws Exception {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws Exception {
		return bean;
	}

	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass,
			String beanName) throws Exception {
		return null;
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName)
			throws Exception {
		return true;
	}

	
	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs,
			PropertyDescriptor[] pds, Object bean, String beanName)
			throws Exception {
		return pvs;
	}

}
