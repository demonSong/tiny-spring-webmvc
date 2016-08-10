package demon.springframework.beans.factory.support;

import demon.springframework.beans.BeanDefinition;

public class GenericBeanDefinition extends BeanDefinition{
	
	private String parentName;
	
	public GenericBeanDefinition() {
		super();
	}
	
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public String getParentName() {
		return this.parentName;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Generic bean");
		if (this.parentName != null) {
			sb.append(" with parent '").append(this.parentName).append("'");
		}
		sb.append(": ").append(super.toString());
		return sb.toString();
	}
}
