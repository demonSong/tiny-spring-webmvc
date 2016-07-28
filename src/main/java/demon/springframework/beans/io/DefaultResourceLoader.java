package demon.springframework.beans.io;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class DefaultResourceLoader extends ResourceLoader {
	
	private ClassLoader classLoader;
	
	public DefaultResourceLoader() {
		this.classLoader = ClassUtils.getDefaultClassLoader();
	}
	
	public DefaultResourceLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	public ClassLoader getClassLoader() {
		return (this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader());
	}


	public Resource getResource(String location) {
		Assert.notNull(location, "Location must not be null");
		return super.getResource(location);
	}
}
