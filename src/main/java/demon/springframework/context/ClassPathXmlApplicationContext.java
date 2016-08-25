package demon.springframework.context;

import java.util.Map;

import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.factory.AbstractBeanFactory;
import demon.springframework.beans.factory.AutowireCapableBeanFactory;
import demon.springframework.beans.factory.support.BeanDefinitionRegistry;
import demon.springframework.beans.factory.support.DefaultListableBeanFactory;
import demon.springframework.beans.io.ResourceLoader;
import demon.springframework.beans.xml.XmlBeanDefinitionReader;
import demon.springframework.context.support.AbstractApplicationContext;

/**
 * @author yihua.huang@dianping.com
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

	private String configLocation;

	public ClassPathXmlApplicationContext(String configLocation) throws Exception {
		this(configLocation, new DefaultListableBeanFactory());
	}

	public ClassPathXmlApplicationContext(String configLocation, AbstractBeanFactory beanFactory) throws Exception {
		super(beanFactory);
		this.configLocation = configLocation;
		refresh();
	}

	//是否可以用处理器来完成对bean xml文件解析处理的功能
	@Override
	protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception {
		//做了xml Reader分发,并且完成注册功能
		XmlBeanDefinitionReader xmlBeanDefinitionReader;
		if(beanFactory instanceof AutowireCapableBeanFactory){
			BeanDefinitionRegistry registry =(BeanDefinitionRegistry) beanFactory;
			xmlBeanDefinitionReader= new XmlBeanDefinitionReader(registry,new ResourceLoader());
		}
		else {
			xmlBeanDefinitionReader =new XmlBeanDefinitionReader(new ResourceLoader());
		}
		xmlBeanDefinitionReader.loadBeanDefinitions(configLocation);
		for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : xmlBeanDefinitionReader.getRegistry().entrySet()) {
			beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
		}
	}
}
