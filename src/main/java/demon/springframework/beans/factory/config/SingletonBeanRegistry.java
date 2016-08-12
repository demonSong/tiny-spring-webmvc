package demon.springframework.beans.factory.config;

public interface SingletonBeanRegistry {
	
	Object getSingleton(String beanName);

}
