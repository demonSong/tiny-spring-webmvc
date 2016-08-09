package demon.springframework.web.servlet.mvc.method.annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import demon.springframework.web.method.HandlerMethod;
import demon.springframework.web.method.support.InvocableHandlerMethod;
import demon.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;

public class RequestMappingHandlerAdapter extends AbstractHandlerMethodAdapter {

	@Override
	protected final void handleInternal(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handler)
			throws Exception {
		
		invokeHandleMethod(request, response, handler);
		
	}
	
	private void invokeHandleMethod(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
		
		InvocableHandlerMethod requestMappingMethod =createRequestMappingMethod(handlerMethod);
		requestMappingMethod.invokeForRequest(request, response);
	}
	
	private InvocableHandlerMethod createRequestMappingMethod(HandlerMethod handlerMethod){
		InvocableHandlerMethod requestMethod =new InvocableHandlerMethod(handlerMethod);
		return requestMethod;
	}
	
}
