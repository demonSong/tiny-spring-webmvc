package demon.springframework.beans.factory.config;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.springframework.util.Assert;


/**
 * 用来描述bean 和对应域的关系
 * @author demon.song
 *
 */
public class DependencyDescriptor implements Serializable{

	private transient Field field;
	
	private Class declaringClass;
	
	private String fieldName;
	
	private final boolean required;
	
	private final boolean eager;
	
	public DependencyDescriptor(Field field, boolean required){
		this(field, required, true);
	}
	
	public DependencyDescriptor(Field field, boolean required, boolean eager){
		Assert.notNull(field, "Field must not be null");
		this.field = field;
		this.declaringClass = field.getDeclaringClass();
		this.fieldName = field.getName();
		this.required = required;
		this.eager = eager;
	}
	
	public boolean isEager() {
		return this.eager;
	}
	
	/**
	 * 目前只支持field类型
	 */
	public Class<?> getDependencyType() {
		return (this.field != null ? this.field.getType() : null);
	}
}
