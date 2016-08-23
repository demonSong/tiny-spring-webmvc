package demon.springframework.remoting.caucho;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.util.NestedServletException;

import demon.springframework.web.servlet.mvc.Controller;

public class HessianServiceExporter extends HessianExporter implements Controller{

	
	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("HessianServiceExporter: hello,we are now in hessian application!");
		
		if(logger.isDebugEnabled()){
			logger.debug("HessianServiceExporter: hello,we are now in hessian application!");
		}
		if(!"POST".equals(request.getMethod())){
			throw new HttpRequestMethodNotSupportedException(request.getMethod(),
					new String[] {"POST"}, "HessianServiceExporter only supports POST requests");
		}
		
		response.setContentType(CONTENT_TYPE_HESSIAN);
		
		try {
			invoke(request.getInputStream(), response.getOutputStream());
		} catch (Throwable e) {
			throw new NestedServletException("Hessian skeleton invocation failed", e);
		}
		
	}

}
