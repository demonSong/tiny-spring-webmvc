package demon.springframework.core.util;

import java.lang.reflect.Method;

public abstract class ReflectionUtils {
	
	public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf)
			throws IllegalArgumentException {
		
		Method[] methods =clazz.getDeclaredMethods();
		for(Method method :methods){
			if(mf !=null && !mf.matches(method)){
				continue;
			}
			try {
				mc.doWith(method);
			} catch (IllegalAccessException ex) {
				throw new IllegalStateException("Shouldn't be illegal to access method '" + method.getName()
						+ "': " + ex);
			}
		}
		if(clazz.getSuperclass() !=null){
			doWithMethods(clazz.getSuperclass(), mc, mf);
		}
		else if (clazz.isInterface()){
			for(Class<?> superIfc : clazz.getInterfaces()){
				doWithMethods(superIfc, mc, mf);
			}
		}
	}
	
	
	public interface MethodFilter{
		
		boolean matches(Method method);
	}
	
	public interface MethodCallback{
		
		void doWith(Method method) throws IllegalArgumentException,IllegalAccessException; 
	}

	public static MethodFilter USER_DECLARED_METHODS = new MethodFilter() {

		public boolean matches(Method method) {
			return (!method.isBridge() && method.getDeclaringClass() != Object.class);
		}
	};
}
