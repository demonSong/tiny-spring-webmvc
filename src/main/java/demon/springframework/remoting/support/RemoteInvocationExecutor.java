package demon.springframework.remoting.support;

import java.lang.reflect.InvocationTargetException;

public interface RemoteInvocationExecutor {
	
	Object invoke(RemoteInvocation invocation, Object targetObject)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

}
