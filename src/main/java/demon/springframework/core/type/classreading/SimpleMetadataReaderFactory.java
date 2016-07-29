package demon.springframework.core.type.classreading;

import java.io.IOException;

import org.springframework.util.ClassUtils;

import demon.springframework.beans.io.DefaultResourceLoader;
import demon.springframework.beans.io.Resource;
import demon.springframework.beans.io.ResourceLoader;

public class SimpleMetadataReaderFactory  implements MetadataReaderFactory{
	
	private final ResourceLoader resourceLoader;

	public SimpleMetadataReaderFactory() {
		this.resourceLoader = new DefaultResourceLoader();
	}
	
	public SimpleMetadataReaderFactory(ResourceLoader resourceLoader) {
		this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
	}
	
	public SimpleMetadataReaderFactory(ClassLoader classLoader) {
		this.resourceLoader =
				(classLoader != null ? new DefaultResourceLoader(classLoader) : new DefaultResourceLoader());
	}
	
	public final ResourceLoader getResourceLoader() {
		return this.resourceLoader;
	}
	
	@Override
	public MetadataReader getMetadataReader(String className)
			throws IOException {
		String resourcePath = ResourceLoader.CLASSPATH_URL_PREFIX +
				ClassUtils.convertClassNameToResourcePath(className) + ClassUtils.CLASS_FILE_SUFFIX;
		return getMetadataReader(this.resourceLoader.getResource(resourcePath));
	}

	@Override
	public MetadataReader getMetadataReader(Resource resource)
			throws IOException {
		return new SimpleMetadataReader(resource, this.resourceLoader.getClassLoader());
	}

}
