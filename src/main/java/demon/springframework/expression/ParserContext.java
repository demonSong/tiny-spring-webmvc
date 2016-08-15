package demon.springframework.expression;

public interface ParserContext {

	boolean isTemplate();

	String getExpressionPrefix();

	String getExpressionSuffix();
	
	public static final ParserContext TEMPLATE_EXPRESSION = new ParserContext() {

		@Override
		public String getExpressionPrefix() {
			return "#{";
		}

		@Override
		public String getExpressionSuffix() {
			return "}";
		}

		@Override
		public boolean isTemplate() {
			return true;
		}

	};
}
