package demon.springframework.beans;

public interface BeanWrapper extends ConfigurablePropertyAccessor{
	
	Object getWrappedInstance();
	
	Class<?> getWrappedClass();

}
