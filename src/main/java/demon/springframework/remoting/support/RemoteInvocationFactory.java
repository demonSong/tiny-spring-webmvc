package demon.springframework.remoting.support;

import org.aopalliance.intercept.MethodInvocation;

public interface RemoteInvocationFactory {
	RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation);
}
