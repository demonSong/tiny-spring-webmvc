package demon.springframework.web.method.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.ReflectionUtils;

import demon.springframework.web.method.HandlerMethod;

public class InvocableHandlerMethod extends HandlerMethod{

	
	
	public InvocableHandlerMethod(Object bean, Method method) {
		super(bean, method);
	}
	
	public InvocableHandlerMethod(HandlerMethod handlerMethod) {
		super(handlerMethod);
	}
	
	public final Object invokeForRequest(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Object[]  args =new Object[2];
		args[0]=request;
		args[1]=response;
		Object returnValue =invoke(args);
		return returnValue;
	}
	
	private Object invoke(Object... args) throws Exception {
		ReflectionUtils.makeAccessible(getMethod());
		try {
			return getMethod().invoke(getBean(), args);
		}
		catch (IllegalArgumentException ex) {
			throw new IllegalStateException("发生异常");
		}
		catch (InvocationTargetException ex) {
			// Unwrap for HandlerExceptionResolvers ...
			Throwable targetException = ex.getTargetException();
			if (targetException instanceof RuntimeException) {
				throw (RuntimeException) targetException;
			}
			else if (targetException instanceof Error) {
				throw (Error) targetException;
			}
			else if (targetException instanceof Exception) {
				throw (Exception) targetException;
			}
			else {
				throw new IllegalStateException(targetException);
			}
		}
	}

}
