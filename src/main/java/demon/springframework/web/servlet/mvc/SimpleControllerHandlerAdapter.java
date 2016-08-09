package demon.springframework.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.web.servlet.HandlerAdapter;

public class SimpleControllerHandlerAdapter implements HandlerAdapter{

	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		((Controller)handler).handleRequest(request, response);
	}

	@Override
	public boolean supports(Object handler) {
		return (handler instanceof Controller);
	}

}
