package demon.springframework.beans.factory.xml;

import org.w3c.dom.Element;

import demon.springframework.beans.BeanDefinition;

public interface BeanDefinitionParser {
	
	BeanDefinition parse(Element element, ParserContext parserContext);

}
