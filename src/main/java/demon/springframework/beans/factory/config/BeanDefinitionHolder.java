package demon.springframework.beans.factory.config;

import org.springframework.util.Assert;

import demon.springframework.beans.BeanDefinition;

/**
 * 所有需要Map封装的类 都需要hashcode方法
 * 类的功能为提供抽象数据结构,以及域读写功能
 * @author demon.song
 *
 */
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
