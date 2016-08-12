package demon.springframework.beans.factory.config;

import org.springframework.util.Assert;


public class RuntimeBeanReference implements TestBeanReference{

	private final String beanName;
	
	private final boolean toParent;
	
	private Object source;
	
	public RuntimeBeanReference(String beanName){
		this(beanName,false);
	}
	
	public RuntimeBeanReference(String beanName , boolean toParent) {
		Assert.hasText(beanName, "'beanName' must not be empty");
		this.beanName = beanName;
		this.toParent = toParent;
	}
	
	@Override
	public String getBeanName() {
		return this.beanName;
	}
	
	public boolean isToParent() {
		return this.toParent;
	}
	
	public void setSource(Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RuntimeBeanReference)) {
			return false;
		}
		RuntimeBeanReference that = (RuntimeBeanReference) other;
		return (this.beanName.equals(that.beanName) && this.toParent == that.toParent);
	}

	@Override
	public int hashCode() {
		int result = this.beanName.hashCode();
		result = 29 * result + (this.toParent ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return '<' + getBeanName() + '>';
	}
	
}
