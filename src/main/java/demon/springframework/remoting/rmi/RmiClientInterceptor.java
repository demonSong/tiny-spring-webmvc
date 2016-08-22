package demon.springframework.remoting.rmi;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.RemoteInvocationFailureException;
import org.springframework.remoting.RemoteLookupFailureException;
import org.springframework.remoting.rmi.RmiClientInterceptorUtils;
import org.springframework.remoting.support.RemoteInvocationUtils;

public class RmiClientInterceptor extends RemoteInvocationBasedAccessor implements MethodInterceptor{
	
	private boolean lookupStubOnStartup = true;
	
	private boolean cacheStub = true;
	
	private boolean refreshStubOnConnectFailure = false;
	
	private Remote cachedStub;
	
	private final Object stubMonitor = new Object();
	
	public void setCacheStub(boolean cacheStub) {
		this.cacheStub = cacheStub;
	}
	
	public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
		this.lookupStubOnStartup = lookupStubOnStartup;
	}
	
	public void setRefreshStubOnConnectFailure(boolean refreshStubOnConnectFailure) {
		this.refreshStubOnConnectFailure = refreshStubOnConnectFailure;
	}
	
	@Override
	public void afterPropertiesSet(){
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
	
	protected Remote getStub() throws RemoteLookupFailureException {
		if (!this.cacheStub || (this.lookupStubOnStartup && !this.refreshStubOnConnectFailure)) {
			return (this.cachedStub != null ? this.cachedStub : lookupStub());
		}
		else {
			synchronized (this.stubMonitor) {
				if (this.cachedStub == null) {
					this.cachedStub = lookupStub();
				}
				return this.cachedStub;
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
		System.out.println("TEST: 方法回调");
		Remote stub = getStub();
		try {
			return doInvoke(invocation, stub);
		}
		catch (RemoteConnectFailureException ex) {
			return handleRemoteConnectFailure(invocation, ex);
		}
		catch (RemoteException ex) {
			if (isConnectFailure(ex)) {
				return handleRemoteConnectFailure(invocation, ex);
			}
			else {
				throw ex;
			}
		}
	}
	
	protected boolean isConnectFailure(RemoteException ex) {
		return RmiClientInterceptorUtils.isConnectFailure(ex);
	}
	
	private Object handleRemoteConnectFailure(MethodInvocation invocation, Exception ex) throws Throwable {
		if (this.refreshStubOnConnectFailure) {
			return refreshAndRetry(invocation);
		}
		else {
			throw ex;
		}
	}
	
	protected Object refreshAndRetry(MethodInvocation invocation) throws Throwable {
		Remote freshStub = null;
		synchronized (this.stubMonitor) {
			this.cachedStub = null;
			freshStub = lookupStub();
			if (this.cacheStub) {
				this.cachedStub = freshStub;
			}
		}
		return doInvoke(invocation, freshStub);
	}
	
	protected Object doInvoke(MethodInvocation invocation, Remote stub) throws Throwable {
		if (stub instanceof RmiInvocationHandler) {
			// RMI invoker
			try {
				return doInvoke(invocation, (RmiInvocationHandler) stub);
			}
			catch (RemoteException ex) {
				throw RmiClientInterceptorUtils.convertRmiAccessException(
					invocation.getMethod(), ex, isConnectFailure(ex), getServiceUrl());
			}
			catch (InvocationTargetException ex) {
				Throwable exToThrow = ex.getTargetException();
				RemoteInvocationUtils.fillInClientStackTraceIfPossible(exToThrow);
				throw exToThrow;
			}
			catch (Throwable ex) {
				throw new RemoteInvocationFailureException("Invocation of method [" + invocation.getMethod() +
						"] failed in RMI service [" + getServiceUrl() + "]", ex);
			}
		}
		else {
			// traditional RMI stub
			try {
				return RmiClientInterceptorUtils.invokeRemoteMethod(invocation, stub);
			}
			catch (InvocationTargetException ex) {
				Throwable targetEx = ex.getTargetException();
				if (targetEx instanceof RemoteException) {
					RemoteException rex = (RemoteException) targetEx;
					throw RmiClientInterceptorUtils.convertRmiAccessException(
							invocation.getMethod(), rex, isConnectFailure(rex), getServiceUrl());
				}
				else {
					throw targetEx;
				}
			}
		}
	}
	
	protected Object doInvoke(MethodInvocation methodInvocation, RmiInvocationHandler invocationHandler)
			throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

			if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
				return "RMI invoker proxy for service URL [" + getServiceUrl() + "]";
			}

			return invocationHandler.invoke(createRemoteInvocation(methodInvocation));
		}

}
