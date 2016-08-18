package demon.springframework.aop.framework;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;

import demon.springframework.aop.Advisor;
import demon.springframework.aop.MethodMatcher;
import demon.springframework.aop.PointcutAdvisor;
import demon.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import demon.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import demon.springframework.aop.support.MethodMatchers;

@SuppressWarnings("serial")
public class DefaultAdvisorChainFactory implements AdvisorChainFactory, Serializable{

	@Override
	public List<Object> getInterceptorsAndDynamicInterceptionAdvice(
			Advised config, Method method, Class<?> targetClass) {
		List<Object> interceptorList = new ArrayList<Object>(config.getAdvisors().length);
		Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());
		boolean hasIntroductions = hasMatchingIntroductions(config, actualClass);
		AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();

		for (Advisor advisor : config.getAdvisors()) {
			if (advisor instanceof PointcutAdvisor) {
				// Add it conditionally.
				PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
				//过滤掉不需要的class
				if (pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
					//通过registry来获得advisor所在的拦截器
					MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
					MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
					//过滤掉不需要的method
					if (MethodMatchers.matches(mm, method, actualClass, hasIntroductions)) {
						interceptorList.addAll(Arrays.asList(interceptors));
					}
				}
			}
		}
		return interceptorList;
	}
	
	private static boolean hasMatchingIntroductions(Advised config, Class<?> actualClass) {
		return false;
	}
}
