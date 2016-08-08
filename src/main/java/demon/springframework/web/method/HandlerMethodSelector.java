package demon.springframework.web.method;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import demon.springframework.core.util.ReflectionUtils;
import demon.springframework.core.util.ReflectionUtils.MethodFilter;

public abstract class HandlerMethodSelector {
	
	public static Set<Method> selectMethods(final Class<?> handlerType, final MethodFilter handlerMethodFilter) {
		final Set<Method> handlerMethods =new LinkedHashSet<Method>();
		Set<Class<?>> handlerTypes = new LinkedHashSet<Class<?>>();
		Class<?> specificHandlerType = null;
		//为何需要判断是否为代理对象
		//如果是代理对象,本身该对象这个类型就不进入handlerTypes了
		if (!Proxy.isProxyClass(handlerType)) {
			handlerTypes.add(handlerType);
			specificHandlerType = handlerType;
		}
		handlerTypes.addAll(Arrays.asList(handlerType.getInterfaces()));
		for (Class<?> currentHandlerType : handlerTypes) {
			final Class<?> targetClass = (specificHandlerType != null ? specificHandlerType : currentHandlerType);
			ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback() {
				@Override
				public void doWith(Method method) {
					Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
					Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
					if (handlerMethodFilter.matches(specificMethod) &&
							(bridgedMethod == specificMethod || !handlerMethodFilter.matches(bridgedMethod))) {
						handlerMethods.add(specificMethod);
					}
				}
			}, ReflectionUtils.USER_DECLARED_METHODS);
		}
		return handlerMethods;
	}

}
