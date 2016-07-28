package demon.springframework.beans.io.support;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

import demon.springframework.beans.io.DefaultResourceLoader;
import demon.springframework.beans.io.Resource;
import demon.springframework.beans.io.ResourceLoader;

//作为resourceLoader 的接口 组合了resourceLoader的部分功能
public class PathMatchingResourcePatternResolver extends ResourceLoader{
	
	private static Method equinoxResolveMethod;

	static {
		// Detect Equinox OSGi (e.g. on WebSphere 6.1)
		try {
			Class fileLocatorClass = PathMatchingResourcePatternResolver.class.getClassLoader().loadClass(
					"org.eclipse.core.runtime.FileLocator");
			equinoxResolveMethod = fileLocatorClass.getMethod("resolve", URL.class);
		}
		catch (Throwable ex) {
			equinoxResolveMethod = null;
		}
	}

	//直接使用 spring所提供的工具类吧
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	private final ResourceLoader resourceLoader;
	
	public PathMatchingResourcePatternResolver() {
		this.resourceLoader = new DefaultResourceLoader();
	}
	
	public PathMatchingResourcePatternResolver(ResourceLoader resourceLoader) {
		Assert.notNull(resourceLoader, "ResourceLoader must not be null");
		this.resourceLoader = resourceLoader;
	}
	
	public ResourceLoader getResourceLoader() {
		return this.resourceLoader;
	}
	
	//组合实现
	public Resource getResource(String location) {
		return getResourceLoader().getResource(location);
	}
	
	public PathMatcher getPathMatcher() {
		return this.pathMatcher;
	}
	
	public Resource[] getResources(String locationPattern) throws IOException {
		Assert.notNull(locationPattern, "Location pattern must not be null");
		if (locationPattern.startsWith(CLASSPATH_ALL_URL_PREFIX)) {
			if (getPathMatcher().isPattern(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()))) {
				// a class path resource pattern
				return findPathMatchingResources(locationPattern);
			}
			else {
				
			}
		}
		else {
			
		}
		return null;
	}

	//用到了一种漂亮的递归
	protected Resource[] findPathMatchingResources(String locationPattern) throws IOException {
		String rootDirPath = determineRootDir(locationPattern);
		String subPattern = locationPattern.substring(rootDirPath.length());
		//这是一种递归?
		Resource[] rootDirResources = getResources(rootDirPath);
		Set<Resource> result = new LinkedHashSet<Resource>(16);
		return result.toArray(new Resource[result.size()]);
	}
	
	protected String determineRootDir(String location) {
		int prefixEnd = location.indexOf(":") + 1;
		int rootDirEnd = location.length();
		while (rootDirEnd > prefixEnd && getPathMatcher().isPattern(location.substring(prefixEnd, rootDirEnd))) {
			rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
		}
		if (rootDirEnd == 0) {
			rootDirEnd = prefixEnd;
		}
		return location.substring(0, rootDirEnd);
	}
	
	protected Resource resolveRootDirResource(Resource original) throws IOException {
		return original;
	}
}
