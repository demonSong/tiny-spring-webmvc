package demon.springframework.core.type.filter;

import java.io.IOException;

import demon.springframework.core.type.classreading.MetadataReader;
import demon.springframework.core.type.classreading.MetadataReaderFactory;

public interface TypeFilter {
	
	boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException;

}
