package demon.springframework.beans.factory.config;

import java.util.Set;

import demon.springframework.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory{
	
	public Object resolveDependency(DependencyDescriptor descriptor, String beanName,
			Set<String> autowiredBeanNames) throws Exception;

}
