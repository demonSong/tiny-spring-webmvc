package demon.springframework.context.annotation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import demon.springframework.beans.io.Resource;
import demon.springframework.beans.io.support.PathMatchingResourcePatternResolver;

public class ClassPathScanningCandidateComponentProvider {
	
	private static final String DEFAULT_RESOURCE_PATTERN="**/*.class";
	
	private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
	
	private PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();

	public ClassPathScanningCandidateComponentProvider(boolean useDefaultFilters) {
		if (useDefaultFilters) {
			registerDefaultFilters();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void registerDefaultFilters() {
		//添加过滤器
		this.includeFilters.add(new AnnotationTypeFilter(Component.class));
		ClassLoader cl = ClassPathScanningCandidateComponentProvider.class.getClassLoader();
		try {
			this.includeFilters.add(new AnnotationTypeFilter(
					((Class<? extends Annotation>) cl.loadClass("javax.annotation.ManagedBean")), false));
		}
		catch (ClassNotFoundException ex) {
		}
		try {
			this.includeFilters.add(new AnnotationTypeFilter(
					((Class<? extends Annotation>) cl.loadClass("javax.inject.Named")), false));
		}
		catch (ClassNotFoundException ex) {
		}
	}
	
	public Set<BeanDefinition> findCandidateComponents(String basePackage) {
		Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
		
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ resolveBasePackage(basePackage) + "/" + this.resourcePattern;
			//知道文件路径，就需要初始化这些资源
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			
		} catch (IOException e) {
		}
		
		return candidates;
	}
	
	protected String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

}
