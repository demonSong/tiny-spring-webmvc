package demon.springframework.aop;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.BeansException;

import demon.springframework.beans.BeanPostProcessor;
import demon.springframework.beans.factory.AbstractBeanFactory;
import demon.springframework.beans.factory.BeanFactory;

/**
 * @author yihua.huang@dianping.com
 */
public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {

	private AbstractBeanFactory beanFactory;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof AspectJExpressionPointcutAdvisor) {
			return bean;
		}
		if (bean instanceof MethodInterceptor) {
			return bean;
		}
		List<AspectJExpressionPointcutAdvisor> advisors;
		try {
			advisors = beanFactory
					.getBeansForType(AspectJExpressionPointcutAdvisor.class);
			for (AspectJExpressionPointcutAdvisor advisor : advisors) {
				if (advisor.getPointcut().getClassFilter().matches(bean.getClass())) {
	                ProxyFactory advisedSupport = new ProxyFactory();
					advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
					advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

					TargetSource targetSource = new TargetSource(bean, bean.getClass(), bean.getClass().getInterfaces());
					advisedSupport.setTargetSource(targetSource);

					return advisedSupport.getProxy();
				}
			}
			return bean;
		} catch (Exception e) {
			return bean;
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws Exception {
		this.beanFactory = (AbstractBeanFactory) beanFactory;
	}
}
