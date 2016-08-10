package demon.springframework.beans.factory.xml;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import demon.springframework.beans.BeanDefinition;

public class BeanDefinitionParserDelegate {

	public static final String BEANS_NAMESPACE_URI="http://www.springframework.org/schema/beans";
	
	public static final String BEAN_ELEMENT="bean";
	
	public static final String ID_ATTRIBUTE = "id";
	
	public static final String CLASS_ATTRIBUTE = "class";
	
	public static final String REF_ELEMENT = "ref";
	
	public static final String PROPS_ELEMENT = "props";

	public static final String PROP_ELEMENT = "prop";
	
	public static final String VALUE_ATTRIBUTE = "value";
	
	private final XmlReaderContext readerContext;
	
	public BeanDefinitionParserDelegate(XmlReaderContext readerContext) {
		Assert.notNull(readerContext, "XmlReaderContext must not be null");
		this.readerContext = readerContext;
	}

	public final XmlReaderContext getReaderContext() {
		return this.readerContext;
	}
	
	public BeanDefinition parseCustomElement(Element ele) {
		return parseCustomElement(ele, null);
	}

	public BeanDefinition parseCustomElement(Element ele, BeanDefinition containingBd) {
		String namespaceUri = getNamespaceURI(ele);
		NamespaceHandler handler =this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
		if (handler == null) {
			return null;
		}
		return handler.parse(ele, new ParserContext(this.readerContext, this));
	}
	
	public boolean nodeNameEquals(Node node,String desiredName){
		return desiredName.equals(node.getNodeName()) || desiredName.equals(getLocalName(node));
	}
	
	public boolean isDefaultNamespace(Node node){
		return isDefaultNamespace(getNamespaceURI(node));
	}
	
	private boolean isDefaultNamespace(String namespaceUri) {
		return (!StringUtils.hasLength(namespaceUri)) || BEANS_NAMESPACE_URI.equals(namespaceUri);
	}

	public String getNamespaceURI(Node node) {
		return node.getNamespaceURI();
	}
	
	public String getLocalName(Node node) {
		return node.getLocalName();
	}
}
