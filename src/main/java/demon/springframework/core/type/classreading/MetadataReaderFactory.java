package demon.springframework.core.type.classreading;

import java.io.IOException;

import demon.springframework.beans.io.Resource;

public interface MetadataReaderFactory {
	
	MetadataReader getMetadataReader(String className) throws IOException;

	MetadataReader getMetadataReader(Resource resource) throws IOException;
}
