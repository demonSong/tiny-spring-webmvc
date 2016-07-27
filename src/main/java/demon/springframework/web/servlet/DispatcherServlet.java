package demon.springframework.web.servlet;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.context.ApplicationContext;
import demon.springframework.web.servlet.mvc.Controller;
import demon.springframework.web.util.UrlPathHelper;

public class DispatcherServlet extends FrameworkServlet{
	
	public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";

	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private List<HandlerMapping> handlerMappings;
	
	protected void initStrategies(ApplicationContext context) {
		initHandlerMappings(context);
	}
	
	@Override
	protected void onRefresh(ApplicationContext context) {
		initStrategies(context);
	}
	
	@Override
	protected void doService(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			doDispatch(request, response);
		}
		finally {
		}
	}

	//源码中 使用了handlerAdapter来获得对应的控制器,及对各种controller进行了封装
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpServletRequest processedRequest = request;
		Controller handler =null;
		handler =getHandler(request);
		handler.handleRequest(processedRequest, response);
	}
	
	protected Controller getHandler(HttpServletRequest request){
		for (HandlerMapping hm : this.handlerMappings) {
			try {
				Controller handler = hm.getHandler(request);
				if (handler != null) {
					return handler;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	private void initHandlerMappings(ApplicationContext context) {
		this.handlerMappings = null;

		try {
			HandlerMapping hm = (HandlerMapping) context.getBean(HANDLER_MAPPING_BEAN_NAME);
			this.handlerMappings = Collections.singletonList(hm);
		}
		catch (Exception ex) {
			// Ignore, we'll add a default HandlerMapping later.
		}

		//如果没有配置handlerMapping采取默认策略,也就是规避风险
		if (this.handlerMappings == null) {
			// this.handlerMappings = getDefaultStrategies(context,HandlerMapping.class);
		}
	}
}
