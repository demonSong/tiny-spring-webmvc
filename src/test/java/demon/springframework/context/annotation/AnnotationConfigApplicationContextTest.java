package demon.springframework.context.annotation;

import org.junit.Test;

import demon.springframework.web.context.bind.annotation.AnnotationConfigApplicationContext;
import demon.springframework.web.test.HelloServiceImpl;

public class AnnotationConfigApplicationContextTest {
	
	String basePackage = "demon.springframework.**.test";
	
	@Test
	public void test()throws Exception {
		AnnotationConfigApplicationContext annotationConfigApplicationContext =new AnnotationConfigApplicationContext(basePackage);
		HelloServiceImpl helloServiceImpl=(HelloServiceImpl) annotationConfigApplicationContext.getBean("helloServiceImpl");
		helloServiceImpl.sayHello();
	}

}
