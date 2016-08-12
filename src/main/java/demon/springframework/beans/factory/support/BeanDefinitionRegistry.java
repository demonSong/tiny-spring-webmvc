package demon.springframework.beans.factory.support;

import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.DmnBeanDefinition;

public interface BeanDefinitionRegistry {
	
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception;
	
	void registerBeanDefinition(String beanName, DmnBeanDefinition beanDefinition) throws Exception;
	
	void removeBeanDefinition(String beanName);
	
	BeanDefinition getBeanDefinition(String beanName);
	
	boolean containsBeanDefinition(String beanName);
	
	String[] getBeanDefinitionNames();

	int getBeanDefinitionCount();

	boolean isBeanNameInUse(String beanName);

}
