package demon.springframework.beans.factory.support;

import demon.springframework.beans.DmnBeanDefinition;

public class RootBeanDefinition extends AbstractBeanDefinition{
	
	public RootBeanDefinition() {
		super();
	}

	public RootBeanDefinition(DmnBeanDefinition original) {
		super(original);
	}

	public RootBeanDefinition(Class<?> beanClass){
		super();
		setBeanClass(beanClass);
	}
	
	public RootBeanDefinition(String beanClassName){
		setBeanClassName(beanClassName);
	}

	@Override
	public String getParentName() {
		return null;
	}

	@Override
	public void setParentName(String parentName) {
		if (parentName != null) {
			throw new IllegalArgumentException("Root bean cannot be changed into a child bean with parent reference");
		}
	}
	
	
	@Override
	public RootBeanDefinition cloneBeanDefinition() {
		return new RootBeanDefinition(this);
	}
	
	@Override
	public String toString() {
		return "Root bean: " + super.toString();
	}
}
