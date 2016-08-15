package demon.springframework.beans.factory.config;

import org.springframework.util.Assert;

public class BeanExpressionContext {
	
	private final ConfigurableBeanFactory beanFactory;
	
	private final Scope scope;
	
	public BeanExpressionContext(ConfigurableBeanFactory beanFactory,Scope scope){
		Assert.notNull(beanFactory, "BeanFactory must not be null");
		this.beanFactory = beanFactory;
		this.scope = scope;
	}
	
	public final ConfigurableBeanFactory getBeanFactory() {
		return this.beanFactory;
	}
	
	public final Scope getScope() {
		return this.scope;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BeanExpressionContext)) {
			return false;
		}
		BeanExpressionContext otherContext = (BeanExpressionContext) other;
		return (this.beanFactory == otherContext.beanFactory && this.scope == otherContext.scope);
	}

	@Override
	public int hashCode() {
		return this.beanFactory.hashCode();
	}
	
}
