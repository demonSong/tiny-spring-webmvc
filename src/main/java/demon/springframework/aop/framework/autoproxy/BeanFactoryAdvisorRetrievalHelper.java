package demon.springframework.aop.framework.autoproxy;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.util.Assert;

import demon.springframework.aop.Advisor;
import demon.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class BeanFactoryAdvisorRetrievalHelper {
	
	private final ConfigurableListableBeanFactory beanFactory;
	
	private String[] cachedAdvisorBeanNames;

	public BeanFactoryAdvisorRetrievalHelper(ConfigurableListableBeanFactory beanFactory) {
		Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
		this.beanFactory = beanFactory;
	}
	
	//查询spring中存在的pointCut advisor
	public List<Advisor> findAdvisorBeans() {
		String[] advisorNames =null;
		synchronized (this) {
			advisorNames = this.cachedAdvisorBeanNames;
			if (advisorNames == null) {
				// Do not initialize FactoryBeans here: We need to leave all regular beans
				// uninitialized to let the auto-proxy creator apply to them!
				
				this.cachedAdvisorBeanNames = advisorNames;
			}
		}
		if (advisorNames.length == 0) {
			return new LinkedList<Advisor>();
		}
		List<Advisor> advisors = new LinkedList<Advisor>();
		return null;
	}
	
	protected boolean isEligibleBean(String beanName) {
		return true;
	}
	
}
