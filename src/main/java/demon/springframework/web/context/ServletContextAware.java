package demon.springframework.web.context;

import javax.servlet.ServletContext;

public interface ServletContextAware {

	void setServletContext(ServletContext servletContext);
	
}
