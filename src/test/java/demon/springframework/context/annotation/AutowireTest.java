package demon.springframework.context.annotation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import demon.springframework.web.test.HelloServiceImpl;
import demon.springframework.web.test.SpringService;

@Component
public class AutowireTest {

	@Autowired
	private SpringService springService;

	@Test
	public void test1() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		// 只能说明一种原因 test是在getBean时进行属性注入的,了解内部实现原理的重要性
		AutowireTest test = (AutowireTest) applicationContext
				.getBean("autowireTest");
		test.sayHello();
		// 当需要使用成员对象的时候,我们还是需要通过xml来配置一些简单的数据集
		test.getSpringService().sayName();
	}

	public SpringService getSpringService() {
		return this.springService;
	}

	public void sayHello() {
		springService.sayhai();
	}

	@Test
	public void test2() {
		try {
			demon.springframework.context.ClassPathXmlApplicationContext applicationContext = 
					new demon.springframework.context.ClassPathXmlApplicationContext(
					"applicationContext.xml");
			HelloServiceImpl helloServiceImpl =(HelloServiceImpl) applicationContext.getBean("helloServiceImpl");
			helloServiceImpl.sayHello();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
