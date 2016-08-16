package demon.springframework.beans.factory.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.ObjectUtils;

import demon.springframework.beans.BeanWrapper;
import demon.springframework.beans.BeanWrapperImpl;
import demon.springframework.beans.MutablePropertyValues;
import demon.springframework.beans.TestPropertyValue;
import demon.springframework.beans.TestPropertyValues;
import demon.springframework.beans.TypeConverter;
import demon.springframework.beans.factory.AbstractBeanFactory;
import demon.springframework.beans.factory.BeanFactory;
import demon.springframework.beans.factory.config.TypedStringValue;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

	private InstantiationStrategy instantiationStrategy =new CglibSubclassingInstantiationStrategy();
	
	@Override
	protected Object createBean(String beanName, RootBeanDefinition mbd,
			Object[] args) throws BeanCreationException {
		//为什么需要解析 beanClass,解析之后 beanclass中就有class对象了
		resolveBeanClass(mbd, beanName);
		
		Object beanInstance= doCreateBean(beanName, mbd, args);
		
		return beanInstance;
	}

	protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd,
			final Object[] args) {
		BeanWrapper instanceWrapper =null;
		instanceWrapper =createBeanInstance(beanName,mbd,args);
		
		final Object bean =(instanceWrapper !=null ? instanceWrapper.getWrappedInstance() : null);

		//initialize the bean instance
		Object exposedObject =bean;
		populateBean(beanName, mbd, instanceWrapper);
		
		return exposedObject;
	}
	
	protected void populateBean(String beanName,RootBeanDefinition mbd,BeanWrapper bean) {
		TestPropertyValues pvs =mbd.getPropertyValues();
		applyPropertyValues(beanName, mbd, bean,pvs);
	}

	private void applyPropertyValues(String beanName, RootBeanDefinition mbd,
			BeanWrapper bw, TestPropertyValues pvs) {
		if(pvs == null || pvs.isEmpty()){
			return;
		}
		MutablePropertyValues mpvs = null;
		List<TestPropertyValue> original;
		
		if(pvs instanceof MutablePropertyValues){
			mpvs = (MutablePropertyValues) pvs;
			original =mpvs.getPropertyValueList();
		}
		else {
			original = Arrays.asList(pvs.getPropertyValues());
		}
		
		TypeConverter converter =bw;
		
		BeanDefinitionValueResolver valueResolver =new BeanDefinitionValueResolver(this, beanName, mbd);
		
		List<TestPropertyValue> deepCopy =new ArrayList<TestPropertyValue>(original.size());
		boolean resolveNecessary =false;
		for(TestPropertyValue pv : original){
			if(pv.isConverted()){
				deepCopy.add(pv);
			}
			else {
				String propertyName =pv.getName();
				Object originalValue = pv.getValue();
				Object resolvedValue =valueResolver.resolveValueIfNecessary(pv, originalValue);
				//resolvedValue均为字符串
				Object convertedValue =resolvedValue;
				//需要判断是否convert的用意是什么? 把字符串形式转为真正的对象
				convertedValue = convertForProperty(resolvedValue,propertyName,bw,converter);
				if(resolvedValue == originalValue){
					pv.setConvertedValue(convertedValue);
					deepCopy.add(pv);
				}
				else if (true && originalValue instanceof TypedStringValue &&
						!((TypedStringValue) originalValue).isDynamic() &&
						!(convertedValue instanceof Collection || ObjectUtils.isArray(convertedValue))) {
					pv.setConvertedValue(convertedValue);
					deepCopy.add(pv);
				}
				else {
					resolveNecessary = true;
					deepCopy.add(new TestPropertyValue(pv, convertedValue));
				}
			}
		}
		if (mpvs != null && !resolveNecessary) {
			mpvs.setConverted();
		}

		//set our (possibly massaged) deep copy
		try {
			bw.setPropertyValues(new MutablePropertyValues(deepCopy));
		} catch (BeansException ex) {
			// TODO Auto-generated catch block
			throw new BeanCreationException(
					mbd.toString(), beanName, "Error setting property values", ex);
		}
		
	}
	
	private Object convertForProperty(Object value, String propertyName, BeanWrapper bw, TypeConverter converter) {
		if (converter instanceof BeanWrapperImpl) {
			return ((BeanWrapperImpl) converter).convertForProperty(value, propertyName);
		}
		else {
			return null;
		}
	}

	protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd,
			Object[] args) {
		return instantiateBean(beanName, mbd);
	}
	
	protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd) {
		Object beanInstance;
		final BeanFactory parent = this;
		beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
		BeanWrapper bw =new BeanWrapperImpl(beanInstance);
		return bw;
	}
	
	public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
		this.instantiationStrategy = instantiationStrategy;
	}

	protected InstantiationStrategy getInstantiationStrategy() {
		return this.instantiationStrategy;
	}
}
