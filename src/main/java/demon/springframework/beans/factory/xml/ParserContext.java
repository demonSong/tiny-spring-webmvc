package demon.springframework.beans.factory.xml;

public final class ParserContext {
	
	private final XmlReaderContext readerContext;
	
	private final BeanDefinitionParserDelegate delegate;
	
	public ParserContext(XmlReaderContext readerContext, BeanDefinitionParserDelegate delegate) {
		this.readerContext = readerContext;
		this.delegate = delegate;
	}
	
	public final BeanDefinitionParserDelegate getDelegate() {
		return this.delegate;
	}
	
	public XmlReaderContext getReaderContext() {
		return this.readerContext;
	}
}
