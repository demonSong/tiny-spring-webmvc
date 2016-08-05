package demon.springframework.web.servlet.mvc.method.annotation;



import org.springframework.core.annotation.AnnotationUtils;

import demon.springframework.stereotype.Controller;
import demon.springframework.web.context.bind.annotation.RequestMapping;
import demon.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

public class RequestMappingHandlerMapping extends RequestMappingInfoHandlerMapping {

	@Override
	protected boolean isHandler(Class<?> beanType) {
		return ((AnnotationUtils.findAnnotation(beanType, Controller.class) != null) ||
				(AnnotationUtils.findAnnotation(beanType, RequestMapping.class) != null));
	}

}
