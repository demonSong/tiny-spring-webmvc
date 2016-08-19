package demon.springframework.aop.framework;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import demon.springframework.aop.TargetSource;

@SuppressWarnings("serial")
final class JdkDynamicAopProxy implements AopProxy,InvocationHandler,Serializable{
	
	private final AdvisedSupport advised;
	
	private boolean equalsDefined;

	private boolean hashCodeDefined;
	
	public JdkDynamicAopProxy(AdvisedSupport config){
		Assert.notNull(config, "AdvisedSupport must not be null");
		this.advised = config;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("TEST:对方法进行增强处理");
		MethodInvocation invocation;
		
		TargetSource targetSource =this.advised.targetSource;
		Class<?> targetClass =null;
		Object target =null;
		
		target =targetSource.getTarget();
		if(target !=null){
			targetClass =target.getClass();
		}
		
		Object retVal;
		//在advised中做了类和方法的过滤,即方法需要执行的所有拦截器都会被过滤出来
		List<Object> chain =this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
		
		//获得方法的处理链,来对方法做N个增强处理,进行回调解耦
		invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass,  chain);
		retVal =invocation.proceed();
		
		return retVal;
	}

	@Override
	public Object getProxy() {
		return getProxy(ClassUtils.getDefaultClassLoader());
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised);
		findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
		return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
	}

	private void findDefinedEqualsAndHashCodeMethods(Class<?>[] proxiedInterfaces) {
		for (Class<?> proxiedInterface : proxiedInterfaces) {
			Method[] methods = proxiedInterface.getDeclaredMethods();
			for (Method method : methods) {
				if (AopUtils.isEqualsMethod(method)) {
					this.equalsDefined = true;
				}
				if (AopUtils.isHashCodeMethod(method)) {
					this.hashCodeDefined = true;
				}
				if (this.equalsDefined && this.hashCodeDefined) {
					return;
				}
			}
		}
	}
}
