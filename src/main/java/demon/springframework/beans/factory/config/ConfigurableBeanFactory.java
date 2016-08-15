package demon.springframework.beans.factory.config;

public interface ConfigurableBeanFactory {

	String SCOPE_SINGLETON ="singleton";
	
	void setBeanClassLoader(ClassLoader beanClassLoader);
	
	ClassLoader getBeanClassLoader();
	
	void setBeanExpressionResolver(BeanExpressionResolver resolver);

	/**
	 * Return the resolution strategy for expressions in bean definition values.
	 * @since 3.0
	 */
	BeanExpressionResolver getBeanExpressionResolver();

	
}
