package demon.springframework.context.annotation;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.util.Assert;

import demon.springframework.beans.factory.support.BeanDefinitionRegistry;
import demon.springframework.beans.io.ResourceLoader;


public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{
	
	//bean definition registry 在这里的作用
	private final BeanDefinitionRegistry registry;
	
	private String[] autowireCandidatePatterns;
	
	//bean name gengrator
	
	//scopeMetadataResolver
	
	//includeAnnotationConfig
	private boolean includeAnnotationConfig = true;
	
	//使用默认的过滤器
	public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		this(registry, true);
	}
	
	public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
		super(useDefaultFilters);

		//断言的作用
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
		this.registry = registry;

		// Determine ResourceLoader to use.
		if (this.registry instanceof ResourceLoader) {
			//setResourceLoader((ResourceLoader) this.registry);
		}
	}
	
	public final BeanDefinitionRegistry getRegistry() {
		return this.registry;
	}
	
	public int scan(String... basePackages) {
		int beanCountAtScanStart = this.registry.getBeanDefinitionCount();

		doScan(basePackages);

		//什么鬼
		if (this.includeAnnotationConfig) {
			//AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
		}

		return this.registry.getBeanDefinitionCount() - beanCountAtScanStart;
	}
	
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
		for (String basePackage : basePackages) {
			Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
			for (BeanDefinition candidate : candidates) {
			}						
		}
		return beanDefinitions;
	}
	
}
