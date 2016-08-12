package demon.springframework.beans;

import demon.springframework.beans.factory.config.ConfigurableBeanFactory;

public interface DmnBeanDefinition {
	
	String SCOPE_SINGLETON=ConfigurableBeanFactory.SCOPE_SINGLETON;
	
	String getBeanClassName();
	
	void setBeanClassName(String beanClassName);
	
	String getParentName();

	void setParentName(String parentName);
	
	/**
	 * 支持单例
	 * @return
	 */
	String getScope();

	void setScope(String scope);
	
	boolean isSingleton();

	/**
	 * 增加属性
	 */
	MutablePropertyValues getPropertyValues();
	
	/**
	 * Return a human-readable description of this bean definition.
	 */
	String getDescription();

}
