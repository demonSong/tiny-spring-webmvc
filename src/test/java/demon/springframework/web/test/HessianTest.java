package demon.springframework.web.test;

import org.junit.Test;

import demon.springframework.context.ClassPathXmlApplicationContext;
import demon.test.service.HelloWorldService;

public class HessianTest {
	
	@Test
	public void test(){
		try {
			ClassPathXmlApplicationContext applicationContext =new ClassPathXmlApplicationContext("hessian.xml");
			HelloWorldService helloWorldService=(HelloWorldService) applicationContext.getBean("hessianHelloWorldService");
			helloWorldService.sayHello("hessian--->tiny-spring-webmvc");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
