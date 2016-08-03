package demon.springframework.beans.config;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeansException;

import demon.springframework.beans.BeanPostProcessor;
import demon.springframework.beans.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{
	
	Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws Exception;

	//注意返回值和beanPostProcessor的区别
	boolean postProcessAfterInstantiation(Object bean, String beanName) throws Exception;

	PropertyValues postProcessPropertyValues(
			PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
			throws Exception;

}
