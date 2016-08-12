package demon.springframework.beans.factory.support;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.Assert;

import demon.springframework.beans.factory.ObjectFactory;
import demon.springframework.beans.factory.config.SingletonBeanRegistry;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
	
	protected static final Object NULL_OBJECT = new Object();

	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(64);
	
	private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>(16);
	
	private final Set<String> registeredSingletons = new LinkedHashSet<String>(64);

	protected void addSingleton(String beanName, Object singletonObject) {
		synchronized (this.singletonObjects) {
			this.singletonObjects.put(beanName, (singletonObject != null ? singletonObject : NULL_OBJECT));
			this.singletonFactories.remove(beanName);
			this.registeredSingletons.add(beanName);
		}
	}
	
	@Override
	public Object getSingleton(String beanName) {
		return null;
	}
	
	public Object getSingleton(String beanName ,ObjectFactory<?> singletonFactory){
		Assert.notNull(beanName, "'beanName' must not be null");
		synchronized (this.singletonObjects) {
			Object singletonObject =this.singletonObjects.get(beanName);
			if(singletonObject == null){
				beforeSingletonCreation(beanName);
				try {
					singletonObject = singletonFactory.getObject();
				}
				catch (BeanCreationException ex) {
					throw ex;
				}
				finally {
					afterSingletonCreation(beanName);
				}
				addSingleton(beanName, singletonObject);
			}
		
			return (singletonObject != NULL_OBJECT ? singletonObject : null);
		}
	}
	
	protected void beforeSingletonCreation(String beanName) {
	}
	
	protected void afterSingletonCreation(String beanName) {
	}

}
