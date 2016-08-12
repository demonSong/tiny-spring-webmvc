package demon.springframework.beans;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

public class TestPropertyValue {
	
	private final String name;
	
	private final Object value;
	
	/**
	 * 封装的value值,不能直接使用,需要进行转化才能注入进bean中
	 */
	private boolean converted =false;
	
	private Object convertedValue;
	
	
	public TestPropertyValue(String name,Object value){
		this.name =name;
		this.value=value;
	}
	
	public TestPropertyValue(TestPropertyValue original){
		Assert.notNull(original, "Original must not be null");
		this.name =original.name;
		this.value =original.value;
		this.converted =original.converted;
		this.convertedValue=original.convertedValue;
	}
	
	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}
	
	public synchronized boolean isConverted() {
		return this.converted;
	}

	public synchronized void setConvertedValue(Object value) {
		this.converted = true;
		this.convertedValue = value;
	}
	
	public synchronized Object getConvertedValue() {
		return this.convertedValue;
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
	}
	
	@Override
	public String toString() {
		return "bean property '" + this.name + "'";
	}
}
