package demon.springframework.web.test;

import org.junit.Test;

import demon.springframework.context.ClassPathXmlApplicationContext;
import demon.springframework.remoting.rmi.RmiProxyFactoryBean;
import demon.test.service.HelloWorldService;

public class RMITest {
	
	
	@Test
	public void test(){
		try {
			ClassPathXmlApplicationContext applicationContext =new ClassPathXmlApplicationContext("rmi.xml");
			HelloWorldService helloWorldService=(HelloWorldService) applicationContext.getBean("rmiHelloWorldService");
			helloWorldService.sayHello("rmi");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
 