package demon.springframework.expression;

import org.springframework.expression.EvaluationException;

public interface Expression {
	
	Object getValue() throws EvaluationException;
	
	String getExpressionString();
}
