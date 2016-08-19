package demon.springframework.aop.framework;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ReflectionUtils;

import demon.springframework.aop.ProxyMethodInvocation;


public class ReflectiveMethodInvocation implements ProxyMethodInvocation,Cloneable{
	
	protected final Object proxy;
	
	protected final Object target;

	protected final Method method;

	protected Object[] arguments;
	
	private final Class<?> targetClass;
	
	protected final List<?> interceptorsAndDynamicMethodMatchers;
	
	private int currentInterceptorIndex = -1;
	
	protected ReflectiveMethodInvocation(
			Object proxy, Object target, Method method, Object[] arguments,
			Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {

		this.proxy = proxy;
		this.target = target;
		this.targetClass = targetClass;
		this.method = BridgeMethodResolver.findBridgedMethod(method);
		this.arguments = arguments;
		this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
	}

	@Override
	public final Method getMethod() {
		return this.method;
	}

	@Override
	public Object[] getArguments() {
		return (this.arguments != null ? this.arguments : new Object[0]);
	}

	@Override
	public Object proceed() throws Throwable {
		if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
			return invokeJoinpoint();
		}
		Object interceptorOrInterceptionAdvice =
				this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
		return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
	}
	
	protected Object invokeJoinpoint() throws Throwable {
		ReflectionUtils.makeAccessible(this.method);
		return method.invoke(this.target, this.arguments);
	}

	@Override
	public Object getThis() {
		return null;
	}

	@Override
	public AccessibleObject getStaticPart() {
		return null;
	}

}
