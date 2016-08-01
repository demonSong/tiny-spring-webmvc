package demon.springframework.context.annotation;

import org.junit.Test;

import demon.springframework.web.context.bind.annotation.AnnotationConfigApplicationContext;

public class ClassPathBeanDefinitionScannerTest {
	
	String basePackage = "demon.springframework.**.test";
	
	@Test
	public void test() throws Exception{
		ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner =new ClassPathBeanDefinitionScanner(new AnnotationConfigApplicationContext());
		classPathBeanDefinitionScanner.doScan(basePackage);
	}

}
