package demon.springframework.beans;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.LogFactory;

class GenericTypeAwarePropertyDescriptor extends PropertyDescriptor {
	
	private final Class<?> beanClass;

	private final Method readMethod;

	private final Method writeMethod;

	private final Class<?> propertyEditorClass;

	private volatile Set<Method> ambiguousWriteMethods;

	private Class<?> propertyType;
	
	public GenericTypeAwarePropertyDescriptor(Class<?> beanClass,String propertyName,
			Method readMethod,Method writeMethod,Class<?> propertyEditorClass) throws IntrospectionException{
		super(propertyName, null,null);
		this.beanClass=beanClass;
		this.propertyEditorClass=propertyEditorClass;
		this.readMethod=readMethod;
		this.writeMethod=writeMethod;
		if (this.writeMethod != null && this.readMethod == null) {
			// Write method not matched against read method: potentially ambiguous through
			// several overloaded variants, in which case an arbitrary winner has been chosen
			// by the JDK's JavaBeans Introspector...
			Set<Method> ambiguousCandidates = new HashSet<Method>();
			for (Method method : beanClass.getMethods()) {
				if (method.getName().equals(writeMethod.getName()) &&
						!method.equals(writeMethod) && !method.isBridge()) {
					ambiguousCandidates.add(method);
				}
			}
			if (!ambiguousCandidates.isEmpty()) {
				this.ambiguousWriteMethods = ambiguousCandidates;
			}
		}
	}
	
	public Method getWriteMethodForActualAccess() {
		Set<Method> ambiguousCandidates = this.ambiguousWriteMethods;
		if (ambiguousCandidates != null) {
			this.ambiguousWriteMethods = null;
			LogFactory.getLog(GenericTypeAwarePropertyDescriptor.class).warn("Invalid JavaBean property '" +
					getName() + "' being accessed! Ambiguous write methods found next to actually used [" +
					this.writeMethod + "]: " + ambiguousCandidates);
		}
		return this.writeMethod;
	}
	
	public Class<?> getBeanClass() {
		return this.beanClass;
	}

	@Override
	public Method getReadMethod() {
		return this.readMethod;
	}

	@Override
	public Method getWriteMethod() {
		return this.writeMethod;
	}
	
	@Override
	public Class<?> getPropertyEditorClass() {
		return this.propertyEditorClass;
	}
}
