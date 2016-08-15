package demon.springframework.expression;

import org.springframework.expression.ParseException;

public interface ExpressionParser {

	Expression parseExpression(String expressionString) throws ParseException;

	Expression parseExpression(String expressionString,ParserContext context) throws ParseException;
	
}
