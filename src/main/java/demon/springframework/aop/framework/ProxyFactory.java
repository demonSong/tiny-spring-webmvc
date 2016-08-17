package demon.springframework.aop.framework;

import org.aopalliance.intercept.Interceptor;

public class ProxyFactory extends AdvisedSupport{
	
	public ProxyFactory(Class<?> proxyInterface,Interceptor interceptor ){
		addInterface(proxyInterface);
		addAdvice(interceptor);
	}
	
	public Object getProxy(ClassLoader classLoader){
		return null;
	}

}
