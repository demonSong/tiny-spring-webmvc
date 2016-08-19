package demon.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import org.springframework.util.Assert;

import demon.springframework.remoting.support.RemoteInvocation;

public class RmiInvocationWrapper implements RmiInvocationHandler {
	
	private final Object wrappedObject;

	private final RmiBasedExporter rmiExporter;

	public RmiInvocationWrapper(Object wrappedObject, RmiBasedExporter rmiExporter) {
		Assert.notNull(wrappedObject, "Object to wrap is required");
		Assert.notNull(rmiExporter, "RMI exporter is required");
		this.wrappedObject = wrappedObject;
		this.rmiExporter = rmiExporter;
	}


	@Override
	public String getTargetInterfaceName() {
		Class<?> ifc = this.rmiExporter.getServiceInterface();
		return (ifc != null ? ifc.getName() : null);
	}
	

	@Override
	public Object invoke(RemoteInvocation invocation)
		throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		return this.rmiExporter.invoke(invocation, this.wrappedObject);
	}

}
