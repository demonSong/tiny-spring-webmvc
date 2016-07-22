package demon.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;

import demon.springframework.web.servlet.mvc.Controller;

public interface HandlerMapping {

	Controller getHandler(HttpServletRequest request) throws Exception;
	
}
