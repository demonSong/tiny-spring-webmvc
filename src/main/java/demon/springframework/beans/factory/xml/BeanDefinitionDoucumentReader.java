package demon.springframework.beans.factory.xml;

import org.w3c.dom.Document;

public interface BeanDefinitionDoucumentReader {
	
	void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) throws Exception;

}
