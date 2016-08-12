package demon.springframework.beans.factory.support;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import demon.springframework.beans.factory.BeanFactory;

public class SimpleInstantiationStrategy implements InstantiationStrategy{

	@Override
	public Object instantiate(RootBeanDefinition beanDefinition,
			String beanName, BeanFactory owner) throws BeansException {
		Constructor<?> constructorToUse;
		final Class<?> clazz = beanDefinition.getBeanClass();
		try {
			constructorToUse =	clazz.getDeclaredConstructor((Class[]) null);
			return BeanUtils.instantiateClass(constructorToUse);
		}
		catch (Exception e) {
			throw new BeanInstantiationException(clazz, "No default constructor found", e);
		}
	}

}
