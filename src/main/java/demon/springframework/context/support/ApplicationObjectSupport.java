package demon.springframework.context.support;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextException;

import us.codecraft.tinyioc.context.ApplicationContext;
import demon.springframework.context.ApplicationContextAware;

/**
 * 基本想法:这个类是用来在tomcat方法初始化中,进行对一些spring 基本组件的初始化的模板类
 * @author demon.song
 *
 */
public abstract class ApplicationObjectSupport implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	public final void setApplicationContext(ApplicationContext context) throws BeansException {
		if (context == null && !isContextRequired()) {
			// Reset internal context state.
			this.applicationContext = null;
		}
		else if (this.applicationContext == null) {
			// Initialize with passed-in context.
			if (!requiredContextClass().isInstance(context)) {
				throw new ApplicationContextException(
						"Invalid application context: needs to be of type [" + requiredContextClass().getName() + "]");
			}
			this.applicationContext = context;
			initApplicationContext(context);
		}
		else {
			// Ignore reinitialization if same context passed in.
			if (this.applicationContext != context) {
				throw new ApplicationContextException(
						"Cannot reinitialize with different application context: current one is [" +
						this.applicationContext + "], passed-in one is [" + context + "]");
			}
		}
	}
	
	protected boolean isContextRequired() {
		return false;
	}
	
	protected Class requiredContextClass() {
		return ApplicationContext.class;
	}
	
	//源码中为什么要多此一举,为何不直接调用,子类重写maybe需要context
	protected void initApplicationContext(ApplicationContext context) throws BeansException {
		initApplicationContext();
	}
	
	protected void initApplicationContext() throws BeansException {
	}
	
	public final ApplicationContext getApplicationContext() throws IllegalStateException {
		if (this.applicationContext == null && isContextRequired()) {
			throw new IllegalStateException(
					"ApplicationObjectSupport instance [" + this + "] does not run in an ApplicationContext");
		}
		return this.applicationContext;
	}
}
