package demon.springframework.context.support;

import demon.springframework.beans.BeanPostProcessor;
import demon.springframework.context.ApplicationContextAware;
import demon.springframework.context.ConfigurableApplicationContext;

class ApplicationContextAwareProcessor implements BeanPostProcessor{
	
	private final ConfigurableApplicationContext applicationContext;
	
	//初始化失败的原因,是构造函数中没有默认的无参构造方法,那怎么进行有参的反射初始化
	public ApplicationContextAwareProcessor(ConfigurableApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	//在对某些bean组件进行初始化时,需要采用一些系统自带的processor做处理
	@Override
	public Object postProcessBeforeInitialization(final Object bean, String beanName) throws Exception {
		//accesscontrolcontext的作用
		System.out.println("所以说,这是一个钩子,配置了这个处理器后,所有使用了applicationContextAware接口的"
				+ "bean都会，回调setApplicationcontext的方法");
		invokeAwareInterfaces(bean);
		return bean;
	}
	
	private void invokeAwareInterfaces(Object bean) {
		if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
		}
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
		return null;
	}

}
