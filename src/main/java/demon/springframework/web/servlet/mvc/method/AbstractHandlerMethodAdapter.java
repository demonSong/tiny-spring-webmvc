package demon.springframework.web.servlet.mvc.method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.web.method.HandlerMethod;
import demon.springframework.web.servlet.HandlerAdapter;

public abstract class AbstractHandlerMethodAdapter implements HandlerAdapter{

	@Override
	public final boolean supports(Object handler) {
		return handler instanceof HandlerMethod;
	}
	
	@Override
	public final void handle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		handleInternal(request,response,(HandlerMethod)handler);
	}

	protected abstract void handleInternal(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handler) throws Exception;
}
