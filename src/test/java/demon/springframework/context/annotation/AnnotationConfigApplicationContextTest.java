package demon.springframework.context.annotation;

import org.junit.Test;

import demon.springframework.web.context.bind.annotation.AnnotationConfigApplicationContext;
import demon.springframework.web.test.AddController;
import demon.springframework.web.test.HelloServiceImpl;

public class AnnotationConfigApplicationContextTest {
	
	String basePackage = "demon.springframework.**.test";
	
	@Test
	public void test1()throws Exception {
		AnnotationConfigApplicationContext annotationConfigApplicationContext =new AnnotationConfigApplicationContext(basePackage);
		HelloServiceImpl helloServiceImpl=(HelloServiceImpl) annotationConfigApplicationContext.getBean("helloServiceImpl");
		helloServiceImpl.sayHello();
	}
	
	@Test
	public void test2()throws Exception {
		AnnotationConfigApplicationContext annotationConfigApplicationContext =new AnnotationConfigApplicationContext(basePackage);
		AddController addController=(AddController) annotationConfigApplicationContext.getBean("addController");
		addController.handleRequest(null, null);
	}

}
