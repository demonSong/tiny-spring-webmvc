package demon.springframework.web.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.context.ApplicationContext;
import demon.springframework.web.util.UrlPathHelper;

public class DispatcherServlet extends FrameworkServlet{
	
	public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
	
	public static final String REQUEST_MAPPING_HANDLER_MAPPING = "requestMappingHandlerMapping";
	
	public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
	
	public static final String REQUEST_MAPPING_HANDLER_ADDAPTER="requestMappingHandlerAdapter";

	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private List<HandlerMapping> handlerMappings;
	
	private List<HandlerAdapter> handlerAdapters;
	
	protected void initStrategies(ApplicationContext context) {
		initHandlerMappings(context);
		initHandlerAdapters(context);
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
		HandlerExecutionChain mappedHandler =null;
		mappedHandler =getHandler(request);
		if(mappedHandler ==null || mappedHandler.getHandler() ==null){
			return;
		}
		//封装了所有的handler后,采用适配器来封装不同形式的handlermapping的实现
		HandlerAdapter ha =getHandlerAdapter(mappedHandler.getHandler());
		try {
			ha.handle(processedRequest, response,mappedHandler.getHandler());
		} 
		finally{
		}
	}
	
	protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException{
		for (HandlerAdapter ha : this.handlerAdapters) {
			if (ha.supports(handler)) {
				return ha;
			}
		}
		throw new ServletException("没有找到适配的处理器");
	}
	
	protected HandlerExecutionChain getHandler(HttpServletRequest request){
		for (HandlerMapping hm : this.handlerMappings) {
			try {
				HandlerExecutionChain handler = hm.getHandler(request);
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
	
	private void initHandlerAdapters(ApplicationContext context) {	
		this.handlerAdapters =null;
		try {
			Map<String, HandlerAdapter> matchingBeans =new HashMap<String, HandlerAdapter>();
			HandlerAdapter ha = (HandlerAdapter) context.getBean(HANDLER_ADAPTER_BEAN_NAME);
			matchingBeans.put(REQUEST_MAPPING_HANDLER_ADDAPTER, (HandlerAdapter) context.getBean(REQUEST_MAPPING_HANDLER_ADDAPTER));
			matchingBeans.put(HANDLER_ADAPTER_BEAN_NAME, ha);
			this.handlerAdapters=new ArrayList<HandlerAdapter>(matchingBeans.values());
		} catch (Exception e) {
		}
		
		
	}
}
