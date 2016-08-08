package demon.springframework.web.servlet.mvc.condition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import demon.springframework.web.context.bind.annotation.RequestMethod;

public final class RequestMethodsRequestCondition extends AbstractRequestCondition<RequestMethodsRequestCondition> {
	
	private final Set<RequestMethod> methods;
	
	public RequestMethodsRequestCondition(RequestMethod... requestMethods){
		this(asList(requestMethods));
	}
	
	private RequestMethodsRequestCondition(Collection<RequestMethod> requestMethods) {
		this.methods = Collections.unmodifiableSet(new LinkedHashSet<RequestMethod>(requestMethods));
	}
	
	private static List<RequestMethod> asList(RequestMethod... requestMethods) {
		return (requestMethods != null ? Arrays.asList(requestMethods) : Collections.<RequestMethod>emptyList());
	}
	
	public Set<RequestMethod> getMethods() {
		return this.methods;
	}

	@Override
	protected Collection<RequestMethod> getContent() {
		return this.methods;
	}


	@Override
	protected String getToStringInfix() {
		return " || ";
	}

	/**
	 * 占位符在这里起到了作用
	 */
	@Override
	public RequestMethodsRequestCondition combine(RequestMethodsRequestCondition other) {
		Set<RequestMethod> set = new LinkedHashSet<RequestMethod>(this.methods);
		set.addAll(other.methods);
		return new RequestMethodsRequestCondition(set);
	}

	@Override
	public RequestMethodsRequestCondition getMatchingCondition(HttpServletRequest request) {
		//匹配各种web请求
		if(this.methods.isEmpty()){
			return this;
		}
		//匹配与methods相同的web请求
		RequestMethod incomingRequestMethod =getRequestMethod(request);
		if(incomingRequestMethod !=null){
			for (RequestMethod method : this.methods) {
				if (method.equals(incomingRequestMethod)) {
					return new RequestMethodsRequestCondition(method);
				}
			}
		}
		//不匹配
		return null;
	}
	
	private RequestMethod getRequestMethod(HttpServletRequest request) {
		try {
			return RequestMethod.valueOf(request.getMethod());
		}
		catch (IllegalArgumentException ex) {
			return null;
		}
	}
}
