package demon.springframework.beans.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.BeanPostProcessor;

/**
 * @author yihua.huang@dianping.com
 */
public abstract class AbstractBeanFactory implements BeanFactory {

	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

	private final List<String> beanDefinitionNames = new ArrayList<String>();

	private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

	@Override
	public Object getBean(String name) throws Exception {
		BeanDefinition beanDefinition = beanDefinitionMap.get(name);
		if (beanDefinition == null) {
			throw new IllegalArgumentException("No bean named " + name + " is defined");
		}
		Object bean = beanDefinition.getBean();
		if (bean == null) {
			//在bean进行初始化时 会先创建bean
			bean = doCreateBean(beanDefinition);
			//添加bean的处理器,可以xml文件形式动态配置 or 可以在程序中就实现配置
            bean = initializeBean(bean, name);
            //bean获得了增加,其实这是一种静态代理模式
            //类似于缓存,在map中获得beandefinition中后,下次getbean直接可以在definition中取
            beanDefinition.setBean(bean);
		}
		return bean;
	}

	protected Object initializeBean(Object bean, String name) throws Exception {
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
		}

		// TODO:call initialize method
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
		}
        return bean;
	}

	protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
		return beanDefinition.getBeanClass().newInstance();
	}

	public void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws Exception {
		beanDefinitionMap.put(name, beanDefinition);
		beanDefinitionNames.add(name);
	}

	public void preInstantiateSingletons() throws Exception {
		for (Iterator it = this.beanDefinitionNames.iterator(); it.hasNext();) {
			String beanName = (String) it.next();
			getBean(beanName);
		}
	}

	protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
		Object bean = createBeanInstance(beanDefinition);
		beanDefinition.setBean(bean);
		applyPropertyValues(bean, beanDefinition);
		return bean;
	}

	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {

	}

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws Exception {
		this.beanPostProcessors.add(beanPostProcessor);
	}

	public List getBeansForType(Class type) throws Exception {
		List beans = new ArrayList<Object>();
		for (String beanDefinitionName : beanDefinitionNames) {
			if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
				beans.add(getBean(beanDefinitionName));
			}
		}
		return beans;
	}

}
