package demon.springframework.web.servlet.mvc.method;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import demon.springframework.web.servlet.handler.AbstractHandlerMethodMapping;

public abstract class RequestMappingInfoHandlerMapping extends AbstractHandlerMethodMapping<RequestMappingInfo>{

	/**
	 * 问题根源在于解决:如何判断是何种类型参数?eg.是post请求 or get请求?
	 * 可以根据request 生成对应的基于HTTP请求的mapping
	 * 变成一个真正可用的requestMappingInfo
	 */
	@Override
	protected RequestMappingInfo getMatchingMapping(RequestMappingInfo info, HttpServletRequest request) {
		return info.getMatchingCondition(request);
	}
	
	@Override
	protected Set<String> getMappingPathPatterns(RequestMappingInfo info) {
		return info.getPatternsCondition().getPatterns();
	}
	
}
