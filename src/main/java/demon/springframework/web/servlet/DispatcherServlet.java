package demon.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.web.servlet.mvc.Controller;
import demon.springframework.web.util.UrlPathHelper;

public class DispatcherServlet extends FrameworkServlet{

	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
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
		String lookupPath =this.urlPathHelper.getLookupPathForRequest(request);
		String path =lookupPath.substring(1,lookupPath.length());
		Controller handler;
		try {
			handler = (Controller) getWebApplicationContext().getBean(path);
			return handler;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
}
