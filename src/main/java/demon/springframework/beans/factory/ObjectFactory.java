package demon.springframework.beans.factory;

import org.springframework.beans.BeansException;

public interface ObjectFactory<T> {
	
	T getObject() throws BeansException;

}
