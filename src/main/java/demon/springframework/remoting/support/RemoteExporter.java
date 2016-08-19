package demon.springframework.remoting.support;

import org.springframework.util.ClassUtils;

import demon.springframework.aop.framework.ProxyFactory;


public class RemoteExporter extends RemotingSupport{
	
	private Object service;
	
	private Class<?> serviceInterface;
	
	private Boolean registerTraceInterceptor;

	private Object[] interceptors;
	
	public void setService(Object service) {
		this.service = service;
	}

	public Object getService() {
		return this.service;
	}

	public void setServiceInterface(Class<?> serviceInterface) {
		if (serviceInterface != null && !serviceInterface.isInterface()) {
			throw new IllegalArgumentException("'serviceInterface' must be an interface");
		}
		this.serviceInterface = serviceInterface;
	}

	public Class<?> getServiceInterface() {
		return this.serviceInterface;
	}

	public void setRegisterTraceInterceptor(boolean registerTraceInterceptor) {
		this.registerTraceInterceptor = Boolean.valueOf(registerTraceInterceptor);
	}

	public void setInterceptors(Object[] interceptors) {
		this.interceptors = interceptors;
	}


	protected void checkService() throws IllegalArgumentException {
		if (getService() == null) {
			throw new IllegalArgumentException("Property 'service' is required");
		}
	}

	protected void checkServiceInterface() throws IllegalArgumentException {
		Class<?> serviceInterface = getServiceInterface();
		Object service = getService();
		if (serviceInterface == null) {
			throw new IllegalArgumentException("Property 'serviceInterface' is required");
		}
		if (service instanceof String) {
			throw new IllegalArgumentException("Service [" + service + "] is a String " +
					"rather than an actual service reference: Have you accidentally specified " +
					"the service bean name as value instead of as reference?");
		}
		if (!serviceInterface.isInstance(service)) {
			throw new IllegalArgumentException("Service interface [" + serviceInterface.getName() +
					"] needs to be implemented by service [" + service + "] of class [" +
					service.getClass().getName() + "]");
		}
	}
	
	protected Object getProxyForService() {
		checkService();
		checkServiceInterface();
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.addInterface(getServiceInterface());
		//proxyFactory添加拦截器来增强代理对象的一些基本方法,用来执行target的方法
		if (this.registerTraceInterceptor != null ?
				this.registerTraceInterceptor.booleanValue() : this.interceptors == null) {
			proxyFactory.addAdvice(new RemoteInvocationTraceInterceptor(getExporterName()));
		}
		//targetSource的添加不可忽略,由反射性质所决定
		proxyFactory.setTarget(getService());
		return proxyFactory.getProxy(getBeanClassLoader());
	}

	protected String getExporterName() {
		return ClassUtils.getShortName(getClass());
	}

}
