package demon.springframework.core.type.classreading;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.MethodAdapter;
import org.springframework.asm.Opcodes;
import org.springframework.asm.Type;
import org.springframework.asm.commons.EmptyVisitor;

import demon.springframework.core.type.MethodMetadata;
import demon.springframework.util.MultiValueMap;

final class MethodMetadataReadingVisitor extends MethodAdapter implements MethodMetadata {
	
	private final String name;

	private final int access;
	
	private String declaringClassName;
	
	private final ClassLoader classLoader;
	
	private final MultiValueMap<String,MethodMetadata> methodMetadataMap;
	
	private final Map<String, Map<String, Object>> attributeMap = new LinkedHashMap<String, Map<String, Object>>(2);

	public MethodMetadataReadingVisitor(String name, int access, String declaringClassName, ClassLoader classLoader,
			MultiValueMap<String, MethodMetadata> methodMetadataMap) {
		super(new EmptyVisitor());
		this.name =name;
		this.access =access;
		this.declaringClassName=declaringClassName;
		this.classLoader =classLoader;
		this.methodMetadataMap=methodMetadataMap;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(final String desc, boolean visible) {
		String className = Type.getType(desc).getClassName();
		methodMetadataMap.add(className, this);
		return new AnnotationAttributesReadingVisitor(className, this.attributeMap, null, this.classLoader);
	}

	public String getMethodName() {
		return this.name;
	}

	@Override
	public String getDeclaringClassName() {
		return this.declaringClassName;
	}

	@Override
	public boolean isStatic() {
		return ((this.access & Opcodes.ACC_STATIC) != 0);
	}

	@Override
	public boolean isFinal() {
		return ((this.access & Opcodes.ACC_FINAL) != 0);
	}

	@Override
	public boolean isOverridable() {
		return (!isStatic() && !isFinal() && ((this.access & Opcodes.ACC_PRIVATE) == 0));
	}

	@Override
	public boolean isAnnotated(String annotationType) {
		return this.attributeMap.containsKey(annotationType);
	}

	@Override
	public Map<String, Object> getAnnotationAttributes(String annotationType) {
		return this.attributeMap.get(annotationType);
	}

}
