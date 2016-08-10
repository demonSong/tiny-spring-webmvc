package demon.springframework.beans.factory.support;

import demon.springframework.beans.DmnBeanDefinition;

public abstract class AbstractBeanDefinition implements DmnBeanDefinition{
	
	public static final String SCOPE_DEFAULT="";
	
	//支持beanClass,基类-beanclass可以作为class类型也可以作为string类型
	private volatile Object beanClass;
	
	private String scope =SCOPE_DEFAULT;
	
	private String description;
	
	protected AbstractBeanDefinition(){}
	
	protected AbstractBeanDefinition(DmnBeanDefinition original){
		setScope(original.getScope());
	}

	@Override
	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public String getScope() {
		return this.scope;
	}
	/**
	 * 默认isSingleton为True
	 */
	@Override
	public boolean isSingleton() {
		return SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getDescription() {
		return this.description;
	}
	
	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
	public Class<?> getBeanClass() {
		Object beanClassObject =this.beanClass;
		if (beanClassObject == null) {
			throw new IllegalStateException("No bean class specified on bean definition");
		}
		if (!(beanClassObject instanceof Class)) {
			throw new IllegalStateException(
					"Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
		}
		return (Class<?>) beanClassObject;
	}
	
	@Override
	public void setBeanClassName(String beanClassName) {
		this.beanClass =beanClassName;
	}
	
	@Override
	public String getBeanClassName() {
		Object beanClassObject =this.beanClass;
		if(beanClassObject instanceof Class){
			return ((Class<?>) beanClassObject).getName();
		}
		else{
			return (String) beanClassObject;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("class [");
		sb.append(getBeanClassName()).append("]");
		sb.append("; scope=").append(this.scope);
		return sb.toString();
	}
	
}
