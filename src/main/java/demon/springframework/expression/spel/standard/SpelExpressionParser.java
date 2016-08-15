package demon.springframework.expression.spel.standard;

import org.springframework.expression.ParseException;

import demon.springframework.expression.Expression;
import demon.springframework.expression.ParserContext;
import demon.springframework.expression.common.TemplateAwareExpressionParser;

public class SpelExpressionParser extends TemplateAwareExpressionParser{
	

	@Override
	protected Expression doParseExpression(String expressionString,
			ParserContext context) throws ParseException {
		return null;
	}

}
