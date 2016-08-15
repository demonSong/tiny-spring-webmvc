package demon.springframework.expression.common;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.springframework.expression.ParseException;

import demon.springframework.expression.Expression;
import demon.springframework.expression.ExpressionParser;
import demon.springframework.expression.ParserContext;

public abstract class TemplateAwareExpressionParser implements ExpressionParser{

	/**
	 * Default ParserContext instance for non-template expressions.
	 */
	private static final ParserContext NON_TEMPLATE_PARSER_CONTEXT = new ParserContext() {

		@Override
		public String getExpressionPrefix() {
			return null;
		}

		@Override
		public String getExpressionSuffix() {
			return null;
		}

		@Override
		public boolean isTemplate() {
			return false;
		}
	};
	
	@Override
	public Expression parseExpression(String expressionString) throws ParseException {
		return parseExpression(expressionString, NON_TEMPLATE_PARSER_CONTEXT);
	}
	
	@Override
	public Expression parseExpression(String expressionString, ParserContext context)
			throws ParseException {
		if (context == null) {
			context = NON_TEMPLATE_PARSER_CONTEXT;
		}

		if (context.isTemplate()) {
			return parseTemplate(expressionString, context);
		}
		else {
			return doParseExpression(expressionString, context);
		}
	}
	
	private Expression parseTemplate(String expressionString, ParserContext context)
			throws ParseException {
		if (expressionString.length() == 0) {
			return new LiteralExpression("");
		}
		Expression[] expressions = parseExpressions(expressionString, context);
		if (expressions.length == 1) {
			return expressions[0];
		}
		else {
			return new CompositeStringExpression(expressionString, expressions);
		}
	}
	
	private Expression[] parseExpressions(String expressionString, ParserContext context)
			throws ParseException {
		List<Expression> expressions = new LinkedList<Expression>();
		String prefix = context.getExpressionPrefix();
		String suffix = context.getExpressionSuffix();
		int startIdx = 0;
		while (startIdx < expressionString.length()) {
			int prefixIndex = expressionString.indexOf(prefix, startIdx);
			if (prefixIndex >= startIdx) {
				// an inner expression was found - this is a composite
				if (prefixIndex > startIdx) {
					expressions.add(createLiteralExpression(context,
							expressionString.substring(startIdx, prefixIndex)));
				}
				int afterPrefixIndex = prefixIndex + prefix.length();
				int suffixIndex = skipToCorrectEndSuffix(prefix, suffix,
						expressionString, afterPrefixIndex);

				if (suffixIndex == -1) {
					throw new ParseException(expressionString, prefixIndex,
							"No ending suffix '" + suffix
									+ "' for expression starting at character "
									+ prefixIndex + ": "
									+ expressionString.substring(prefixIndex));
				}

				if (suffixIndex == afterPrefixIndex) {
					throw new ParseException(expressionString, prefixIndex,
							"No expression defined within delimiter '" + prefix + suffix
									+ "' at character " + prefixIndex);
				}

				String expr = expressionString.substring(prefixIndex + prefix.length(),
						suffixIndex);
				expr = expr.trim();

				if (expr.length() == 0) {
					throw new ParseException(expressionString, prefixIndex,
							"No expression defined within delimiter '" + prefix + suffix
									+ "' at character " + prefixIndex);
				}

				expressions.add(doParseExpression(expr, context));
				startIdx = suffixIndex + suffix.length();
			}
			else {
				// no more ${expressions} found in string, add rest as static text
				expressions.add(createLiteralExpression(context,
						expressionString.substring(startIdx)));
				startIdx = expressionString.length();
			}
		}
		return expressions.toArray(new Expression[expressions.size()]);
	}
	
	private int skipToCorrectEndSuffix(String prefix, String suffix,
			String expressionString, int afterPrefixIndex) throws ParseException {
		// Chew on the expression text - relying on the rules:
		// brackets must be in pairs: () [] {}
		// string literals are "..." or '...' and these may contain unmatched brackets
		int pos = afterPrefixIndex;
		int maxlen = expressionString.length();
		int nextSuffix = expressionString.indexOf(suffix, afterPrefixIndex);
		if (nextSuffix == -1) {
			return -1; // the suffix is missing
		}
		Stack<Bracket> stack = new Stack<Bracket>();
		while (pos < maxlen) {
			if (isSuffixHere(expressionString, pos, suffix) && stack.isEmpty()) {
				break;
			}
			char ch = expressionString.charAt(pos);
			switch (ch) {
				case '{':
				case '[':
				case '(':
					stack.push(new Bracket(ch, pos));
					break;
				case '}':
				case ']':
				case ')':
					if (stack.isEmpty()) {
						throw new ParseException(expressionString, pos, "Found closing '"
								+ ch + "' at position " + pos + " without an opening '"
								+ Bracket.theOpenBracketFor(ch) + "'");
					}
					Bracket p = stack.pop();
					if (!p.compatibleWithCloseBracket(ch)) {
						throw new ParseException(expressionString, pos, "Found closing '"
								+ ch + "' at position " + pos
								+ " but most recent opening is '" + p.bracket
								+ "' at position " + p.pos);
					}
					break;
				case '\'':
				case '"':
					// jump to the end of the literal
					int endLiteral = expressionString.indexOf(ch, pos + 1);
					if (endLiteral == -1) {
						throw new ParseException(expressionString, pos,
								"Found non terminating string literal starting at position "
										+ pos);
					}
					pos = endLiteral;
					break;
			}
			pos++;
		}
		if (!stack.isEmpty()) {
			Bracket p = stack.pop();
			throw new ParseException(expressionString, p.pos, "Missing closing '"
					+ Bracket.theCloseBracketFor(p.bracket) + "' for '" + p.bracket
					+ "' at position " + p.pos);
		}
		if (!isSuffixHere(expressionString, pos, suffix)) {
			return -1;
		}
		return pos;
	}
	
	private boolean isSuffixHere(String expressionString, int pos, String suffix) {
		int suffixPosition = 0;
		for (int i = 0; i < suffix.length() && pos < expressionString.length(); i++) {
			if (expressionString.charAt(pos++) != suffix.charAt(suffixPosition++)) {
				return false;
			}
		}
		if (suffixPosition != suffix.length()) {
			// the expressionString ran out before the suffix could entirely be found
			return false;
		}
		return true;
	}
	
	private Expression createLiteralExpression(ParserContext context, String text) {
		return new LiteralExpression(text);
	}
	
	private static class Bracket {

		char bracket;

		int pos;

		Bracket(char bracket, int pos) {
			this.bracket = bracket;
			this.pos = pos;
		}

		boolean compatibleWithCloseBracket(char closeBracket) {
			if (this.bracket == '{') {
				return closeBracket == '}';
			}
			else if (this.bracket == '[') {
				return closeBracket == ']';
			}
			return closeBracket == ')';
		}

		static char theOpenBracketFor(char closeBracket) {
			if (closeBracket == '}') {
				return '{';
			}
			else if (closeBracket == ']') {
				return '[';
			}
			return '(';
		}

		static char theCloseBracketFor(char openBracket) {
			if (openBracket == '{') {
				return '}';
			}
			else if (openBracket == '[') {
				return ']';
			}
			return ')';
		}
	}
	
	protected abstract Expression doParseExpression(String expressionString,
			ParserContext context) throws ParseException;
	
}
