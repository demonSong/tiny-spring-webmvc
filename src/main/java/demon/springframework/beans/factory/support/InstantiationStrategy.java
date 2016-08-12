package demon.springframework.beans.factory.support;

import org.springframework.beans.BeansException;

import demon.springframework.beans.factory.BeanFactory;

public interface InstantiationStrategy {
	
	Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner)
			throws BeansException;

}
