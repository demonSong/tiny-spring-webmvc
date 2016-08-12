package demon.springframework.beans.factory.xml;


import demon.springframework.beans.factory.parsing.ReaderContext;
import demon.springframework.beans.factory.support.BeanDefinitionRegistry;
import demon.springframework.beans.io.Resource;
import demon.springframework.beans.io.ResourceLoader;
import demon.springframework.beans.xml.XmlBeanDefinitionReader;

public class XmlReaderContext extends ReaderContext{
	
	private final XmlBeanDefinitionReader reader;
	
	private final NamespaceHandlerResolver namespaceHandlerResolver;
	
	public XmlReaderContext(Resource resource,XmlBeanDefinitionReader reader,NamespaceHandlerResolver namespaceHandlerResolver){
		super(resource);
		this.reader =reader;
		this.namespaceHandlerResolver =namespaceHandlerResolver;
	}
	
	public NamespaceHandlerResolver getNamespaceHandlerResolver() {
		return this.namespaceHandlerResolver;
	}
	
	public final BeanDefinitionRegistry getRegistry() {
		return this.reader.getBeanFactory();
	}

	public final ResourceLoader getResourceLoader() {
		return this.reader.getResourceLoader();
	}
	
	public final ClassLoader getBeanClassLoader() {
		return this.reader.getBeanClassLoader();
	}
}
