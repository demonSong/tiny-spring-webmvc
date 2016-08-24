package demon.springframework.aop.framework.autoproxy;

import org.springframework.beans.BeansException;

import demon.springframework.beans.BeanPostProcessor;

public abstract class AbstractAutoProxyCreator implements BeanPostProcessor{
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return null;
	}
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return null;
	}
	
}
