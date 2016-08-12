package demon.springframework.beans.factory.config;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import demon.springframework.beans.DmnBeanDefinition;

/**
 * 所有需要Map封装的类 都需要hashcode方法
 * 类的功能为提供抽象数据结构,以及域读写功能
 * @author demon.song
 *
 */
public class TestBeanDefinitionHolder {
	
	private final DmnBeanDefinition beanDefinition;
	
	private final String beanName;
	
	public TestBeanDefinitionHolder(DmnBeanDefinition beanDefinition, String beanName) {
		Assert.notNull(beanDefinition, "BeanDefinition must not be null");
		Assert.notNull(beanName, "Bean name must not be null");
		this.beanDefinition = beanDefinition;
		this.beanName = beanName;
	}
	
	public DmnBeanDefinition getBeanDefinition() {
		return this.beanDefinition;
	}
	
	public String getBeanName() {
		return this.beanName;
	}
	
	public String getShortDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("Bean definition with name '").append(this.beanName).append("'");
		return sb.toString();
	}
	
	public String getLongDescription() {
		StringBuilder sb = new StringBuilder(getShortDescription());
		sb.append(": ").append(this.beanDefinition);
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return getLongDescription();
	}
}
