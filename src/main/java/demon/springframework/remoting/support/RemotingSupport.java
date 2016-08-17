package demon.springframework.remoting.support;

import org.springframework.util.ClassUtils;

import demon.springframework.beans.factory.BeanClassLoaderAware;

public abstract class RemotingSupport implements BeanClassLoaderAware {

	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
	
	protected ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}
}
