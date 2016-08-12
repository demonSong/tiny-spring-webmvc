package demon.springframework.beans.factory.config;

public interface ConfigurableBeanFactory {

	String SCOPE_SINGLETON ="singleton";
	
	void setBeanClassLoader(ClassLoader beanClassLoader);
	
	ClassLoader getBeanClassLoader();
	
}
