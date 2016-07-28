package demon.springframework.context.support;

import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.factory.AbstractBeanFactory;
import demon.springframework.beans.factory.support.BeanDefinitionRegistry;

//源码中使用了装饰器模式 来对abstractApplicationContext进行扩展
public class GenericApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry{
	
	//封装一个beanfactory方法的 用来loadbeanDefinition
	
	public GenericApplicationContext(){
		
	}
	
	public GenericApplicationContext(AbstractBeanFactory beanFactory) {
		super(beanFactory);
	}
	
	@Override
	protected void loadBeanDefinitions(AbstractBeanFactory beanFactory)
			throws Exception {
	}

	//下面的方法只需要做下封装即可
	@Override
	public void registerBeanDefinition(String beanName,
			BeanDefinition beanDefinition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeBeanDefinition(String beanName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BeanDefinition getBeanDefinition(String beanName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsBeanDefinition(String beanName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getBeanDefinitionNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBeanDefinitionCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isBeanNameInUse(String beanName) {
		// TODO Auto-generated method stub
		return false;
	}

}
