package demon.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;

import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class ClassEditor extends PropertyEditorSupport{

	private final ClassLoader classLoader;
	
	public ClassEditor(){
		this(null);
	}
	
	public ClassEditor(ClassLoader classLoader){
		this.classLoader =(classLoader !=null ? classLoader :ClassUtils.getDefaultClassLoader());
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.hasText(text)) {
			setValue(ClassUtils.resolveClassName(text.trim(), this.classLoader));
		}
		else {
			setValue(null);
		}
	}
	
	@Override
	public String getAsText() {
		Class<?> clazz = (Class<?>) getValue();
		if (clazz != null) {
			return ClassUtils.getQualifiedName(clazz);
		}
		else {
			return "";
		}
	}
	
}
