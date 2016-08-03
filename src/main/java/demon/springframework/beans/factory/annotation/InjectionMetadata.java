package demon.springframework.beans.factory.annotation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.ReflectionUtils;

import demon.springframework.beans.PropertyValues;

public class InjectionMetadata {	
	
	private final Set<InjectedElement> injectedElements;
	
	public InjectionMetadata(Class targetClass, Collection<InjectedElement> elements) {
		this.injectedElements = new LinkedHashSet<InjectedElement>();
		for (InjectedElement element : elements) {
			this.injectedElements.add(element);
		}
	}
	
	public void inject(Object target ,String beanName,PropertyValues pvs) throws Throwable{
		if(!this.injectedElements.isEmpty()){
			for (InjectedElement element : this.injectedElements) {
				element.inject(target, beanName, pvs);
			}
		}
	}
	
	public static abstract class InjectedElement{
		
		protected final Member member;
		
		protected final boolean isField;
		
		protected final PropertyDescriptor pd;
		
		protected volatile Boolean skip;
		
		protected InjectedElement(Member member, PropertyDescriptor pd) {
			this.member = member;
			this.isField = (member instanceof Field);
			this.pd = pd;
		}
		
		public final Member getMember() {
			return this.member;
		}

		protected void inject(Object target, String requestingBeanName, PropertyValues pvs) throws Throwable {
			if (this.isField) {
				Field field = (Field) this.member;
				ReflectionUtils.makeAccessible(field);
				field.set(target, getResourceToInject(target, requestingBeanName));
			}
			else {
				if (checkPropertySkipping(pvs)) {
					return;
				}
				try {
					Method method = (Method) this.member;
					ReflectionUtils.makeAccessible(method);
					method.invoke(target, getResourceToInject(target, requestingBeanName));
				}
				catch (InvocationTargetException ex) {
					throw ex.getTargetException();
				}
			}
		}
		
		protected Object getResourceToInject(Object target, String requestingBeanName) {
			return null;
		}
		
		protected boolean checkPropertySkipping(PropertyValues pvs) {
			if (this.skip == null) {
				if (pvs != null) {
					//当pvs不等于null时
					this.skip =true;
					return true;
				}
				this.skip = false;
			}
			return this.skip;
		}
	}
	
	
}
