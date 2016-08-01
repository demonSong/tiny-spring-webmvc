package demon.springframework.beans.factory.support;

import demon.springframework.beans.BeanDefinition;

public class GenericBeanDefinition extends BeanDefinition{
	
	public GenericBeanDefinition() {
		super();
	}
	
	@Override
	public String toString() {
		return "Generic bean: " + super.toString();
	}

}
