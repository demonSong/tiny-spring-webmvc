package demon.springframework.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.util.Assert;

import demon.springframework.core.convert.Property;
import demon.springframework.core.convert.TypeDescriptor;

public class BeanWrapperImpl extends AbstractPropertyAccessor implements BeanWrapper{
	
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
		this.typeConverterDelegate = new TypeConverterDelegate(this);
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

	//Object property inject
	@Override
	public void setPropertyValue(String propertyName, Object value)
			throws BeansException {
	}
	
	//重写父类方法 
	@Override
	public void setPropertyValue(TestPropertyValue pv) throws BeansException {
		PropertyTokenHolder tokens = (PropertyTokenHolder) pv.resolvedTokens;
		if(tokens ==null){
			String propertyName =pv.getName();
			BeanWrapperImpl nestedBw;
			try {
				nestedBw =getBeanWrapperForPropertyPath(propertyName);
			} catch (Exception e) {
				throw new IllegalArgumentException("nested BeanWrapper are not supported!");
			}
			tokens =getPropertyNameTokens(getFinalPath(nestedBw, propertyName));
			if(nestedBw ==this){
				pv.getOriginalPropertyValue().resolvedTokens =tokens;
			}
			nestedBw.setPropertyValue(tokens,pv);
		}
		else {
			setPropertyValue(tokens, pv);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setPropertyValue(PropertyTokenHolder tokens, TestPropertyValue pv) throws BeansException {
		String propertyName =tokens.canonicalName;
		String actualName =tokens.actualName;
		PropertyDescriptor pd =pv.resolvedDescriptor;
		if(pd ==null || !pd.getWriteMethod().getDeclaringClass().isInstance(this.object)){
			pd =getCachedIntrospectionResults().getPropertyDescriptor(actualName);
			if(pd ==null || pd.getWriteMethod() ==null){
				return;
			}
			pv.getOriginalPropertyValue().resolvedDescriptor =pd;
		}
		Object oldValue =null;
		try {
			Object originalValue = pv.getValue();
			Object valueToApply = originalValue;
			if (!Boolean.FALSE.equals(pv.conversionNecessary)) {
				if(pv.isConverted()){
					valueToApply =pv.getConvertedValue();
				}
				pv.getOriginalPropertyValue().conversionNecessary =(valueToApply !=originalValue);
			}
			final Method writeMethod = (pd instanceof GenericTypeAwarePropertyDescriptor ?
					((GenericTypeAwarePropertyDescriptor) pd).getWriteMethodForActualAccess() :
					pd.getWriteMethod());
			
			final Object value =valueToApply;
			writeMethod.invoke(this.object, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private String getFinalPath(BeanWrapper bw, String nestedPath) {
		if (bw == this) {
			return nestedPath;
		}
		return nestedPath.substring(PropertyAccessorUtils.getLastNestedPropertySeparatorIndex(nestedPath) + 1);
	}
	
	private PropertyTokenHolder getPropertyNameTokens(String propertyName) {
		PropertyTokenHolder tokens = new PropertyTokenHolder();
		String actualName = null;
		tokens.actualName = (actualName != null ? actualName : propertyName);
		tokens.canonicalName = tokens.actualName;
		return tokens;
	}
	
	protected BeanWrapperImpl getBeanWrapperForPropertyPath(String propertyPath) {
		int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(propertyPath);
		// Handle nested properties recursively.
		if (pos > -1) {
			String nestedProperty = propertyPath.substring(0, pos);
			String nestedPath = propertyPath.substring(pos + 1);
			throw new IllegalArgumentException("nested BeanWrapper are not supported!");
		}
		else {
			return this;
		}
	}
	
	private static class PropertyTokenHolder{

		public String canonicalName;
		
		public String actualName;

		public String[] keys;
	}

}
