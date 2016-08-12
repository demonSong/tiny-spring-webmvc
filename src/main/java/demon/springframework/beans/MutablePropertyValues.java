package demon.springframework.beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.util.StringUtils;


public class MutablePropertyValues implements TestPropertyValues {
	
	private final List<TestPropertyValue> propertyValueList;
	
	public MutablePropertyValues() {
		this.propertyValueList = new ArrayList<TestPropertyValue>(0);
	}
	
	public MutablePropertyValues(TestPropertyValues original) {
		if (original != null) {
			TestPropertyValue[] pvs = original.getPropertyValues();
			this.propertyValueList = new ArrayList<TestPropertyValue>(pvs.length);
			for (TestPropertyValue pv : pvs) {
				this.propertyValueList.add(new TestPropertyValue(pv));
			}
		}
		else {
			this.propertyValueList = new ArrayList<TestPropertyValue>(0);
		}
	}
	
	public List<TestPropertyValue> getPropertyValueList() {
		return this.propertyValueList;
	}
	
	public int size() {
		return this.propertyValueList.size();
	}
	
	public MutablePropertyValues addPropertyValues(TestPropertyValues other) {
		if (other != null) {
			TestPropertyValue[] pvs = other.getPropertyValues();
			for (TestPropertyValue pv : pvs) {
				addPropertyValue(new TestPropertyValue(pv));
			}
		}
		return this;
	}
	
	public MutablePropertyValues addPropertyValue(TestPropertyValue pv) {
		for (int i = 0; i < this.propertyValueList.size(); i++) {
			TestPropertyValue currentPv = this.propertyValueList.get(i);
			//如果属性名字相同,进行合并,暂且不考虑
			if (currentPv.getName().equals(pv.getName())) {
				return this;
			}
		}
		this.propertyValueList.add(pv);
		return this;
	}

	
	//-------------------------------
	@Override
	public TestPropertyValue[] getPropertyValues() {
		return this.propertyValueList.toArray(new TestPropertyValue[this.propertyValueList.size()]);
	}

	@Override
	public TestPropertyValue getPropertyValue(String propertyName) {
		return null;
	}

	@Override
	public TestPropertyValues changesSince(TestPropertyValues old) {
		return null;
	}

	@Override
	public boolean contains(String propertyName) {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public String toString() {
		TestPropertyValue[] pvs = getPropertyValues();
		StringBuilder sb = new StringBuilder("PropertyValues: length=").append(pvs.length);
		if (pvs.length > 0) {
			sb.append("; ").append(StringUtils.arrayToDelimitedString(pvs, "; "));
		}
		return sb.toString();
	}
	
}
