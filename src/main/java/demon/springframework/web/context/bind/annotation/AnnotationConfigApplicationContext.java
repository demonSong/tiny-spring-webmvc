package demon.springframework.web.context.bind.annotation;

import demon.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import demon.springframework.context.support.GenericApplicationContext;

public class AnnotationConfigApplicationContext extends GenericApplicationContext{
	
	//封装了 两个reader 用来读取annotation数据,一种是通过文件进行扫描,一种是基于反射来读取annotation
	private final ClassPathBeanDefinitionScanner scanner =new ClassPathBeanDefinitionScanner(this);

	public AnnotationConfigApplicationContext() {
	}
	
	public AnnotationConfigApplicationContext(String... basePackages){
		scan(basePackages);
	}
	
	public void scan(String... basePackages) {
		this.scanner.scan(basePackages);
	}
}
