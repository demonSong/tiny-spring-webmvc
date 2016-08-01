package demon.springframework.core.type.classreading;


import demon.springframework.beans.io.Resource;
import demon.springframework.core.type.AnnotationMetadata;
import demon.springframework.core.type.ClassMetadata;

public interface MetadataReader {
	
	Resource getResource();

	ClassMetadata getClassMetadata();

	AnnotationMetadata getAnnotationMetadata();

}
