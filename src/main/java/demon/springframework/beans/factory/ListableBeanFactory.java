package demon.springframework.beans.factory;

public interface ListableBeanFactory extends BeanFactory{
	
	String[] getBeanNamesForType(Class<?> type);
	
}
