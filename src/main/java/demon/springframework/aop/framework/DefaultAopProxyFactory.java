package demon.springframework.aop.framework;

import java.io.Serializable;

import org.springframework.aop.framework.AopConfigException;

@SuppressWarnings("serial")
public class DefaultAopProxyFactory implements AopProxyFactory ,Serializable{
	
	@Override
	public AopProxy createAopProxy(AdvisedSupport config)
			throws AopConfigException {
		return new JdkDynamicAopProxy(config);
	}

}
