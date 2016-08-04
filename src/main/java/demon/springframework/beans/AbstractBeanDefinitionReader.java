package demon.springframework.beans;

import java.util.HashMap;
import java.util.Map;

import demon.springframework.beans.factory.support.BeanDefinitionRegistry;
import demon.springframework.beans.io.ResourceLoader;

/**
 * 从配置中读取BeanDefinition
 * 
 * @author yihua.huang@dianping.com
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

	private final BeanDefinitionRegistry registry;
	
    private Map<String,BeanDefinition> registrys;

    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this(null, resourceLoader);
    }

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry,ResourceLoader resourceLoader){
    	this.registrys = new HashMap<String, BeanDefinition>();
        this.resourceLoader = resourceLoader;
        this.registry =registry;
    }
    
    public Map<String, BeanDefinition> getRegistry() {
        return registrys;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
    
    public final BeanDefinitionRegistry getBeanFactory() {
		return this.registry;
	}
}
