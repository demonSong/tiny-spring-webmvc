package demon.springframework.aop.framework;

public class ProxyCreatorSupport extends AdvisedSupport{
	
	private AopProxyFactory aopProxyFactory;
	
	public ProxyCreatorSupport() {
		this.aopProxyFactory = new DefaultAopProxyFactory();
	}
	
	protected final synchronized AopProxy createAopProxy() {
		return getAopProxyFactory().createAopProxy(this);
	}
	
	public AopProxyFactory getAopProxyFactory() {
		return this.aopProxyFactory;
	}

}
