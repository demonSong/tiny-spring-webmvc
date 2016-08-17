package demon.springframework.aop;

import java.io.Serializable;
import java.lang.reflect.Method;

@SuppressWarnings("serial")
class TrueMethodMatcher implements MethodMatcher, Serializable {

	public static final TrueMethodMatcher INSTANCE = new TrueMethodMatcher();

	/**
	 * Enforce Singleton pattern.
	 */
	private TrueMethodMatcher() {
	}

	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return true;
	}

	private Object readResolve() {
		return INSTANCE;
	}

	@Override
	public String toString() {
		return "MethodMatcher.TRUE";
	}

}
