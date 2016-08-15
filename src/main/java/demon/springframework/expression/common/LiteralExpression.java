package demon.springframework.expression.common;

import org.springframework.expression.EvaluationException;

import demon.springframework.expression.Expression;

public class LiteralExpression implements Expression{
	
	private final String literalValue;
	
	public LiteralExpression(String literalValue) {
		this.literalValue =literalValue;
	}

	@Override
	public Object getValue() throws EvaluationException {
		return this.literalValue;
	}

	@Override
	public String getExpressionString() {
		return this.literalValue;
	}

}
