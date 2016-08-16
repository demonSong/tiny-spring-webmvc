package demon.springframework.beans;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

import demon.springframework.beans.propertyeditors.ClassEditor;
import demon.springframework.beansn.PropertyEditorRegistry;

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
	}
}
