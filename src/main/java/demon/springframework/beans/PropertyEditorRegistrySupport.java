package demon.springframework.beans;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.propertyeditors.CustomNumberEditor;

import demon.springframework.beans.propertyeditors.ClassEditor;

public class PropertyEditorRegistrySupport implements PropertyEditorRegistry{
	
	private Map<Class<?>, PropertyEditor> defaultEditors;
	
	public PropertyEditor getDefaultEditor(Class<?> requiredType){
		if(this.defaultEditors == null){
			createDefaultEditors();
		}
		return this.defaultEditors.get(requiredType);
	}

	private void createDefaultEditors() {
		this.defaultEditors = new HashMap<Class<?>, PropertyEditor>(64);
		
		this.defaultEditors.put(Class.class, new ClassEditor());
		this.defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, false));
		this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));
	}
}
