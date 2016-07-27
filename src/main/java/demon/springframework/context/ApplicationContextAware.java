package demon.springframework.context;

import org.springframework.beans.BeansException;

public interface ApplicationContextAware {
	
	void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
	
}
