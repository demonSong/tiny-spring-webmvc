package demon.springframework.aop;

import java.io.Serializable;

@SuppressWarnings("serial")
class TruePointcut implements Pointcut, Serializable{

	public static final TruePointcut INSTANCE =new TruePointcut();
	
	@Override
	public ClassFilter getClassFilter() {
		return ClassFilter.TRUE;
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return MethodMatcher.TRUE;
	}

	private Object readResolve(){
		return INSTANCE;
	}
	
	@Override
	public String toString() {
		return "Pointcut.TRUE";
	}
}
