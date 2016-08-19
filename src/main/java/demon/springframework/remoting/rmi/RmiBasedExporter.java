package demon.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;

import demon.springframework.remoting.support.RemoteInvocation;
import demon.springframework.remoting.support.RemoteInvocationBasedExporter;

public class RmiBasedExporter extends RemoteInvocationBasedExporter{
	
	protected Remote getObjectToExport() {
		//可以使用原生remote service
		if (getService() instanceof Remote &&
				(getServiceInterface() == null || Remote.class.isAssignableFrom(getServiceInterface()))) {
			// conventional RMI service
			return (Remote) getService();
		}
		else {
			// RMI invoker
			return new RmiInvocationWrapper(getProxyForService(), this);
		}
		
	}
	
	@Override
	protected Object invoke(RemoteInvocation invocation, Object targetObject)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		return super.invoke(invocation, targetObject);
	}

}
