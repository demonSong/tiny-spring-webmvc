package demon.springframework.web.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.context.ApplicationContext;
import demon.springframework.web.servlet.mvc.Controller;
import demon.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import demon.springframework.web.util.UrlPathHelper;

public class DispatcherServlet extends FrameworkServlet{
	
	public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
	
	public static final String REQUEST_MAPPING_HANDLER_MAPPING = "requestMappingHandlerMapping";

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
		
		//检测所有handlerMapping开关
		try {
			Map<String,HandlerMapping> matchingBeans =new HashMap<String, HandlerMapping>();
			//根据context来获得当前在IOC中的bean,simplerHandlerMapping是在xml文件中初始化的
			//但requestHandlerMapping初始化的顺序跟controller的读取顺序有关系
			HandlerMapping hm = (HandlerMapping) context.getBean(HANDLER_MAPPING_BEAN_NAME);
			matchingBeans.put(REQUEST_MAPPING_HANDLER_MAPPING, (HandlerMapping) context.getBean(REQUEST_MAPPING_HANDLER_MAPPING));
			//-----------------------------------just a test-------------------------------------------//
			((RequestMappingHandlerMapping) context.getBean(REQUEST_MAPPING_HANDLER_MAPPING)).afterPropertiesSet();
			matchingBeans.put(HANDLER_MAPPING_BEAN_NAME, hm);
			this.handlerMappings = new ArrayList<HandlerMapping>(matchingBeans.values());
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
