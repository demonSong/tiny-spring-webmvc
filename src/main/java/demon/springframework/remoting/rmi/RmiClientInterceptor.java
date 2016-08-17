package demon.springframework.remoting.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.RemoteLookupFailureException;

import demon.springframework.remoting.support.UrlBasedRemoteAccessor;

public class RmiClientInterceptor extends UrlBasedRemoteAccessor implements MethodInterceptor{
	
	private boolean lookupStubOnStartup = true;
	
	private boolean cacheStub = true;
	
	private Remote cachedStub;
	
	public void setCacheStub(boolean cacheStub) {
		this.cacheStub = cacheStub;
	}
	
	public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
		this.lookupStubOnStartup = lookupStubOnStartup;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		prepare();
	}
	
	public void prepare() throws RemoteLookupFailureException {
		if(this.lookupStubOnStartup){
			Remote remoteObj =lookupStub();
			if (this.cacheStub) {
				this.cachedStub = remoteObj;
			}
		}
	}
	
	protected Remote lookupStub() throws RemoteLookupFailureException {
		try {
			Remote stub =null;
			stub =Naming.lookup(getServiceUrl());
			return stub;
		}
		catch (MalformedURLException ex) {
			throw new RemoteLookupFailureException("Service URL [" + getServiceUrl() + "] is invalid", ex);
		}
		catch (NotBoundException ex) {
			throw new RemoteLookupFailureException(
					"Could not find RMI service [" + getServiceUrl() + "] in RMI registry", ex);
		}
		catch (RemoteException ex) {
			throw new RemoteLookupFailureException("Lookup of RMI stub failed", ex);
		}
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		return null;
	}

}
