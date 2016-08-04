package demon.springframework.context.config;

import demon.springframework.beans.factory.xml.NamespaceHandlerSupport;
import demon.springframework.context.annotation.ComponentScanBeanDefinitionParser;

public class ContextNamespaceHandler extends NamespaceHandlerSupport{

	public void init() {
		registerBeanDefinitionParser("component-scan", new ComponentScanBeanDefinitionParser());
	}

}
