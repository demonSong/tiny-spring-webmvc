package demon.springframework.aop.framework;

import org.aopalliance.intercept.Interceptor;

public class ProxyFactory extends ProxyCreatorSupport{
	
	public ProxyFactory(Class<?> proxyInterface,Interceptor interceptor ){
		addInterface(proxyInterface);
		addAdvice(interceptor);
	}
	
	public Object getProxy(ClassLoader classLoader){
		return createAopProxy().getProxy(classLoader);
	}

}
