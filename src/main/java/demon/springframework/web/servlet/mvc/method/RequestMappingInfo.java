package demon.springframework.web.servlet.mvc.method;

import javax.servlet.http.HttpServletRequest;

import demon.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import demon.springframework.web.servlet.mvc.condition.RequestCondition;
import demon.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;

public final class RequestMappingInfo implements RequestCondition<RequestMappingInfo> {
	
	private final PatternsRequestCondition patternsCondition;

	private final RequestMethodsRequestCondition methodsCondition;
	
	public RequestMappingInfo(PatternsRequestCondition patterns, RequestMethodsRequestCondition methods) {

		this.patternsCondition = (patterns != null ? patterns : new PatternsRequestCondition());
		this.methodsCondition = (methods != null ? methods : new RequestMethodsRequestCondition());
	}
	
	public PatternsRequestCondition getPatternsCondition() {
		return this.patternsCondition;
	}

	public RequestMethodsRequestCondition getMethodsCondition() {
		return this.methodsCondition;
	}
	
	@Override
	public RequestMappingInfo combine(RequestMappingInfo other) {
		PatternsRequestCondition patterns =this.patternsCondition.combine(other.patternsCondition);
		RequestMethodsRequestCondition methods =this.methodsCondition.combine(other.methodsCondition);
		return new RequestMappingInfo(patterns, methods);
	}
	
	/**
	 * 这个方法的作用是什么?
	 */
	@Override
	public RequestMappingInfo getMatchingCondition(HttpServletRequest request) {
		RequestMethodsRequestCondition methods =this.methodsCondition.getMatchingCondition(request);
		if (methods == null) {
			return null;
		}
		PatternsRequestCondition patterns = this.patternsCondition.getMatchingCondition(request);
		if (patterns == null) {
			return null;
		}
		
		return new RequestMappingInfo(patterns, methods);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj != null && obj instanceof RequestMappingInfo) {
			RequestMappingInfo other = (RequestMappingInfo) obj;
			return (this.patternsCondition.equals(other.patternsCondition) &&
					this.methodsCondition.equals(other.methodsCondition));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (this.patternsCondition.hashCode() * 31 +  // primary differentiation
				this.methodsCondition.hashCode());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");
		builder.append(this.patternsCondition);
		builder.append(",methods=").append(this.methodsCondition);
		builder.append('}');
		return builder.toString();
	}
	
}
