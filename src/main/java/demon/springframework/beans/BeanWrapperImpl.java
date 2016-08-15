package demon.springframework.beans;

import java.beans.PropertyDescriptor;

import org.springframework.beans.TypeMismatchException;
import org.springframework.util.Assert;

import demon.springframework.core.convert.Property;
import demon.springframework.core.convert.TypeDescriptor;

public class BeanWrapperImpl extends TypeConverterSupport implements BeanWrapper{
	
	/** The wrapped object*/
	private Object object;
	
	/**优化用的*/
	private CachedIntrospectionResults cachedIntrospectionResults;
	
	public BeanWrapperImpl(Object object){
		setWrappedInstance(object);
	}
	
	public void setWrappedInstance(Object object){
		setWrappedInstance(object,"",null);
	}
	
	public void setWrappedInstance(Object object, String nestedPath, Object rootObject) {
		Assert.notNull(object, "Bean object must not be null");
		this.object = object;
		this.typeConverterDelegate = new TypeConverterDelegate();
	}
	
	/**
	 * 把propertyName转化为实际的类
	 * @param value
	 * @param propertyName
	 * @return
	 * @throws TypeMismatchException
	 */
	public Object convertForProperty(Object value, String propertyName) throws TypeMismatchException {
		//其实只需要一个类型描述符
		CachedIntrospectionResults cachedIntrospectionResults =getCachedIntrospectionResults();
		//根据传入的propertyName去寻找javaBean中符合该域的读写域,再交给TypeDescriptor封装去做处理
		PropertyDescriptor pd =cachedIntrospectionResults.getPropertyDescriptor(propertyName);
		if (pd == null) {
			throw new IllegalArgumentException("pd为空 in BeanWrapperImpl");
		}
		TypeDescriptor td =cachedIntrospectionResults.getTypeDescriptor(pd);
		if(td ==null){
			td =new TypeDescriptor(property(pd));
			cachedIntrospectionResults.addTypeDescriptor(pd, td);
		}
		
		return convertForProperty(propertyName, null, value, td);
	}
	
	private Object convertForProperty(String propertyName, Object oldValue, Object newValue, TypeDescriptor td)
			throws TypeMismatchException {

		return convertIfNecessary(propertyName, oldValue, newValue, td.getType(), td);
	}
	
	private Object convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<?> requiredType,
			TypeDescriptor td) throws TypeMismatchException {
		try {
			return this.typeConverterDelegate.convertIfNecessary(propertyName, oldValue, newValue, requiredType, td);
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
	}
	
	private Property property(PropertyDescriptor pd) {
		GenericTypeAwarePropertyDescriptor typeAware = (GenericTypeAwarePropertyDescriptor) pd;
		return new Property(typeAware.getBeanClass(), typeAware.getReadMethod(), typeAware.getWriteMethod(), typeAware.getName());
	}
	
	protected void setIntrospectionClass(Class<?> clazz) {
		if (this.cachedIntrospectionResults != null &&
				!clazz.equals(this.cachedIntrospectionResults.getBeanClass())) {
			this.cachedIntrospectionResults = null;
		}
	}

	/**
	 * Obtain a lazily initializted CachedIntrospectionResults instance
	 * for the wrapped object.
	 */
	private CachedIntrospectionResults getCachedIntrospectionResults() {
		Assert.state(this.object != null, "BeanWrapper does not hold a bean instance");
		if (this.cachedIntrospectionResults == null) {
			this.cachedIntrospectionResults = CachedIntrospectionResults.forClass(getWrappedClass());
		}
		return this.cachedIntrospectionResults;
	}
	
	public final Class<?> getWrappedClass() {
		return (this.object != null ? this.object.getClass() : null);
	}

	@Override
	public Object getWrappedInstance() {
		return this.object;
	}

}
