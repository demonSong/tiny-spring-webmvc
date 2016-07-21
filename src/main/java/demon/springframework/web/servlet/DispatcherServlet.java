package demon.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.web.servlet.mvc.Controller;

public class DispatcherServlet extends FrameworkServlet{

	
	@Override
	protected void doService(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			doDispatch(request, response);
		}
		finally {
		}
	}

	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpServletRequest processedRequest = request;
		Controller handler =getHandler(request);
		handler.handleRequest(processedRequest, response);
	}
	
	protected Controller getHandler(HttpServletRequest request){
		String path =request.getServletPath();
		String configLocation =path.substring(1,path.length());
		Controller handler;
		try {
			handler = (Controller) getWebApplicationContext().getBean(configLocation);
			return handler;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
}
