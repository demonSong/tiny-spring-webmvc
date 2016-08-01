package demon.springframework.beans.factory.config;

import org.springframework.util.Assert;

import demon.springframework.beans.BeanDefinition;

public class BeanDefinitionHolder {
	
	private final BeanDefinition beanDefinition;
	
	private final String beanName;
	
	public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName) {
		Assert.notNull(beanDefinition, "BeanDefinition must not be null");
		Assert.notNull(beanName, "Bean name must not be null");
		this.beanDefinition = beanDefinition;
		this.beanName = beanName;
	}
	
	public BeanDefinition getBeanDefinition() {
		return this.beanDefinition;
	}
	
	public String getBeanName() {
		return this.beanName;
	}
}
