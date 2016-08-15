package demon.springframework.beans;

public interface BeanWrapper extends TypeConverter{
	
	Object getWrappedInstance();
	
	Class<?> getWrappedClass();

}
