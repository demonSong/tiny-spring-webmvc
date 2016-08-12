package demon.springframework.beans.factory.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.BeanCreationException;

import demon.springframework.beans.MutablePropertyValues;
import demon.springframework.beans.TestPropertyValue;
import demon.springframework.beans.TestPropertyValues;
import demon.springframework.beans.factory.AbstractBeanFactory;
import demon.springframework.beans.factory.BeanFactory;

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
		
		final Object bean =createBeanInstance(beanName, mbd,args);
		
		//initialize the bean instance
		Object exposedObject =bean;
		populateBean(beanName, mbd, bean);
		
		return null;
	}
	
	protected void populateBean(String beanName,RootBeanDefinition mbd,Object bean) {
		TestPropertyValues pvs =mbd.getPropertyValues();
		applyPropertyValues(beanName, mbd, bean,pvs);
	}

	private void applyPropertyValues(String beanName, RootBeanDefinition mbd,
			Object bean, TestPropertyValues pvs) {
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
				Object convertedValue =resolvedValue;
				System.out.println(propertyName +originalValue);	
			}
		}

	}

	protected Object createBeanInstance(String beanName, RootBeanDefinition mbd,
			Object[] args) {
		return instantiateBean(beanName, mbd);
	}
	
	protected Object instantiateBean(final String beanName, final RootBeanDefinition mbd) {
		Object beanInstance;
		final BeanFactory parent = this;
		beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
		return beanInstance;
	}
	
	public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
		this.instantiationStrategy = instantiationStrategy;
	}

	protected InstantiationStrategy getInstantiationStrategy() {
		return this.instantiationStrategy;
	}
}
