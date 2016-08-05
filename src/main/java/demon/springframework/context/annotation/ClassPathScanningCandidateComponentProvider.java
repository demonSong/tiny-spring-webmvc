package demon.springframework.context.annotation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import demon.springframework.beans.io.Resource;
import demon.springframework.beans.io.support.PathMatchingResourcePatternResolver;
import demon.springframework.core.type.classreading.MetadataReader;
import demon.springframework.core.type.classreading.MetadataReaderFactory;
import demon.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import demon.springframework.core.type.filter.AnnotationTypeFilter;
import demon.springframework.core.type.filter.TypeFilter;
import demon.springframework.stereotype.Component;

public class ClassPathScanningCandidateComponentProvider {
	
	private static final String DEFAULT_RESOURCE_PATTERN="**/*.class";
	
	private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
	
	private PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();

	private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();

	public ClassPathScanningCandidateComponentProvider(boolean useDefaultFilters) {
		if (useDefaultFilters) {
			registerDefaultFilters();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void registerDefaultFilters() {
		//添加过滤器,为什么传入的是component.class 却能匹配到service 和 controller的注解
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
	
	public void resetFilters(boolean useDefaultFilters) {
		this.includeFilters.clear();
		if (useDefaultFilters) {
			registerDefaultFilters();
		}
	}
	
	
	//验证 自己注册的注解 是否通过 即需要自定义的注解和修饰的注解进行匹配的过程
	protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
		//设置过滤器的用处是什么?
		for (TypeFilter tf : this.includeFilters) {
			if (tf.match(metadataReader, this.metadataReaderFactory)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return (beanDefinition.getMetadata().isConcrete() && beanDefinition.getMetadata().isIndependent());
	}
	
	public Set<BeanDefinition> findCandidateComponents(String basePackage) {
		Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
		
		try {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ resolveBasePackage(basePackage) + "/" + this.resourcePattern;
			//知道文件路径，就需要初始化这些资源,根据class path查找所有匹配的class文件,已备下一步做解析
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					try {
						MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
						if(isCandidateComponent(metadataReader)){
							//beanDefinition 进行metadataReader的初始化操作
							ScannedGenericBeanDefinition sbd =new ScannedGenericBeanDefinition(metadataReader);
							if (isCandidateComponent(sbd)) {
								candidates.add(sbd);
							}
						}
					}
					catch (Throwable ex) {
						ex.printStackTrace();
					}
				}	
			}
		} 
		catch (IOException e) {
		}
		return candidates;
	}
	
	protected String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

}
