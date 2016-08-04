package demon.springframework.beans.factory.xml;

import org.w3c.dom.Element;

import demon.springframework.beans.BeanDefinition;

public interface NamespaceHandler {
	
	void init();
	
	BeanDefinition parse(Element element,ParserContext parserContext);

}
