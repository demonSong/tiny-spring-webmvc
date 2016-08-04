package demon.springframework.beans.factory.xml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import demon.springframework.beans.BeanDefinition;

public abstract class NamespaceHandlerSupport implements NamespaceHandler {
	
	private final Map<String, BeanDefinitionParser> parsers =
			new HashMap<String, BeanDefinitionParser>();
	
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		return findParserForElement(element, parserContext).parse(element, parserContext);
	}
	
	private BeanDefinitionParser findParserForElement(Element element, ParserContext parserContext) {
		String localName = parserContext.getDelegate().getLocalName(element);
		BeanDefinitionParser parser = this.parsers.get(localName);
		if (parser == null) {
		}
		return parser;
	}
	
	protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
		this.parsers.put(elementName, parser);
	}

}
