package demon.springframework.aop.framework;

public interface AopProxy {
	
	Object getProxy();
	
	Object getProxy(ClassLoader classLoader);

}
