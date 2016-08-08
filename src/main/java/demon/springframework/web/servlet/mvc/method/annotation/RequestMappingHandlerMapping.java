package demon.springframework.web.servlet.mvc.method.annotation;



import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringValueResolver;

import demon.springframework.stereotype.Controller;
import demon.springframework.web.context.bind.annotation.RequestMapping;
import demon.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import demon.springframework.web.servlet.mvc.condition.RequestCondition;
import demon.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import demon.springframework.web.servlet.mvc.method.RequestMappingInfo;
import demon.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

public class RequestMappingHandlerMapping extends RequestMappingInfoHandlerMapping {

	private StringValueResolver embeddedValueResolver;
	
	private boolean useSuffixPatternMatch = true;

	private boolean useTrailingSlashMatch = true;
	
	private final List<String> fileExtensions = new ArrayList<String>();
	
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		this.embeddedValueResolver  = resolver;
	}
	
	public List<String> getFileExtensions() {
		return this.fileExtensions;
	}
	
	@Override
	protected boolean isHandler(Class<?> beanType) {
		return ((AnnotationUtils.findAnnotation(beanType, Controller.class) != null) ||
				(AnnotationUtils.findAnnotation(beanType, RequestMapping.class) != null));
	}

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method,Class<?> handlerType) {
		RequestMappingInfo info =null;
		RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
		//搜寻的方法比较奇怪,判断每个方法是否拥有requestMapping这个注解
		if (methodAnnotation != null) {
			//主要功能为参数的组装
			RequestCondition<?> methodCondition =getCustomMethodCondition(method);
			info = createRequestMappingInfo(methodAnnotation, methodCondition);
			//对类进行搜索...
			RequestMapping typeAnnotation = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
			if (typeAnnotation != null) {
				RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
				info = createRequestMappingInfo(typeAnnotation, typeCondition).combine(info);
			}
		}
		return info;
	}
	
	protected String[] resolveEmbeddedValuesInPatterns(String[] patterns) {
		if (this.embeddedValueResolver == null) {
			return patterns;
		}
		else {//暂且不需要resolver
			String[] resolvedPatterns = new String[patterns.length];
			for (int i = 0; i < patterns.length; i++) {
				resolvedPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
			}
			return resolvedPatterns;
		}
	}
	
	protected RequestCondition<?> getCustomMethodCondition(Method method) {
		return null;
	}
	
	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		return null;
	}

	protected RequestMappingInfo createRequestMappingInfo(RequestMapping annotation, RequestCondition<?> customCondition) {
		String[] patterns =resolveEmbeddedValuesInPatterns(annotation.value());
		//添加method方法
		return new RequestMappingInfo(
				new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(),
						this.useSuffixPatternMatch, this.useTrailingSlashMatch, this.fileExtensions),
				new RequestMethodsRequestCondition(annotation.method()));
	}
}
