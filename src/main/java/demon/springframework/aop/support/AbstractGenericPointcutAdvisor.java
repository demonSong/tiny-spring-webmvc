package demon.springframework.aop.support;

import org.aopalliance.aop.Advice;

@SuppressWarnings("serial")
public abstract class AbstractGenericPointcutAdvisor extends AbstractPointcutAdvisor{
	
	private Advice advice;
	
	public void setAdvice(Advice advice) {
		this.advice = advice;
	}
	
	@Override
	public Advice getAdvice() {
		return this.advice;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + ": advice [" + getAdvice() + "]";
	}

}
