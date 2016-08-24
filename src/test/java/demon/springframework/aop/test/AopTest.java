package demon.springframework.aop.test;

import org.junit.Test;

import demon.springframework.aop.service.AopHelloService;
import demon.springframework.context.ApplicationContext;
import demon.springframework.context.ClassPathXmlApplicationContext;

public class AopTest {
	
	@Test
	public void testJdkAop(){
		try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("aop.xml");
			AopHelloService service = (AopHelloService) applicationContext.getBean("aopService");
			service.sayHello2Aop("demon");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
