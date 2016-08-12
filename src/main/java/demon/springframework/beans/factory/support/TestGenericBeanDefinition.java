package demon.springframework.beans.factory.support;

import demon.springframework.beans.DmnBeanDefinition;

public class TestGenericBeanDefinition extends AbstractBeanDefinition{
	
	private String parentName;
	
	public TestGenericBeanDefinition() {
		super();
	}
	
	public TestGenericBeanDefinition(DmnBeanDefinition original) {
		super(original);
	}
	
	@Override
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	@Override
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

	@Override
	public AbstractBeanDefinition cloneBeanDefinition() {
		return new TestGenericBeanDefinition(this);
	}
}
