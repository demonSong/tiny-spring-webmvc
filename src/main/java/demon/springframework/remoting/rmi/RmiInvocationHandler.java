package demon.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import demon.springframework.remoting.support.RemoteInvocation;

public interface RmiInvocationHandler extends Remote {
	
	public String getTargetInterfaceName() throws RemoteException;
	
	public Object invoke(RemoteInvocation invocation)
			throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;

}
