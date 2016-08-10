package demon.springframework.beans.factory.support;

import demon.springframework.beans.DmnBeanDefinition;

public class RootBeanDefinition extends AbstractBeanDefinition{
	
	public RootBeanDefinition() {
		super();
	}

	RootBeanDefinition(DmnBeanDefinition original) {
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
	public String toString() {
		return "Root bean: " + super.toString();
	}
	
}
