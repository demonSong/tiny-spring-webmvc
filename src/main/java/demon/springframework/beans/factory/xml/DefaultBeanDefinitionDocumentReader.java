package demon.springframework.beans.factory.xml;

import org.w3c.dom.Document;

public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDoucumentReader{

	private XmlReaderContext readerContext;

	@Override
	public void registerBeanDefinitions(Document doc,XmlReaderContext readerContext){
		this.readerContext =readerContext;
	}
	
	public XmlReaderContext getReaderContext() {
		return this.readerContext;
	}

}
