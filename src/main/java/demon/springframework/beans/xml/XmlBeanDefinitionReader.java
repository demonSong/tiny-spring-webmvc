package demon.springframework.beans.xml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import demon.springframework.BeanReference;
import demon.springframework.beans.AbstractBeanDefinitionReader;
import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.PropertyValue;
import demon.springframework.beans.io.ResourceLoader;

/**
 * @author yihua.huang@dianping.com
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

	public XmlBeanDefinitionReader(ResourceLoader resourceLoader) {
		super(resourceLoader);
	}

	@Override
	public void loadBeanDefinitions(String location) throws Exception {
		InputStream inputStream = getResourceLoader().getResource(location).getInputStream();
		doLoadBeanDefinitions(inputStream);
	}

	protected void doLoadBeanDefinitions(InputStream inputStream) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		Document doc = docBuilder.parse(inputStream);
		// 解析bean
		registerBeanDefinitions(doc);
		inputStream.close();
	}

	public void registerBeanDefinitions(Document doc) {
		Element root = doc.getDocumentElement();

		parseBeanDefinitions(root);
	}

	protected void parseBeanDefinitions(Element root) {
		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element) {
				Element ele = (Element) node;
				processBeanDefinition(ele);
			}
		}
	}

	protected void processBeanDefinition(Element ele) {
		String name = ele.getAttribute("id");
		String className = ele.getAttribute("class");
		//判断下 如果name 和 className为空的话返回
		if(name.equals("") || className.equals("")){
			return;
		}
		BeanDefinition beanDefinition = new BeanDefinition();
		processProperty(ele, beanDefinition);
		beanDefinition.setBeanClassName(className);
		getRegistry().put(name, beanDefinition);
	}

	//需要一套更完善的xml node文件的解析
	private void processProperty(Element ele, BeanDefinition beanDefinition) {
		NodeList propertyNode = ele.getElementsByTagName("property");
		for (int i = 0; i < propertyNode.getLength(); i++) {
			Node node = propertyNode.item(i);
			if (node instanceof Element) {
				Element propertyEle = (Element) node;
				//获得了对应的set方法的名字
				String name = propertyEle.getAttribute("name");
				//解析策略需要判断是否存在value和子节点
				String value = propertyEle.getAttribute("value");
				//解析是否为ref属性
				String ref =propertyEle.getAttribute("ref");
				//判断该节点是否还有子节点
				NodeList props =propertyEle.getElementsByTagName("props");
				if (value != null && value.length() > 0) {
					beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
				} 
				else if(ref !=null && ref.length() >0){//value的值为空,则需要判断是否是ref
					//封装了一个统一的引用对象,来处理propertyValue
					BeanReference beanReference = new BeanReference(ref);
					beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));
				}
				else if(props !=null){//如果还存在子节点,就需处理property的子节点,最好还是根据name来判断来得可靠一些
					processProps(props,beanDefinition,name);
				}
				else{
					throw new IllegalArgumentException("Configuration problem: <property> element for property '"
							+ name + "' must specify a ref or value");
				}
			}
		}
	}
	
	//处理props数组
	private void processProps(NodeList props,BeanDefinition beanDefinition,String name){
		for (int i = 0; i < props.getLength(); i++) {
			Node node = props.item(i);
			//针对每一个节点,又需要for循环进行处理字节点
			if (node instanceof Element) {
				Element propsEle = (Element) node;
				NodeList prop =propsEle.getElementsByTagName("prop");
				Map<String, Object> urlMap =new HashMap<String, Object>();
				for (int j = 0; j < prop.getLength(); j++) {
					Node pNode = prop.item(j);
					//针对每一个节点,又需要for循环进行处理字节点
					if (pNode instanceof Element) {
						Element propEle = (Element) pNode;
						//获得了对应的set方法的名字,并且明确了是对应的键值对
						String key = propEle.getAttribute("key");
						String value =propEle.getFirstChild().getTextContent();
						if (value != null && value.length() > 0) {
							//封装一个object对象 ? 统一的键值对数据格式来得靠谱
							urlMap.put(key, value);
						}else {
							throw new IllegalArgumentException("Configuration problem: <prop> element for prop '"
									+ key + "' must specify a TextContent value");
						}
					}
				}
				beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, urlMap));
			}
		}
	}
}
