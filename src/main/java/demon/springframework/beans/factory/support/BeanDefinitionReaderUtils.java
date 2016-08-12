package demon.springframework.beans.factory.support;

import org.springframework.util.ClassUtils;

import demon.springframework.beans.factory.config.BeanDefinitionHolder;
import demon.springframework.beans.factory.config.TestBeanDefinitionHolder;

public class BeanDefinitionReaderUtils {
	
	public static void registerBeanDefinition(
			BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) throws Exception{

		// Register bean definition under primary name.
		String beanName = definitionHolder.getBeanName();
		registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
		
		//可以增加新的功能
	}
	
	public static void registerBeanDefinition(
			TestBeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) throws Exception{

		// Register bean definition under primary name.
		String beanName = definitionHolder.getBeanName();
		registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
		//可以增加新的功能
	}
	
	public static AbstractBeanDefinition createBeanDefinition(String parentName,String className,ClassLoader classLoader) throws ClassNotFoundException {
		TestGenericBeanDefinition bd =new TestGenericBeanDefinition();
		bd.setParentName(parentName);
		if(className !=null){
			if(classLoader !=null){
				bd.setBeanClass(ClassUtils.forName(className,classLoader));
			}
			else {
				bd.setBeanClassName(className);
			}
		}
		return bd;
	}
	

}
