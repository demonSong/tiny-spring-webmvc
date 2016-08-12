package demon.springframework.beans.factory.xml;

import org.springframework.beans.factory.parsing.BeanEntry;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.beans.factory.parsing.PropertyEntry;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.DmnBeanDefinition;
import demon.springframework.beans.TestPropertyValue;
import demon.springframework.beans.factory.config.RuntimeBeanReference;
import demon.springframework.beans.factory.config.TestBeanDefinitionHolder;
import demon.springframework.beans.factory.config.TypedStringValue;
import demon.springframework.beans.factory.support.AbstractBeanDefinition;
import demon.springframework.beans.factory.support.BeanDefinitionReaderUtils;

public class BeanDefinitionParserDelegate {

	public static final String BEANS_NAMESPACE_URI="http://www.springframework.org/schema/beans";
	
	public static final String BEAN_ELEMENT="bean";
	
	public static final String ID_ATTRIBUTE = "id";
	
	public static final String CLASS_ATTRIBUTE = "class";
	
	private static final String SINGLETON_ATTRIBUTE = "singleton";
	
	public static final String SCOPE_ATTRIBUTE = "scope";
	
	public static final String REF_ELEMENT = "ref";
	
	public static final String PROPERTY_ELEMENT = "property";
	
	public static final String PROPS_ELEMENT = "props";

	public static final String PROP_ELEMENT = "prop";
	
	public static final String VALUE_ATTRIBUTE = "value";
	
	public static final String NAME_ATTRIBUTE="name";
	
	private final XmlReaderContext readerContext;
	
	private final ParseState parseState = new ParseState();
	
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
	
	public TestBeanDefinitionHolder parseBeanDefinitionElement(Element ele){
		return parseBeanDefinitionElement(ele, null);
	}
	
	public TestBeanDefinitionHolder parseBeanDefinitionElement(Element ele, DmnBeanDefinition containingBean){
		String id =ele.getAttribute(ID_ATTRIBUTE);
		
		String beanName =id;
		AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele,beanName,containingBean);
		if(beanDefinition !=null){
			if(!StringUtils.hasText(beanName)){
				try {
					if (containingBean != null) {
						//目前来说,不可能为null
					}
					else {
						String beanClassName = beanDefinition.getBeanClassName();
						if (beanClassName != null &&
								beanName.startsWith(beanClassName) && beanName.length() > beanClassName.length() &&
								!this.readerContext.getRegistry().isBeanNameInUse(beanClassName)) {
						}
					}
				}
				catch (Exception ex) {
					//输出错误警告
					return null;
				}
			}
			return new TestBeanDefinitionHolder(beanDefinition, beanName);
		}
		return null;
	}
	
	public AbstractBeanDefinition parseBeanDefinitionElement(Element ele,String beanName, DmnBeanDefinition containingBean) {
		//栈
		this.parseState.push(new BeanEntry(beanName));
		String className =null;
		if(ele.hasAttribute(CLASS_ATTRIBUTE)){
			className=ele.getAttribute(CLASS_ATTRIBUTE).trim();
		}
		//暂时不需要parent信息
		String parent =null;
		try {
			//初始化 完bean 中所有的属性
			AbstractBeanDefinition bd =createBeanDefinition(className, parent);
			parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
			
			parsePropertyElements(ele, bd);
			
			return bd;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally{
			this.parseState.pop();
		}
		return null;
	}
	
	public void parsePropertyElements(Element beanEle, DmnBeanDefinition bd) {
		NodeList nl = beanEle.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (isCandidateElement(node) && nodeNameEquals(node, PROPERTY_ELEMENT)) {
				parsePropertyElement((Element) node, bd);
			}
		}
	}

	public void parsePropertyElement(Element ele, DmnBeanDefinition bd) {
		String propertyName =ele.getAttribute(NAME_ATTRIBUTE);
		if(!StringUtils.hasLength(propertyName)){
			//出错信息
			Assert.notNull(propertyName,"property must have a 'name' attribute");
			return;
		}
		this.parseState.push(new PropertyEntry(propertyName));
		try {
			Object val =parsePropertyValue(ele, bd, propertyName);
			TestPropertyValue pv =new TestPropertyValue(propertyName,val);
			bd.getPropertyValues().addPropertyValue(pv);
		}
		finally{
			this.parseState.pop();
		}
	}

	public Object parsePropertyValue(Element ele, DmnBeanDefinition bd,String propertyName) {
		NodeList n1 =ele.getChildNodes();
		Element subElement =null;
		for(int i =0 ;i <n1.getLength(); i++){
			Node node =n1.item(i);
			if(node instanceof Element){
				if (subElement !=null) {
					//
				} 
				else {
					subElement =(Element) node;
				}
			}
		}
		
		boolean hasRefAttribute =ele.hasAttribute(REF_ELEMENT);
		boolean hasValueAttribute =ele.hasAttribute(VALUE_ATTRIBUTE);
		
		if ((hasRefAttribute && hasValueAttribute) ||
				((hasRefAttribute || hasValueAttribute) && subElement != null)) {
			//只允许ref or value or 集合属性
		}
		
		if(hasRefAttribute){
			String refName =ele.getAttribute(REF_ELEMENT);
			if(!StringUtils.hasText(refName)){
				
			}
			//RuntimeBeanRefernce
			RuntimeBeanReference ref =new RuntimeBeanReference(refName);
			return ref;
		}
		else if(hasValueAttribute){
			TypedStringValue valueHolder =new TypedStringValue(ele.getAttribute(VALUE_ATTRIBUTE));
			return valueHolder;
		}
		else if(subElement !=null){
			//暂时不支持解析sub props
			return null;
		}
		else{
			return null;
		}
	}

	public AbstractBeanDefinition parseBeanDefinitionAttributes(Element ele, String beanName,
			DmnBeanDefinition containingBean, AbstractBeanDefinition bd) {
		//目前仅支持单例
		if(ele.hasAttribute(SINGLETON_ATTRIBUTE)){
		}
		else if(ele.hasAttribute(SCOPE_ATTRIBUTE)){
			bd.setScope(ele.getAttribute(SCOPE_ATTRIBUTE));
		}
		else if(containingBean !=null){
			bd.setScope(containingBean.getScope());
		}
		return bd;
	}

	protected AbstractBeanDefinition createBeanDefinition(String className, String parentName)
			throws ClassNotFoundException {

		return BeanDefinitionReaderUtils.createBeanDefinition(
				parentName, className, this.readerContext.getBeanClassLoader());
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
	
	private boolean isCandidateElement(Node node) {
		return (node instanceof Element && (isDefaultNamespace(node) || !isDefaultNamespace(node.getParentNode())));
	}
}
