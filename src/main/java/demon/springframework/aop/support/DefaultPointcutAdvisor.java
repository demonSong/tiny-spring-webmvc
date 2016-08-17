package demon.springframework.aop.support;

import java.io.Serializable;

import demon.springframework.aop.Pointcut;

@SuppressWarnings("serial")
public class DefaultPointcutAdvisor extends AbstractGenericPointcutAdvisor implements Serializable{
	
	private Pointcut pointcut =Pointcut.TRUE;
	
	public DefaultPointcutAdvisor(){
	}
	
	public void setPointcut(Pointcut pointcut) {
		this.pointcut = (pointcut != null ? pointcut : Pointcut.TRUE);
	}

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + ": pointcut [" + getPointcut() + "]; advice [" + getAdvice() + "]";
	}
}
