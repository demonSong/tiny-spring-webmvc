package demon.springframework.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import demon.springframework.core.SpringProperties;
import demon.springframework.core.convert.TypeDescriptor;

public class CachedIntrospectionResults {
	
	public static final String IGNORE_BEANINFO_PROPERTY_NAME = "spring.beaninfo.ignore";

	private static final boolean shouldIntrospectorIgnoreBeaninfoClasses =
			SpringProperties.getFlag(IGNORE_BEANINFO_PROPERTY_NAME);
	
	static final Map<Class<?>, Object> classCache = new WeakHashMap<Class<?>, Object>();
	
	static final Set<ClassLoader> acceptedClassLoaders = new HashSet<ClassLoader>();
	
	private final BeanInfo beanInfo;
	
	private final Map<String, PropertyDescriptor> propertyDescriptorCache;
	
	private final Map<PropertyDescriptor, TypeDescriptor> typeDescriptorCache;
	
	private CachedIntrospectionResults(Class<?> beanClass) throws BeansException {
		try {
			BeanInfo beanInfo = null;
			if (beanInfo == null) {
				// If none of the factories supported the class, fall back to the default
				beanInfo = (shouldIntrospectorIgnoreBeaninfoClasses ?
						Introspector.getBeanInfo(beanClass, Introspector.IGNORE_ALL_BEANINFO) :
						Introspector.getBeanInfo(beanClass));
			}
			this.beanInfo = beanInfo;

			this.propertyDescriptorCache = new LinkedHashMap<String, PropertyDescriptor>();

			// This call is slow so we do it once.
			PropertyDescriptor[] pds = this.beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (Class.class.equals(beanClass) &&
						("classLoader".equals(pd.getName()) ||  "protectionDomain".equals(pd.getName()))) {
					// Ignore Class.getClassLoader() and getProtectionDomain() methods - nobody needs to bind to those
					continue;
				}
				pd =buildGenericTypeAwarePropertyDescriptor(beanClass, pd);
				this.propertyDescriptorCache.put(pd.getName(), pd);
			}
			this.typeDescriptorCache = new ConcurrentHashMap<PropertyDescriptor, TypeDescriptor>();
		}
		catch (IntrospectionException ex) {
			throw new FatalBeanException("Failed to obtain BeanInfo for class [" + beanClass.getName() + "]", ex);
		}
	}
	
	private static boolean isClassLoaderAccepted(ClassLoader classLoader) {
		// Iterate over array copy in order to avoid synchronization for the entire
		// ClassLoader check (avoiding a synchronized acceptedClassLoaders Iterator).
		ClassLoader[] acceptedLoaderArray;
		synchronized (acceptedClassLoaders) {
			acceptedLoaderArray = acceptedClassLoaders.toArray(new ClassLoader[acceptedClassLoaders.size()]);
		}
		for (ClassLoader acceptedLoader : acceptedLoaderArray) {
			if (isUnderneathClassLoader(classLoader, acceptedLoader)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isUnderneathClassLoader(ClassLoader candidate, ClassLoader parent) {
		if (candidate == parent) {
			return true;
		}
		if (candidate == null) {
			return false;
		}
		ClassLoader classLoaderToCheck = candidate;
		while (classLoaderToCheck != null) {
			classLoaderToCheck = classLoaderToCheck.getParent();
			if (classLoaderToCheck == parent) {
				return true;
			}
		}
		return false;
	}
	
	static CachedIntrospectionResults forClass(Class<?> beanClass) throws BeansException {
		CachedIntrospectionResults results;
		Object value;
		synchronized (classCache) {
			value = classCache.get(beanClass);
		}
		if (value instanceof Reference) {
			Reference<CachedIntrospectionResults> ref = (Reference<CachedIntrospectionResults>) value;
			results = ref.get();
		}
		else {
			results = (CachedIntrospectionResults) value;
		}
		if (results == null) {
			if (ClassUtils.isCacheSafe(beanClass, CachedIntrospectionResults.class.getClassLoader()) ||
					isClassLoaderAccepted(beanClass.getClassLoader())) {
				results = new CachedIntrospectionResults(beanClass);
				synchronized (classCache) {
					classCache.put(beanClass, results);
				}
			}
			else {
				results = new CachedIntrospectionResults(beanClass);
				synchronized (classCache) {
					classCache.put(beanClass, new SoftReference<CachedIntrospectionResults>(results));
				}
			}
		}
		return results;
	}
	
	PropertyDescriptor getPropertyDescriptor(String name) {
		PropertyDescriptor pd = this.propertyDescriptorCache.get(name);
		if (pd == null && StringUtils.hasLength(name)) {
			// Same lenient fallback checking as in PropertyTypeDescriptor...
			pd = this.propertyDescriptorCache.get(name.substring(0, 1).toLowerCase() + name.substring(1));
			if (pd == null) {
				pd = this.propertyDescriptorCache.get(name.substring(0, 1).toUpperCase() + name.substring(1));
			}
		}
		return (pd == null || pd instanceof GenericTypeAwarePropertyDescriptor ? pd :
				buildGenericTypeAwarePropertyDescriptor(getBeanClass(), pd));
	}
	
	private PropertyDescriptor buildGenericTypeAwarePropertyDescriptor(Class<?> beanClass, PropertyDescriptor pd) {
		try {
			return new GenericTypeAwarePropertyDescriptor(beanClass, pd.getName(), pd.getReadMethod(),
					pd.getWriteMethod(), pd.getPropertyEditorClass());
		}
		catch (IntrospectionException ex) {
			throw new FatalBeanException("Failed to re-introspect class [" + beanClass.getName() + "]", ex);
		}
	}

	Class<?> getBeanClass() {
		return this.beanInfo.getBeanDescriptor().getBeanClass();
	}
	
	void addTypeDescriptor(PropertyDescriptor pd, TypeDescriptor td) {
		this.typeDescriptorCache.put(pd, td);
	}

	TypeDescriptor getTypeDescriptor(PropertyDescriptor pd) {
		return this.typeDescriptorCache.get(pd);
	}
}
