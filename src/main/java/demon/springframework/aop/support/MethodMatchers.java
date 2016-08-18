package demon.springframework.aop.support;

import java.lang.reflect.Method;

import org.springframework.util.Assert;

import demon.springframework.aop.MethodMatcher;

public abstract class MethodMatchers {
	
	public static boolean matches(MethodMatcher mm, Method method, Class<?> targetClass, boolean hasIntroductions) {
		Assert.notNull(mm, "MethodMatcher must not be null");
		return mm.matches(method, targetClass);
	}

}
