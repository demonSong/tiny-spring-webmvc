package demon.springframework.beans;

import org.springframework.beans.BeansException;

public interface PropertyAccessor {
	
	void setPropertyValue(String propertyName, Object value) throws BeansException;
	
	void setPropertyValue(TestPropertyValue pv) throws BeansException;
	
	void setPropertyValues(TestPropertyValues pv) throws BeansException;
	
	void setPropertyValues(TestPropertyValues pvs, boolean ignoreUnknown, boolean ignoreInvalid)
			throws BeansException;

}
