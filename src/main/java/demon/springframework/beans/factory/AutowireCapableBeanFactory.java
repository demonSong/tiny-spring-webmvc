package demon.springframework.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import demon.springframework.BeanReference;
import demon.springframework.aop.BeanFactoryAware;
import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.PropertyValue;

/**
 * 可自动装配内容的BeanFactory
 * 
 * @author yihua.huang@dianping.com
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory {

	protected void applyPropertyValues(Object bean, BeanDefinition mbd) throws Exception {
		if (bean instanceof BeanFactoryAware) {
			((BeanFactoryAware) bean).setBeanFactory(this);
		}
		for (PropertyValue propertyValue : mbd.getPropertyValues().getPropertyValues()) {
			Object value = propertyValue.getValue();
			if (value instanceof BeanReference) {
				BeanReference beanReference = (BeanReference) value;
				value = getBean(beanReference.getName());
			}
			//针对这样的方法,是必须要重写set方法的,否则没法进行注入
			try {
				Method declaredMethod = bean.getClass().getDeclaredMethod(
						"set" + propertyValue.getName().substring(0, 1).toUpperCase()
								+ propertyValue.getName().substring(1), value.getClass());
				declaredMethod.setAccessible(true);
				declaredMethod.invoke(bean, value);
				
			} catch (NoSuchMethodException e) {//没有set方法时,直接field注入
				//没有对应的method方法
				Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
				declaredField.setAccessible(true);
				declaredField.set(bean, value);
			}
		}
	}
}
