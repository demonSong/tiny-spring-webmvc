package demon.springframework.beans;

import org.springframework.util.Assert;

public class TestPropertyValue {
	
	private final String name;
	
	private final Object value;
	
	public TestPropertyValue(String name,Object value){
		this.name =name;
		this.value=value;
	}
	
	public TestPropertyValue(TestPropertyValue original){
		Assert.notNull(original, "Original must not be null");
		this.name =original.name;
		this.value =original.value;
	}
	
	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return "bean property '" + this.name + "'";
	}
}
