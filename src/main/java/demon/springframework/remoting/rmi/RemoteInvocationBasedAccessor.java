package demon.springframework.remoting.rmi;

import org.aopalliance.intercept.MethodInvocation;

import demon.springframework.remoting.support.DefaultRemoteInvocationFactory;
import demon.springframework.remoting.support.RemoteInvocation;
import demon.springframework.remoting.support.RemoteInvocationFactory;
import demon.springframework.remoting.support.UrlBasedRemoteAccessor;

public abstract class RemoteInvocationBasedAccessor extends UrlBasedRemoteAccessor {
	
	private RemoteInvocationFactory remoteInvocationFactory = new DefaultRemoteInvocationFactory();

	
	public RemoteInvocationFactory getRemoteInvocationFactory() {
		return this.remoteInvocationFactory;
	}
	
	protected RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
		return getRemoteInvocationFactory().createRemoteInvocation(methodInvocation);
	}

}
