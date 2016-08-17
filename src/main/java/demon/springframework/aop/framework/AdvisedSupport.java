package demon.springframework.aop.framework;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.util.Assert;

import demon.springframework.aop.Advisor;
import demon.springframework.aop.support.DefaultPointcutAdvisor;

public class AdvisedSupport implements Advised{
	
	private List<Class<?>> interfaces =new ArrayList<Class<?>>();
	
	private List<Advisor> advisors =new LinkedList<Advisor>();
	
	public void addInterface(Class<?> intf) {
		Assert.notNull(intf, "Interface must not be null");
		if (!intf.isInterface()) {
			throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
		}
		if (!this.interfaces.contains(intf)) {
			this.interfaces.add(intf);
		}
	}
	
	@Override
	public void addAdvice(Advice advice) throws AopConfigException {
		int pos = this.advisors.size();
		addAdvice(pos, advice);
	}
	
	@Override
	public void addAdvice(int pos, Advice advice) throws AopConfigException {
		Assert.notNull(advice, "Advice must not be null");
		addAdvisor(pos,new DefaultPointcutAdvisor());
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
	}

}
