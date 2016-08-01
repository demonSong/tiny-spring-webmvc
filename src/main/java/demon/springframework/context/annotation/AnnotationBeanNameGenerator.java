package demon.springframework.context.annotation;


import java.beans.Introspector;
import java.util.Map;
import java.util.Set;

import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import demon.springframework.beans.factory.support.BeanDefinitionRegistry;
import demon.springframework.beans.factory.support.BeanNameGenerator;
import demon.springframework.core.type.AnnotationMetadata;

public class AnnotationBeanNameGenerator implements BeanNameGenerator{
	
	private static final String COMPONENT_ANNOTATION_CLASSNAME = "org.springframework.stereotype.Component";

	@Override
	public String generateBeanName(BeanDefinition definition,BeanDefinitionRegistry registry) {
		if (definition instanceof AnnotatedBeanDefinition) {
			String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
			if (StringUtils.hasText(beanName)) {
				// Explicit bean name found.
				return beanName;
			}
		}
		// Fallback: generate a unique default bean name.
		return buildDefaultBeanName(definition);
	}
	
	protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
		AnnotationMetadata amd = annotatedDef.getMetadata();
		Set<String> types = amd.getAnnotationTypes();
		String beanName = null;
		for (String type : types) {
			Map<String, Object> attributes = amd.getAnnotationAttributes(type);
			if (isStereotypeWithNameValue(type, amd.getMetaAnnotationTypes(type), attributes)) {
				String value = (String) attributes.get("value");
				if (StringUtils.hasLength(value)) {
					if (beanName != null && !value.equals(beanName)) {
						throw new IllegalStateException("Stereotype annotations suggest inconsistent " +
								"component names: '" + beanName + "' versus '" + value + "'");
					}
					beanName = value;
				}
			}
		}
		return beanName;
	}
	
	protected boolean isStereotypeWithNameValue(String annotationType,
			Set<String> metaAnnotationTypes, Map<String, Object> attributes) {

		boolean isStereotype = annotationType.equals(COMPONENT_ANNOTATION_CLASSNAME) ||
				(metaAnnotationTypes != null && metaAnnotationTypes.contains(COMPONENT_ANNOTATION_CLASSNAME)) ||
				annotationType.equals("javax.annotation.ManagedBean") ||
				annotationType.equals("javax.inject.Named");
		return (isStereotype && attributes != null && attributes.containsKey("value"));
	}
	
	protected String buildDefaultBeanName(BeanDefinition definition) {
		String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
		return Introspector.decapitalize(shortClassName);
	}

}
