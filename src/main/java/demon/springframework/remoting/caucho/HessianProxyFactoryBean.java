package demon.springframework.remoting.caucho;

import demon.springframework.aop.framework.ProxyFactory;
import demon.springframework.beans.factory.FactoryBean;

public class HessianProxyFactoryBean extends HessianClientInterceptor implements FactoryBean<Object>{

	private Object serviceProxy;


	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
	}


	@Override
	public Object getObject() {
		return this.serviceProxy;
	}

	@Override
	public Class<?> getObjectType() {
		return getServiceInterface();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
