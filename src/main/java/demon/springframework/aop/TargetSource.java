package demon.springframework.aop;

/**
 * 被代理的对象
 * @author yihua.huang@dianping.com
 */
public interface TargetSource {
	
	Class<?> getTargetClass();
	
	boolean isStatic();
	
	Object getTarget() throws Exception;
	
	void releaseTarget(Object target) throws Exception;

}
