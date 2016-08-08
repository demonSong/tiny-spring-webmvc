package demon.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;

import demon.springframework.web.servlet.mvc.Controller;

public interface HandlerMapping {
	
	String PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE = HandlerMapping.class.getName() + ".pathWithinHandlerMapping";

	Controller getHandler(HttpServletRequest request) throws Exception;
	
}
