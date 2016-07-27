package demon.springframework.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.context.ApplicationContext;
import demon.springframework.context.ClassPathXmlApplicationContext;
import demon.springframework.context.WebApplicationContext;
import demon.springframework.context.support.WebApplicationContextUtils;

public abstract class FrameworkServlet extends HttpServletBean {

	/**
	 * 封装我们需要的context
	 */
	private WebApplicationContext webApplicationContext;
	
	/** Flag used to detect whether onRefresh has already been called */
	private boolean refreshEventReceived = false;
	
	/**
	 * servlet 加载默认目录
	 */
	private static final String DEFAULT_XML_RESOURCE="applicationContext.xml";
	
	/** ServletContext attribute to find the WebApplicationContext in */
	private String contextAttribute;

	@Override
	protected final void initServletBean() throws ServletException {
		
		try {
			this.webApplicationContext = initWebApplicationContext();
			initFrameWorkServlet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	protected void initFrameWorkServlet() throws ServletException{
	}
	
	protected void onRefresh(ApplicationContext context) {
		// For subclasses: do nothing by default.
	}
	
	/**
	 * 所以说做初始化的时候,针对每一个函数,我们都可以分步骤来完成.
	 * 显得非常有条理性
	 * @return
	 */
	protected WebApplicationContext initWebApplicationContext(){
		
		//1.findWebApplicationContext
		WebApplicationContext wac =findWebApplicationContext();
		
		//2.
		if(wac == null){
			//都是根据当前servletContext来生成的applicationContext
			//applicationContext目前来看没有任何作用,为何要给wac 设置一个这样的applicationContext的父亲呢?
			// WebApplicationContext parent =
			// WebApplicationContextUtils.getWebApplicationContext(getServletContext());		
			wac = createWebApplicationContext();
		}
		
		if (!this.refreshEventReceived) {
			// Apparently not a ConfigurableApplicationContext with refresh support:
			// triggering initial onRefresh manually here.
			onRefresh(wac);
		}
		return wac;
	}
	
	//出现配置不正确,所以导致 context是不存在的 为null值
	protected WebApplicationContext createWebApplicationContext() {
		try {
			return new ClassPathXmlApplicationContext(DEFAULT_XML_RESOURCE);
		} catch (Exception e) {
			throw new IllegalStateException("配置文件名不正确");
		}
	}
	
	
	
	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected final void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected final void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}
	
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doOptions(req, resp);
	}


	@Override
	protected final void doTrace(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doTrace(req, resp);
	}

	protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			try {
				doService(request, response);
			}
			catch (Exception e) {
			}
			finally{
			}
	}
	
	protected abstract void doService(HttpServletRequest request, HttpServletResponse response)
			throws Exception;

	/**
	 * 如果能够先找到,就无需再新建一个
	 * @return
	 */
	protected WebApplicationContext findWebApplicationContext() {
		String attrName = getContextAttribute();
		if (attrName == null) {
			return null;
		}
		WebApplicationContext wac =
				WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
		if (wac == null) {
			throw new IllegalStateException("No WebApplicationContext found: initializer not registered?");
		}
		return wac;
	}
	
	public void setContextAttribute(String contextAttribute) {
		this.contextAttribute = contextAttribute;
	}

	public String getContextAttribute() {
		return this.contextAttribute;
	}
	
	/**
	 * Return this servlet's WebApplicationContext.
	 */
	public final WebApplicationContext getWebApplicationContext() {
		return this.webApplicationContext;
	}
}
