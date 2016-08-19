package demon.springframework.remoting.support;

import org.aopalliance.intercept.MethodInvocation;

public class DefaultRemoteInvocationFactory implements RemoteInvocationFactory {

	@Override
	public RemoteInvocation createRemoteInvocation(
			MethodInvocation methodInvocation) {
		return new RemoteInvocation(methodInvocation);
	}

}
