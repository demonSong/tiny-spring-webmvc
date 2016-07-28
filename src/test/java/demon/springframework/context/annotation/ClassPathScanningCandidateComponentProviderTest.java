package demon.springframework.context.annotation;

import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;

public class ClassPathScanningCandidateComponentProviderTest {

	String basePackage="com.vip.**.repository";
	
	@Test
	public void test() throws Exception {
		ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider=new ClassPathScanningCandidateComponentProvider(false);
		
		Set<BeanDefinition> beanDefinitions=classPathScanningCandidateComponentProvider.findCandidateComponents(basePackage);
	}

}
