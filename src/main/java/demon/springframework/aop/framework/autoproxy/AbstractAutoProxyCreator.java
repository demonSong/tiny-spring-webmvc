package demon.springframework.aop.framework.autoproxy;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;

import demon.springframework.aop.BeanFactoryAware;
import demon.springframework.aop.TargetSource;
import demon.springframework.aop.target.SingletonTargetSource;
import demon.springframework.beans.BeanPostProcessor;
import demon.springframework.beans.factory.BeanFactory;

public abstract class AbstractAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware{
	
	protected static final Object[] DO_NOT_PROXY = null;
	
	private final Set<Object> earlyProxyReferences =Collections.newSetFromMap(new ConcurrentHashMap<Object, Boolean>(16));
	
	private final Map<Object, Boolean> advisedBeans = new ConcurrentHashMap<Object, Boolean>(64);

	private final Map<Object, Class<?>> proxyTypes = new ConcurrentHashMap<Object, Class<?>>(16);
	
	private BeanFactory beanFactory;
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		System.out.println("AutoProxyCreator: bean初始化后,执行该方法");
		if(bean !=null){
			Object cacheKey = getCacheKey(bean.getClass(),beanName);
			if(!this.earlyProxyReferences.contains(cacheKey)){
				return wrapIfNecessary(bean,beanName,cacheKey);
			}
		}
		return bean;
	}
	
	protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
		
		if(Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))){
			return bean;
		}
		
		//根据beanClass来获得是否有Advice,有自然是Proxy
		Object[] specificInterceptors  = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
		if(specificInterceptors != DO_NOT_PROXY){
			//用来记录该bean是否为proxy的缓存变量
			this.advisedBeans.put(cacheKey,Boolean.TRUE);
			Object proxy =createProxy(bean.getClass(),beanName,specificInterceptors,new SingletonTargetSource(bean));
			this.proxyTypes.put(cacheKey,proxy.getClass());
			return proxy;
		}
		
		this.advisedBeans.put(cacheKey, Boolean.FALSE);
		return bean;
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	protected BeanFactory getBeanFactory(){
		return this.beanFactory;
	}
	
	protected Object createProxy(
			Class<?> beanClass, String beanName, Object[] specificInterceptors, TargetSource targetSource) {
		return null;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}
	
	protected Object getCacheKey(Class<?> beanClass, String beanName) {
		return beanClass.getName()+"_"+beanName;
	}
	
	protected abstract Object[] getAdvicesAndAdvisorsForBean(
			Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException;
	
}
