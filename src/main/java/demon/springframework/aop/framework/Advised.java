package demon.springframework.aop.framework;

import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.AopConfigException;

import demon.springframework.aop.Advisor;

public interface Advised {

	void addAdvisor(Advisor advisor) throws AopConfigException;

	void addAdvisor(int pos, Advisor advisor) throws AopConfigException;
	
	void addAdvice(Advice advice) throws AopConfigException;

	void addAdvice(int pos, Advice advice) throws AopConfigException;
	
	Class<?>[] getProxiedInterfaces();
	
	Advisor[] getAdvisors();
	
	boolean isInterfaceProxied(Class<?> intf);

}
