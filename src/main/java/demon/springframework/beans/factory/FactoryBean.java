package demon.springframework.beans.factory;

/**
 * FactoryBean 自身是一个bean
 * 它可以再次生产我们需要的bean
 * @author demon.song
 *
 * @param <T>
 */
public interface FactoryBean<T> {
	
	T getObject() throws Exception;
	
	Class<?> getObjectType();
	
	boolean isSingleton();
}
