package demon.springframework.beans.factory.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ReflectionUtils;

import demon.springframework.aop.BeanFactoryAware;
import demon.springframework.beans.PropertyValues;
import demon.springframework.beans.factory.BeanFactory;
import demon.springframework.beans.factory.config.AutowireCapableBeanFactory;
import demon.springframework.beans.factory.config.DependencyDescriptor;
import demon.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

//有些新功能,暂时不需要,完成对注解的解析
/**
 * 1.谁来调用postProcessPropertyValues方法 ？ 在beanfactory的子类中,会获得各种processor,并且调用processor的该方法
 * 2.谁来初始化beanfactory这个域?
 * @author demon.song
 *
 */
public class AutowiredAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
		implements BeanFactoryAware{
	
	private final Set<Class<? extends Annotation>> autowiredAnnotationTypes= new LinkedHashSet<Class<? extends Annotation>>();
	
	//concurrentHashmap的作用
	private final Map<Class<?>, InjectionMetadata> injectionMetadataCache =
			new ConcurrentHashMap<Class<?>, InjectionMetadata>();
	
	private boolean requiredParameterValue = true;
	
	private String requiredParameterName = "required";
	
	//谁来给bean进行初始化操作呢?
	private AutowireCapableBeanFactory beanFactory;
	
	public AutowiredAnnotationBeanPostProcessor() {
		this.autowiredAnnotationTypes.add(Autowired.class);
		this.autowiredAnnotationTypes.add(Value.class);
		ClassLoader cl = AutowiredAnnotationBeanPostProcessor.class.getClassLoader();
		try {
			this.autowiredAnnotationTypes.add((Class<? extends Annotation>) cl.loadClass("javax.inject.Inject"));
		}
		catch (ClassNotFoundException ex) {
			// JSR-330 API not available - simply skip.
		}
	}
	
	@Override
	public PropertyValues postProcessPropertyValues(
			PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws Exception {
		InjectionMetadata metadata =findAutowiringMetadata(bean.getClass());
		try {
			metadata.inject(bean, beanName, pvs);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pvs;
	}
	
	private InjectionMetadata findAutowiringMetadata(Class<?> clazz){
		InjectionMetadata metadata = this.injectionMetadataCache.get(clazz);
		if (metadata == null) {
			synchronized (this.injectionMetadataCache) {
				metadata = this.injectionMetadataCache.get(clazz);
				if (metadata == null) {
					metadata = buildAutowiringMetadata(clazz);
					this.injectionMetadataCache.put(clazz, metadata);
				}
			}
		}
		return metadata;
	}
	
	private InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
		LinkedList<InjectionMetadata.InjectedElement> elements =new LinkedList<InjectionMetadata.InjectedElement>();
		//为什么需要赋值给一个新的变量
		Class<?> targetClass =clazz;
		
		do {
			LinkedList<InjectionMetadata.InjectedElement> currElements = new LinkedList<InjectionMetadata.InjectedElement>();
			for (Field field : targetClass.getDeclaredFields()) {
				Annotation annotation = findAutowiredAnnotation(field);
				if (annotation != null) {
					if (Modifier.isStatic(field.getModifiers())) {
						continue;
					}
					boolean required = determineRequiredStatus(annotation);
					currElements.add(new AutowiredFieldElement(field, required));
				}
			}
			for (Method method : targetClass.getDeclaredMethods()) {
				//暂时不支持method方法
			}
			elements.addAll(0, currElements);
			targetClass = targetClass.getSuperclass();
		}
		while (targetClass != null && targetClass != Object.class);//直到父类 循环才结束,跟继承有关系
		
		return new InjectionMetadata(clazz, elements);
	}
	
	private Annotation findAutowiredAnnotation(AccessibleObject ao) {
		for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
			Annotation annotation = ao.getAnnotation(type);
			if (annotation != null) {
				return annotation;
			}
		}
		return null;
	}
	
	protected boolean determineRequiredStatus(Annotation annotation) {
		try {
			Method method = ReflectionUtils.findMethod(annotation.annotationType(), this.requiredParameterName);
			return (this.requiredParameterValue == (Boolean) ReflectionUtils.invokeMethod(method, annotation));
		}
		catch (Exception ex) {
			// required by default
			return true;
		}
	}
	
	private class AutowiredFieldElement extends InjectionMetadata.InjectedElement{
		
		public AutowiredFieldElement(Field field, boolean required) {
			super(field, null);
		}
		
		@Override
		protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
			Field field = (Field) this.member;
			try {
				Object value;
				//还需要根据beanName获得field的name
				DependencyDescriptor descriptor =new DependencyDescriptor(field, false);
				Set<String> autowiredBeanNames = new LinkedHashSet<String>(1);
				value =beanFactory.resolveDependency(descriptor, beanName, autowiredBeanNames);
				if (value != null) {
					ReflectionUtils.makeAccessible(field);
					field.set(bean, value);
				}
			}
			catch (Throwable ex) {
				throw new BeanCreationException("Could not autowire field: " + field, ex);
			}
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws Exception {
		//源码中 对类层次结构划分的更清楚
		if(beanFactory instanceof AutowireCapableBeanFactory){
			this.beanFactory =(AutowireCapableBeanFactory) beanFactory;
		}
	}
}
