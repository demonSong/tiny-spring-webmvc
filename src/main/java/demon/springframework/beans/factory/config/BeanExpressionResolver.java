package demon.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public interface BeanExpressionResolver {
	
	Object evalute(String value ,BeanExpressionContext evalContext) throws BeansException;

}
