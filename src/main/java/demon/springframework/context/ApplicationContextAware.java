package demon.springframework.context;

import org.springframework.beans.BeansException;

import us.codecraft.tinyioc.context.ApplicationContext;

public interface ApplicationContextAware {
	
	void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
	
}
