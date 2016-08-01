package demon.springframework.context.annotation;

import java.util.Set;

import org.junit.Test;

import demon.springframework.beans.BeanDefinition;

public class ClassPathScanningCandidateComponentProviderTest {

	String basePackage = "demon.springframework.**.test";

	@Test
	public void test1() throws Exception {
		//直接findcandidatecomponents是不会得到任何过滤器的
		org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider = new org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider(
				false);
		classPathScanningCandidateComponentProvider.resetFilters(true);
		Set<org.springframework.beans.factory.config.BeanDefinition> beanDefinitions = classPathScanningCandidateComponentProvider
				.findCandidateComponents(basePackage);
	}
	
	@Test
	public void test2() throws Exception {
		ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(
				false);
		classPathScanningCandidateComponentProvider.resetFilters(true);
		Set<BeanDefinition> beanDefinitions = classPathScanningCandidateComponentProvider
				.findCandidateComponents(basePackage);
	}

}
