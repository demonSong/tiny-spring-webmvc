package demon.springframework.core.type.filter;

import java.io.IOException;

import demon.springframework.core.type.ClassMetadata;
import demon.springframework.core.type.classreading.MetadataReader;
import demon.springframework.core.type.classreading.MetadataReaderFactory;


public abstract class AbstractTypeHierarchyTraversingFilter implements TypeFilter {
	
	private final boolean considerInherited;

	private final boolean considerInterfaces;


	protected AbstractTypeHierarchyTraversingFilter(boolean considerInherited, boolean considerInterfaces) {
		this.considerInherited = considerInherited;
		this.considerInterfaces = considerInterfaces;
	}
	
	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {
		if (matchSelf(metadataReader)) {
			return true;
		}
		
		ClassMetadata metadata = metadataReader.getClassMetadata();
		if (matchClassName(metadata.getClassName())) {
			return true;
		}
		if (!this.considerInherited) {
			return false;
		}
		if (metadata.hasSuperClass()) {
			// Optimization to avoid creating ClassReader for super class.
			Boolean superClassMatch = matchSuperClass(metadata.getSuperClassName());
			if (superClassMatch != null) {
				if (superClassMatch.booleanValue()) {
					return true;
				}
			}
			else {
				// Need to read super class to determine a match...
				if (match(metadata.getSuperClassName(), metadataReaderFactory)) {
					return true;
				}
			}
		}
		if (!this.considerInterfaces) {
			return false;
		}
		for (String ifc : metadata.getInterfaceNames()) {
			// Optimization to avoid creating ClassReader for super class
			Boolean interfaceMatch = matchInterface(ifc);
			if (interfaceMatch != null) {
				if (interfaceMatch.booleanValue()) {
					return true;
				}
			}
			else {
				// Need to read interface to determine a match...
				if (match(ifc, metadataReaderFactory)) {
					return true;
				}
			}
		}

		return false;
	}
	
	private boolean match(String className, MetadataReaderFactory metadataReaderFactory) throws IOException {
		return match(metadataReaderFactory.getMetadataReader(className), metadataReaderFactory);
	}
	
	protected boolean matchSelf(MetadataReader metadataReader) {
		return false;
	}
	
	protected boolean matchClassName(String className) {
		return false;
	}
	
	protected Boolean matchSuperClass(String superClassName) {
		return null;
	}

	protected Boolean matchInterface(String interfaceNames) {
		return null;
	}
}
