package demon.springframework.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import demon.springframework.BeanReference;
import demon.springframework.aop.BeanFactoryAware;
import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.BeanPostProcessor;
import demon.springframework.beans.PropertyValue;
import demon.springframework.beans.PropertyValues;
import demon.springframework.beans.config.InstantiationAwareBeanPostProcessor;
import demon.springframework.beans.factory.config.DependencyDescriptor;

/**
 * 可自动装配内容的BeanFactory
 * 可以说该类是abstractFactory的增强实现
 * @author yihua.huang@dianping.com
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory implements demon.springframework.beans.factory.config.AutowireCapableBeanFactory{
	
	//实现了bean的依赖注入过程
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
	
	//实现bean注解形式的依赖注入 不同形式的依赖注入分发的路口
	@SuppressWarnings("unused")
	@Override
	protected void populateBean(String beanName, BeanDefinition mbd, Object bean)throws Exception {
		PropertyValues pvs =mbd.getPropertyValues();
		
		boolean continueWithPropertyPopulation = true;
		
		if(hasInstantiationAwareBeanPostProcessors()){
			for (BeanPostProcessor bp : getBeanPostProcessors()) {
				if(bp instanceof InstantiationAwareBeanPostProcessor){
					InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
					//使用beanWrapper 干吗用的啊
					//判断后置处理器是否处理完成,如果处理完成直接返回
					if(!ibp.postProcessAfterInstantiation(null, beanName) && false){
						continueWithPropertyPopulation =false;
						break;	
					}
				}
			}
		}
		
		if(!continueWithPropertyPopulation){
			return;
		}
		
		boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
		
		if(hasInstAwareBpps){
			if (hasInstAwareBpps) {
				for (BeanPostProcessor bp : getBeanPostProcessors()) {
					if (bp instanceof InstantiationAwareBeanPostProcessor) {
						InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
						pvs = ibp.postProcessPropertyValues(pvs, null, bean, beanName);
						if (pvs == null) {
							return;
						}
					}
				}
			}
		}
	}
	
	//以下方法应该是在default factory中实现,但这里为了简化类层次结构,就不再划分新的类来完成类似的功能
	public Object resolveDependency(DependencyDescriptor descriptor, String beanName,
			Set<String> autowiredBeanNames) throws Exception  {
		return doResolveDependency(descriptor, descriptor.getDependencyType(), beanName, autowiredBeanNames);
	}
	
	protected Object doResolveDependency(DependencyDescriptor descriptor, Class<?> type, String beanName,
			Set<String> autowiredBeanNames) throws Exception  {
		//找到匹配的bean
		Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
		// We have exactly one match.
		Map.Entry<String, Object> entry = matchingBeans.entrySet().iterator().next();
		if (autowiredBeanNames != null) {
			autowiredBeanNames.add(entry.getKey());
		}
		return entry.getValue();
	}
	
	protected Map<String, Object> findAutowireCandidates(
			String beanName, Class requiredType, DependencyDescriptor descriptor) {
		//源码中为什么要多此一举呢?
		Map<String, Object> result;
		try {
			String[] candidateNames = getBeanNamesForType(requiredType, true, true);
			result = new LinkedHashMap<String, Object>(candidateNames.length);
			for (String candidateName : candidateNames) {
				if (!candidateName.equals(beanName)) {
					result.put(candidateName, getBean(candidateName));
				}
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	public String[] getBeanNamesForType(Class type, boolean includeNonSingletons, boolean allowEagerInit) {
		List<String> result = new ArrayList<String>();

		// Check all bean definitions.
		String[] beanDefinitionNames = getBeanDefinitionNames();
		for (String beanName : beanDefinitionNames) {
			try {
				BeanDefinition mbd = getBeanDefinition(beanName);
				boolean matchFound = (allowEagerInit || true) &&(includeNonSingletons || true) && isTypeMatch(beanName, type);
				if (matchFound) {
					result.add(beanName);
				}
			}
			catch (Exception ex) {
			}
		}
		return StringUtils.toStringArray(result);
	}

	@Override
	protected void invokeAwareMethods(String beanName, Object bean) {
		if(bean instanceof BeanFactoryAware){
			try {
				((BeanFactoryAware) bean).setBeanFactory(AutowireCapableBeanFactory.this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
