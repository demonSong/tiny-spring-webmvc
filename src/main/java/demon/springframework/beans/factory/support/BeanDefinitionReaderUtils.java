package demon.springframework.beans.factory.support;

import demon.springframework.beans.factory.config.BeanDefinitionHolder;

public class BeanDefinitionReaderUtils {
	
	public static void registerBeanDefinition(
			BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) throws Exception{

		// Register bean definition under primary name.
		String beanName = definitionHolder.getBeanName();
		registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
		
		//可以增加新的功能
	}
	

}
