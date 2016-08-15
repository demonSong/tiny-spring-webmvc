package demon.springframework.context.expression;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

import demon.springframework.beans.factory.config.BeanExpressionContext;
import demon.springframework.beans.factory.config.BeanExpressionResolver;
import demon.springframework.expression.Expression;
import demon.springframework.expression.ExpressionParser;
import demon.springframework.expression.ParserContext;
import demon.springframework.expression.spel.standard.SpelExpressionParser;

public class StandardBeanExpressionResolver implements BeanExpressionResolver{
	
	public static final String DEFAULT_EXPRESSION_PREFIX = "#{";

	/** Default expression suffix: "}" */
	public static final String DEFAULT_EXPRESSION_SUFFIX = "}";


	private String expressionPrefix = DEFAULT_EXPRESSION_PREFIX;

	private String expressionSuffix = DEFAULT_EXPRESSION_SUFFIX;

	private final Map<String, Expression> expressionCache = new ConcurrentHashMap<String, Expression>(256);
	
	private ExpressionParser expressionParser = new SpelExpressionParser();
	
	private final ParserContext beanExpressionParserContext = new ParserContext() {
		@Override
		public boolean isTemplate() {
			return true;
		}
		@Override
		public String getExpressionPrefix() {
			return expressionPrefix;
		}
		@Override
		public String getExpressionSuffix() {
			return expressionSuffix;
		}
	};
	
	@Override
	public Object evalute(String value, BeanExpressionContext evalContext)
			throws BeansException {
		if (!StringUtils.hasLength(value)) {
			return value;
		}
		
		Expression expr =this.expressionCache.get(value);
		if(expr ==null){
			expr=this.expressionParser.parseExpression(value, this.beanExpressionParserContext);
			this.expressionCache.put(value, expr);
		}
		
		return expr.getValue();
	}

	
}
