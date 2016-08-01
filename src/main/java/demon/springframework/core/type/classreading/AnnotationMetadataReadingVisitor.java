package demon.springframework.core.type.classreading;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;
import org.springframework.util.CollectionUtils;

import demon.springframework.core.type.AnnotationMetadata;
import demon.springframework.core.type.MethodMetadata;
import demon.springframework.util.LinkedMultiValueMap;
import demon.springframework.util.MultiValueMap;

final class AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor implements AnnotationMetadata{
	
	private final ClassLoader classLoader;
	
	private final Set<String> annotationSet =new LinkedHashSet<String>();
	
	private final Map<String, Set<String>> metaAnnotationMap = new LinkedHashMap<String, Set<String>>(4);

	private final Map<String, Map<String, Object>> attributeMap = new LinkedHashMap<String, Map<String, Object>>(4);
	
	private final MultiValueMap<String, MethodMetadata> methodMetadataMap = new LinkedMultiValueMap<String, MethodMetadata>();

	
	public AnnotationMetadataReadingVisitor(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	//重写两个父类方法,继续返回新的访问者
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return new MethodMetadataReadingVisitor(name, access, this.getClassName(), this.classLoader, this.methodMetadataMap);
	}

	@Override
	public AnnotationVisitor visitAnnotation(final String desc, boolean visible) {
		String className = Type.getType(desc).getClassName();
		this.annotationSet.add(className);
		return new AnnotationAttributesReadingVisitor(className, this.attributeMap, this.metaAnnotationMap, this.classLoader);
	}
	
	
	public Set<String> getAnnotationTypes() {
		return this.annotationSet;
	}

	public Set<String> getMetaAnnotationTypes(String annotationType) {
		return this.metaAnnotationMap.get(annotationType);
	}

	public boolean hasAnnotation(String annotationType) {
		return this.annotationSet.contains(annotationType);
	}

	public boolean hasMetaAnnotation(String metaAnnotationType) {
		Collection<Set<String>> allMetaTypes = this.metaAnnotationMap.values();
		for (Set<String> metaTypes : allMetaTypes) {
			if (metaTypes.contains(metaAnnotationType)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAnnotated(String annotationType) {
		return this.attributeMap.containsKey(annotationType);
	}

	public Map<String, Object> getAnnotationAttributes(String annotationType) {
		return getAnnotationAttributes(annotationType, false);
	}

	public Map<String, Object> getAnnotationAttributes(String annotationType,
			boolean classValuesAsString) {
		Map<String, Object> raw = this.attributeMap.get(annotationType);
		if (raw == null) {
			return null;
		}
		Map<String, Object> result = new LinkedHashMap<String, Object>(raw.size());
		for (Map.Entry<String, Object> entry : raw.entrySet()) {
			try {
				Object value = entry.getValue();
				if (value instanceof Type) {
					value = (classValuesAsString ? ((Type) value).getClassName() :
							this.classLoader.loadClass(((Type) value).getClassName()));
				}
				else if (value instanceof Type[]) {
					Type[] array = (Type[]) value;
					Object[] convArray = (classValuesAsString ? new String[array.length] : new Class[array.length]);
					for (int i = 0; i < array.length; i++) {
						convArray[i] = (classValuesAsString ? array[i].getClassName() :
								this.classLoader.loadClass(array[i].getClassName()));
					}
					value = convArray;
				}
				result.put(entry.getKey(), value);
			}
			catch (Exception ex) {
				// Class not found - can't resolve class reference in annotation attribute.
			}
		}
		return result;
	}

	public boolean hasAnnotatedMethods(String annotationType) {
		return this.methodMetadataMap.containsKey(annotationType);
	}

	public Set<MethodMetadata> getAnnotatedMethods(String annotationType) {
		List<MethodMetadata> list = this.methodMetadataMap.get(annotationType);
		if (CollectionUtils.isEmpty(list)) {
			return new LinkedHashSet<MethodMetadata>(0);
		}
		Set<MethodMetadata> annotatedMethods = new LinkedHashSet<MethodMetadata>(list.size());
		annotatedMethods.addAll(list);
		return annotatedMethods;
	}

}
