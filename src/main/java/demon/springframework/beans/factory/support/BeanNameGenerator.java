package demon.springframework.beans.factory.support;

import demon.springframework.beans.BeanDefinition;

public interface BeanNameGenerator {
	
	String generateBeanName(BeanDefinition definition,BeanDefinitionRegistry registry);
}
