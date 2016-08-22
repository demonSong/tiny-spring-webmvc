package demon.springframework.remoting.rmi;

import demon.springframework.aop.framework.ProxyFactory;
import demon.springframework.beans.factory.BeanClassLoaderAware;
import demon.springframework.beans.factory.FactoryBean;

public class RmiProxyFactoryBean extends RmiClientInterceptor implements FactoryBean<Object>,BeanClassLoaderAware{

	private Object serviceProxy;
	
	@Override
	public Object getObject() throws Exception {
		return this.serviceProxy;
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (getServiceInterface() == null) {
			throw new IllegalArgumentException("Property 'serviceInterface' is required");
		}
		this.serviceProxy =new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
	}
	
	@Override
	public Class<?> getObjectType() {
		return getServiceInterface();
	}

	/**
	 * 默认的便是singleton 单例模式了
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

}
