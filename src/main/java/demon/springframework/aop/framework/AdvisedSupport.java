package demon.springframework.aop.framework;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.util.Assert;

import demon.springframework.aop.Advisor;
import demon.springframework.aop.support.DefaultPointcutAdvisor;

public class AdvisedSupport implements Advised{
	
	private List<Class<?>> interfaces =new ArrayList<Class<?>>();
	
	private List<Advisor> advisors =new LinkedList<Advisor>();
	
	AdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();
	
	private Advisor[] advisorArray = new Advisor[0];
	
	private transient Map<MethodCacheKey, List<Object>> methodCache;
	
	
	
	public AdvisedSupport() {
		initMethodCache();
	}
	
	private void initMethodCache() {
		this.methodCache = new ConcurrentHashMap<MethodCacheKey, List<Object>>(32);
	}
	
	protected void adviceChanged() {
		this.methodCache.clear();
	}
	
	public void addInterface(Class<?> intf) {
		Assert.notNull(intf, "Interface must not be null");
		if (!intf.isInterface()) {
			throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
		}
		if (!this.interfaces.contains(intf)) {
			this.interfaces.add(intf);
		}
	}
	
	public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
		MethodCacheKey cacheKey = new MethodCacheKey(method);
		List<Object> cached = this.methodCache.get(cacheKey);
		if (cached == null) {
			cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(
					this, method, targetClass);
			this.methodCache.put(cacheKey, cached);
		}
		return cached;
	}
	
	@Override
	public boolean isInterfaceProxied(Class<?> intf) {
		for (Class<?> proxyIntf : this.interfaces) {
			if (intf.isAssignableFrom(proxyIntf)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void addAdvice(Advice advice) throws AopConfigException {
		int pos = this.advisors.size();
		addAdvice(pos, advice);
	}
	
	@Override
	public void addAdvice(int pos, Advice advice) throws AopConfigException {
		Assert.notNull(advice, "Advice must not be null");
		addAdvisor(pos,new DefaultPointcutAdvisor(advice));
	}
	
	@Override
	public void addAdvisor(Advisor advisor) {
		int pos = this.advisors.size();
		addAdvisor(pos, advisor);
	}
	
	@Override
	public void addAdvisor(int pos, Advisor advisor) throws AopConfigException {
		addAdvisorInternal(pos, advisor);
	}
	
	private void addAdvisorInternal(int pos, Advisor advisor) throws AopConfigException {
		Assert.notNull(advisor, "Advisor must not be null");
		if (pos > this.advisors.size()) {
			throw new IllegalArgumentException(
					"Illegal position " + pos + " in advisor list with size " + this.advisors.size());
		}
		this.advisors.add(pos, advisor);
		updateAdvisorArray();
		adviceChanged();
	}
	
	protected final void updateAdvisorArray() {
		this.advisorArray = this.advisors.toArray(new Advisor[this.advisors.size()]);
	}
	
	@Override
	public Class<?>[] getProxiedInterfaces() {
		return this.interfaces.toArray(new Class<?>[this.interfaces.size()]);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getName());
		sb.append(this.advisors.size()).append(" advisors ");
		sb.append(this.advisors).append("; ");
		sb.append(super.toString());
		return sb.toString();
	}
	
	private static class MethodCacheKey {

		private final Method method;

		private final int hashCode;

		public MethodCacheKey(Method method) {
			this.method = method;
			this.hashCode = method.hashCode();
		}

		@Override
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			MethodCacheKey otherKey = (MethodCacheKey) other;
			return (this.method == otherKey.method);
		}

		@Override
		public int hashCode() {
			return this.hashCode;
		}
	}

	@Override
	public final Advisor[] getAdvisors() {
		return this.advisorArray;
	}

}
