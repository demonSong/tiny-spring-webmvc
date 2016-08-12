package demon.springframework.web.test;

import org.junit.Test;

import demon.springframework.context.ClassPathXmlApplicationContext;
import demon.springframework.remoting.rmi.RmiProxyFactoryBean;

public class RMITest {
	
	
	@Test
	public void test(){
		try {
			ClassPathXmlApplicationContext applicationContext =new ClassPathXmlApplicationContext("rmi.xml");
			RmiProxyFactoryBean proxy =(RmiProxyFactoryBean) applicationContext.getBean("rmiHelloWorldService");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
