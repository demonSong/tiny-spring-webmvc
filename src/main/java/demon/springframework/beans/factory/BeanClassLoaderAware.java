package demon.springframework.beans.factory;

public interface BeanClassLoaderAware extends Aware{
	
	void setBeanClassLoader(ClassLoader classLoader);

}
