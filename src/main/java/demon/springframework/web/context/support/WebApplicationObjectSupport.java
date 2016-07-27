package demon.springframework.web.context.support;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import demon.springframework.context.ApplicationContext;
import demon.springframework.context.support.ApplicationObjectSupport;
import demon.springframework.web.context.ServletContextAware;

public abstract class WebApplicationObjectSupport extends ApplicationObjectSupport 
		implements ServletContextAware {

	private ServletContext servletContext;

	public final void setServletContext(ServletContext servletContext) {
		if (servletContext != this.servletContext) {
			this.servletContext = servletContext;
			if (servletContext != null) {
				initServletContext(servletContext);
			}
		}
	}
	
	
	//子类同样遵循模板的延续
	@Override
	protected void initApplicationContext(ApplicationContext context) {
		super.initApplicationContext(context);
		if (this.servletContext == null && context instanceof WebApplicationContext) {
			this.servletContext = ((WebApplicationContext) context).getServletContext();
			if (this.servletContext != null) {
				initServletContext(this.servletContext);
			}
		}
	}
	
	@Override
	protected boolean isContextRequired() {
		return true;
	}
	
	protected void initServletContext(ServletContext servletContext) {
	}

}
