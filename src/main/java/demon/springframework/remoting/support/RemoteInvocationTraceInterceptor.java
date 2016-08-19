package demon.springframework.remoting.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class RemoteInvocationTraceInterceptor implements MethodInterceptor {

	private final String exporterNameClause;
	
	public RemoteInvocationTraceInterceptor() {
		this.exporterNameClause = "";
	}

	public RemoteInvocationTraceInterceptor(String exporterName) {
		this.exporterNameClause = exporterName + " ";
	}
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			Object retVal = invocation.proceed();
			return retVal;
		}
		catch (Throwable ex) {
			throw ex;
		}
	}

}
