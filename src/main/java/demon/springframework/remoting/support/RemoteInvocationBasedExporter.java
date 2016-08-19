package demon.springframework.remoting.support;

import java.lang.reflect.InvocationTargetException;

public abstract class RemoteInvocationBasedExporter extends RemoteExporter{
	
	private RemoteInvocationExecutor remoteInvocationExecutor = new DefaultRemoteInvocationExecutor();
	
	public void setRemoteInvocationExecutor(RemoteInvocationExecutor remoteInvocationExecutor) {
		this.remoteInvocationExecutor = remoteInvocationExecutor;
	}

	public RemoteInvocationExecutor getRemoteInvocationExecutor() {
		return this.remoteInvocationExecutor;
	}
	
	protected Object invoke(RemoteInvocation invocation, Object targetObject)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		try {
			return getRemoteInvocationExecutor().invoke(invocation, targetObject);
		}
		catch (NoSuchMethodException ex) {
			throw ex;
		}
		catch (IllegalAccessException ex) {
			throw ex;
		}
		catch (InvocationTargetException ex) {
			throw ex;
		}
	}

}
