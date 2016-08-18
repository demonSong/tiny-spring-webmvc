package demon.springframework.aop.framework;

import org.springframework.aop.framework.AopConfigException;

public interface AopProxyFactory {
	
	AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException;

}
