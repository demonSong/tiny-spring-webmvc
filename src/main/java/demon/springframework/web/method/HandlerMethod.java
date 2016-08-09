package demon.springframework.web.method;

import java.lang.reflect.Method;

import org.springframework.util.Assert;

import demon.springframework.beans.factory.BeanFactory;

public class HandlerMethod {
	
	private final Method method;
	
	private final Object bean;
	
	private final BeanFactory beanFactory;
	
	public HandlerMethod(Object bean, Method method) {
		Assert.notNull(bean, "Bean is required");
		Assert.notNull(method, "Method is required");
		this.beanFactory =null;
		this.bean = bean;
		this.method = method;
	}
	
	public HandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
		Assert.hasText(beanName, "Bean name is required");
		Assert.notNull(beanFactory, "BeanFactory is required");
		Assert.notNull(method, "Method is required");
		this.bean = beanName;
		this.beanFactory = beanFactory;
		this.method = method;
	}
	
	protected HandlerMethod(HandlerMethod handlerMethod){
		Assert.notNull(handlerMethod,"HandlerMethod is requeired");
		this.bean=handlerMethod.bean;
		this.beanFactory=handlerMethod.beanFactory;
		this.method=handlerMethod.method;
	}
	
	private HandlerMethod(HandlerMethod handlerMethod, Object handler) {
		Assert.notNull(handlerMethod, "HandlerMethod is required");
		Assert.notNull(handler, "Handler object is required");
		this.bean = handler;
		this.beanFactory = handlerMethod.beanFactory;
		this.method = handlerMethod.method;
	}
	
	public HandlerMethod createWithResolvedBean(){
		Object handler =this.bean;
		if(this.bean instanceof String){
			String beanName =(String) this.bean;
			try {
				handler =this.beanFactory.getBean(beanName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new HandlerMethod(this,handler);
	}
	
	public Method getMethod() {
		return this.method;
	}
	
	public Object getBean() {
		return this.bean;
	}
}
