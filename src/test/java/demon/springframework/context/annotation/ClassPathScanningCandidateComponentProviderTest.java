package demon.springframework.context.annotation;

import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;

public class ClassPathScanningCandidateComponentProviderTest {

	String basePackage = "demon.springframework.**.support";

	@Test
	public void test1() throws Exception {
		org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider = new org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider(
				false);
		Set<BeanDefinition> beanDefinitions = classPathScanningCandidateComponentProvider
				.findCandidateComponents(basePackage);
	}
	
	@Test
	public void test2() throws Exception {
		ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(
				false);
		Set<BeanDefinition> beanDefinitions = classPathScanningCandidateComponentProvider
				.findCandidateComponents(basePackage);
	}

}
