package demon.springframework.remoting.rmi;

import demon.springframework.beans.factory.FactoryBean;

public class RmiProxyFactoryBean implements FactoryBean<Object>{

	private Object serviceProxy;
	
	@Override
	public Object getObject() throws Exception {
		return this.serviceProxy;
	}

	@Override
	public Class<?> getObjectType() {
		return null;
	}

	/**
	 * 默认的便是singleton 单例模式了
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

}
