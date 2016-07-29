package demon.springframework.core.type.classreading;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Attribute;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.commons.EmptyVisitor;
import org.springframework.util.ClassUtils;

import demon.springframework.core.type.ClassMetadata;

class ClassMetadataReadingVisitor implements ClassVisitor,ClassMetadata{
	
	private String className;
	
	private boolean isInterface;
	
	private boolean isAbstract;
	
	private boolean isFinal;
	
	private String enclosingClassName;
	
	private boolean independentInnerClass;
	
	private String superClassName;
	
	private String[] interfaces;

	@Override
	public void visit(int version, int access, String name, String signature,
			String supername, String[] interfaces) {
		this.className = ClassUtils.convertResourcePathToClassName(name);
		this.isInterface = ((access & Opcodes.ACC_INTERFACE) != 0);
		this.isAbstract = ((access & Opcodes.ACC_ABSTRACT) != 0);
		this.isFinal = ((access & Opcodes.ACC_FINAL) != 0);
		if (supername != null) {
			this.superClassName = ClassUtils.convertResourcePathToClassName(supername);
		}
		this.interfaces = new String[interfaces.length];
		for (int i = 0; i < interfaces.length; i++) {
			this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
		}
		
	}
	
	public void visitOuterClass(String owner, String name, String desc) {
		this.enclosingClassName = ClassUtils.convertResourcePathToClassName(owner);
	}
	
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		if (outerName != null && this.className.equals(ClassUtils.convertResourcePathToClassName(name))) {
			this.enclosingClassName = ClassUtils.convertResourcePathToClassName(outerName);
			this.independentInnerClass = ((access & Opcodes.ACC_STATIC) != 0);
		}
	}

	@Override
	public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
		return new EmptyVisitor();
	}

	@Override
	public void visitAttribute(Attribute arg0) {
	}

	@Override
	public void visitEnd() {
	}

	@Override
	public FieldVisitor visitField(int arg0, String arg1, String arg2,
			String arg3, Object arg4) {
		return new EmptyVisitor();
	}

	@Override
	public MethodVisitor visitMethod(int arg0, String arg1, String arg2,
			String arg3, String[] arg4) {
		return new EmptyVisitor();
	}

	@Override
	public void visitSource(String arg0, String arg1) {
	}

	
	//---------------------------classMetadata-interface------------------//
	public String getClassName() {
		return this.className;
	}

	public boolean isInterface() {
		return this.isInterface;
	}

	public boolean isAbstract() {
		return this.isAbstract;
	}

	public boolean isConcrete() {
		return !(this.isInterface || this.isAbstract);
	}

	public boolean isFinal() {
		return this.isFinal;
	}

	public boolean isIndependent() {
		return (this.enclosingClassName == null || this.independentInnerClass);
	}

	public boolean hasEnclosingClass() {
		return (this.enclosingClassName != null);
	}

	public String getEnclosingClassName() {
		return this.enclosingClassName;
	}

	public boolean hasSuperClass() {
		return (this.superClassName != null);
	}

	public String getSuperClassName() {
		return this.superClassName;
	}

	public String[] getInterfaceNames() {
		return this.interfaces;
	}

}
