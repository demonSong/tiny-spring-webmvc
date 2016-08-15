package demon.springframework.expression.common;

import org.springframework.expression.EvaluationException;

import demon.springframework.expression.Expression;

public class CompositeStringExpression implements Expression{
	
	private final String expressionString;
	
	private final Expression[] expressions;
	
	public CompositeStringExpression(String expressionString, Expression[] expressions){
		this.expressionString =expressionString;
		this.expressions=expressions;
	}
	
	@Override
	public Object getValue() throws EvaluationException {
		return null;
	}

	@Override
	public String getExpressionString() {
		return this.expressionString;
	}

}
