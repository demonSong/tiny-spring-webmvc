package demon.springframework.remoting.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;

import demon.springframework.beans.factory.BeanClassLoaderAware;

public abstract class RemotingSupport implements BeanClassLoaderAware {
	
	protected final Log logger =LogFactory.getLog(getClass());

	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
	
	protected ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}
	
	protected ClassLoader overrideThreadContextClassLoader() {
		return ClassUtils.overrideThreadContextClassLoader(getBeanClassLoader());
	}
	
	protected void resetThreadContextClassLoader(ClassLoader original) {
		if (original != null) {
			Thread.currentThread().setContextClassLoader(original);
		}
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}
}
