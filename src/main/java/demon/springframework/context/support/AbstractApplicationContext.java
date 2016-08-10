package demon.springframework.context.support;

import java.util.List;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.convert.ConversionService;

import demon.springframework.beans.BeanPostProcessor;
import demon.springframework.beans.factory.AbstractBeanFactory;
import demon.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import demon.springframework.context.ConfigurableApplicationContext;
import demon.springframework.context.WebApplicationContext;
import demon.springframework.web.context.ConfigurableWebApplicationContext;

/**
 * @author yihua.huang@dianping.com
 */
public abstract class AbstractApplicationContext implements WebApplicationContext,ConfigurableApplicationContext{
	protected AbstractBeanFactory beanFactory;
	
	public AbstractApplicationContext() {
		this(null);
	}

	public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public void refresh() throws Exception {
		//防止多个线程的进入
		//所以说LoadBeanDefintion的函数完全可以由处理器来完成替代
		loadBeanDefinitions(beanFactory);
		
		//这里只是完成了对BeanPostProcessor的注册,而没有执行调用方法
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
		//直接在代码中new出来,这样一个处理器
		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
		//源码使用一种工厂来产生符合条件的processor,我们直接new出来即可 
		//这里的条件便是 把processor也当作是一种bean,所以暂时把它放在xml文件变成bean processor吧
	}

	protected void onRefresh() throws Exception{
        beanFactory.preInstantiateSingletons();
    }

	@Override
	public Object getBean(String name) throws Exception {
		return beanFactory.getBean(name);
	}
	
	@Override
	public String[] getBeanNamesForType(Class<?> type) {
		return beanFactory.getBeanNamesForType(type);
	}
	
	@Override
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return beanFactory.getType(name);
	}
	
}
