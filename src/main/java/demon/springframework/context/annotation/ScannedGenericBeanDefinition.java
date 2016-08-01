package demon.springframework.context.annotation;

import org.springframework.util.Assert;

import demon.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import demon.springframework.beans.factory.support.GenericBeanDefinition;
import demon.springframework.core.type.AnnotationMetadata;
import demon.springframework.core.type.classreading.MetadataReader;

public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition{

	private final AnnotationMetadata metadata;
	
	public ScannedGenericBeanDefinition(MetadataReader metadataReader) {
		Assert.notNull(metadataReader, "MetadataReader must not be null");
		this.metadata = metadataReader.getAnnotationMetadata();
		setBeanClassName(this.metadata.getClassName());
	}

	public final AnnotationMetadata getMetadata() {
		return this.metadata;
	}

}
