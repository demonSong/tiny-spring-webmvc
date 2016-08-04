package demon.springframework.context.annotation;

import java.util.Set;

import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.factory.config.BeanDefinitionHolder;
import demon.springframework.beans.factory.xml.BeanDefinitionParser;
import demon.springframework.beans.factory.xml.ParserContext;
import demon.springframework.beans.factory.xml.XmlReaderContext;
import demon.springframework.context.ConfigurableApplicationContext;

public class ComponentScanBeanDefinitionParser implements BeanDefinitionParser{
	
	private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		
		String[] basePackages = StringUtils.tokenizeToStringArray(element.getAttribute(BASE_PACKAGE_ATTRIBUTE),
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		// Actually scan for bean definitions and register them.
		ClassPathBeanDefinitionScanner scanner = configureScanner(parserContext, element);
		Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackages);
		return null;
	}

	protected ClassPathBeanDefinitionScanner configureScanner(ParserContext parserContext, Element element) {
		XmlReaderContext readerContext =parserContext.getReaderContext();
		
		boolean useDefaultFilters = true;
		ClassPathBeanDefinitionScanner scanner =createScanner(readerContext, useDefaultFilters);
		return scanner;
	}
	
	protected ClassPathBeanDefinitionScanner createScanner(XmlReaderContext readerContext, boolean useDefaultFilters) {
		return new ClassPathBeanDefinitionScanner(readerContext.getRegistry(), useDefaultFilters);
	}
}
