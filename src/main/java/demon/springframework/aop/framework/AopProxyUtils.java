package demon.springframework.aop.framework;

import org.springframework.aop.SpringProxy;

public abstract class AopProxyUtils {
	
	public static Class<?>[] completeProxiedInterfaces(AdvisedSupport advised) {
		Class<?>[] specifiedInterfaces = advised.getProxiedInterfaces();
		boolean addSpringProxy = !advised.isInterfaceProxied(SpringProxy.class);
		boolean addAdvised = !advised.isInterfaceProxied(Advised.class);
		int nonUserIfcCount = 0;
		if (addSpringProxy) {
			nonUserIfcCount++;
		}
		if (addAdvised) {
			nonUserIfcCount++;
		}
		Class<?>[] proxiedInterfaces = new Class<?>[specifiedInterfaces.length + nonUserIfcCount];
		System.arraycopy(specifiedInterfaces, 0, proxiedInterfaces, 0, specifiedInterfaces.length);
		if (addSpringProxy) {
			proxiedInterfaces[specifiedInterfaces.length] = SpringProxy.class;
		}
		if (addAdvised) {
			proxiedInterfaces[proxiedInterfaces.length - 1] = Advised.class;
		}
		return proxiedInterfaces;
	}

}
