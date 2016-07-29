package demon.springframework.core.type;

public interface ClassMetadata {
	
	String getClassName();
	
	boolean isInterface();
	
	boolean isAbstract();
	
	boolean isConcrete();
	
	boolean isFinal();
	
	boolean isIndependent();
	
	boolean hasEnclosingClass();
	
	String getEnclosingClassName();

	boolean hasSuperClass();
	
	String getSuperClassName();
	
	String[] getInterfaceNames();
}
