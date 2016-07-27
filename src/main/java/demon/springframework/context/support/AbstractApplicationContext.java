package demon.springframework.context.support;

import java.util.List;

import demon.springframework.beans.BeanPostProcessor;
import demon.springframework.beans.factory.AbstractBeanFactory;
import demon.springframework.context.ConfigurableApplicationContext;
import demon.springframework.context.WebApplicationContext;

/**
 * @author yihua.huang@dianping.com
 */
public abstract class AbstractApplicationContext implements WebApplicationContext,ConfigurableApplicationContext{
	protected AbstractBeanFactory beanFactory;

	public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public void refresh() throws Exception {
		loadBeanDefinitions(beanFactory);
		registerBeanPostProcessors(beanFactory);
		onRefresh();
	}

	protected abstract void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception;

	protected void registerBeanPostProcessors(AbstractBeanFactory beanFactory) throws Exception {
		//注册bean应该采取两种方式,通过xml方式进行注册,直接在代码中new
		List beanPostProcessors = beanFactory.getBeansForType(BeanPostProcessor.class);
		for (Object beanPostProcessor : beanPostProcessors) {
			beanFactory.addBeanPostProcessor((BeanPostProcessor) beanPostProcessor);
		}
		//直接在代码中new出来
	}

	protected void onRefresh() throws Exception{
        beanFactory.preInstantiateSingletons();
    }

	@Override
	public Object getBean(String name) throws Exception {
		return beanFactory.getBean(name);
	}
}
