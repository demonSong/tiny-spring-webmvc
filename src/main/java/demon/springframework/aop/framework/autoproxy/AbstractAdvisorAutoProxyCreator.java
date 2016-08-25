package demon.springframework.aop.framework.autoproxy;

import java.util.List;

import org.springframework.beans.BeansException;

import demon.springframework.aop.Advisor;
import demon.springframework.aop.TargetSource;
import demon.springframework.beans.factory.BeanFactory;
import demon.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public abstract class AbstractAdvisorAutoProxyCreator extends AbstractAutoProxyCreator {

	private BeanFactoryAdvisorRetrievalHelper advisorRetrievalHelper;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		super.setBeanFactory(beanFactory);
		if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
			throw new IllegalStateException("Cannot use AdvisorAutoProxyCreator without a ConfigurableListableBeanFactory");
		}
		initBeanFactory((ConfigurableListableBeanFactory) beanFactory);
	}
	
	protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		this.advisorRetrievalHelper = new BeanFactoryAdvisorRetrievalHelperAdapter(beanFactory);
	}
	
	@Override
	protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass,
			String beanName, TargetSource customTargetSource)
			throws BeansException {
		List<Advisor> advisors = findEligibleAdvisors(beanClass, beanName);
		if(advisors.isEmpty()){
			return DO_NOT_PROXY;
		}
		return advisors.toArray();
	}
	
	protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
		List<Advisor> candidateAdvisors = findCandidateAdvisors();
		
		return null;
	}
	
	protected List<Advisor> findCandidateAdvisors() {
		return this.advisorRetrievalHelper.findAdvisorBeans();
	}
	
	protected boolean isEligibleAdvisorBean(String beanName) {
		return true;
	}
	
	private class BeanFactoryAdvisorRetrievalHelperAdapter extends BeanFactoryAdvisorRetrievalHelper {

		public BeanFactoryAdvisorRetrievalHelperAdapter(ConfigurableListableBeanFactory beanFactory) {
			super(beanFactory);
		}

		@Override
		protected boolean isEligibleBean(String beanName) {
			return AbstractAdvisorAutoProxyCreator.this.isEligibleAdvisorBean(beanName);
		}
	}

}
