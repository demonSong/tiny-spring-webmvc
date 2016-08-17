package demon.springframework.beans.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;

import demon.springframework.beans.factory.FactoryBean;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{

	private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>(16);
	
	protected Object getCachedObjectForFactoryBean(String beanName) {
		Object object = this.factoryBeanObjectCache.get(beanName);
		return (object != NULL_OBJECT ? object : null);
	}
	
	protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess) {
		if(factory.isSingleton()){
			Object object =this.factoryBeanObjectCache.get(beanName);
			if(object ==null){
				object =doGetObjectFromFactoryBean(factory,beanName);
				//当另外一个线程来时，才需要这样的判断
				Object alreadyThere =this.factoryBeanObjectCache.get(beanName);
				if(alreadyThere !=null){
					object =alreadyThere;
				}
				else{
					if (object != null && shouldPostProcess) {
						try {
							object = postProcessObjectFromFactoryBean(object, beanName);
						}
						catch (Throwable ex) {
							throw new BeanCreationException(beanName,
									"Post-processing of FactoryBean's singleton object failed", ex);
						}
					}
					this.factoryBeanObjectCache.put(beanName, (object !=null ? object : NULL_OBJECT));
				}
			}
			return (object != NULL_OBJECT ? object : null);
		}
		else {
			return null;	
		}
	}
	
	protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
		return object;
	}
	
	private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, final String beanName)
			throws BeanCreationException {

		Object object;
		try {
			object = factory.getObject();
		}
		catch (FactoryBeanNotInitializedException ex) {
			throw new BeanCurrentlyInCreationException(beanName, ex.toString());
		}
		catch (Throwable ex) {
			throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
		}

		// Do not accept a null value for a FactoryBean that's not fully
		// initialized yet: Many FactoryBeans just return null then.
		if (object == null) {
			throw new BeanCurrentlyInCreationException(
					beanName, "FactoryBean which is currently in creation returned null from getObject");
		}
		return object;
	}
}
