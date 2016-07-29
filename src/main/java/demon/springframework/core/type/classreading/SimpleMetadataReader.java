package demon.springframework.core.type.classreading;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.asm.ClassReader;

import demon.springframework.beans.io.Resource;
import demon.springframework.core.type.ClassMetadata;

/*
 * 封装了annoation读取接口
 * 典型的不变类,初始化后就不再改变
 * classReader用来根据resource来读取class信息 asm强大的字节码解析工具
 */
final class SimpleMetadataReader implements MetadataReader{
	
	private final Resource resource;
	private final ClassMetadata classMetadata;
	//private final AnnotationMetadata annotationMetadata;
	
	SimpleMetadataReader(Resource resource, ClassLoader classLoader) throws IOException {
		InputStream is = resource.getInputStream();
		ClassReader classReader = null;
		try {
			classReader = new ClassReader(is);
		} finally {
			is.close();
		}

		ClassMetadataReadingVisitor visitor = new ClassMetadataReadingVisitor();
		classReader.accept(visitor, true);
		
		//this.annotationMetadata = visitor;
		// (since AnnotationMetadataReader extends ClassMetadataReadingVisitor)
		this.classMetadata = visitor;
		this.resource = resource;
	}
	
	public Resource getResource() {
		return this.resource;
	}
	
	public ClassMetadata getClassMetadata() {
		return this.classMetadata;
	}

//	public AnnotationMetadata getAnnotationMetadata() {
//		return this.annotationMetadata;
//	}

}
