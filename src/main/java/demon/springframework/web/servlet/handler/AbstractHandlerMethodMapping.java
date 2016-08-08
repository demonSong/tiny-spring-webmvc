package demon.springframework.web.servlet.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.ClassUtils;

import demon.springframework.beans.factory.InitializingBean;
import demon.springframework.core.util.ReflectionUtils.MethodFilter;
import demon.springframework.util.LinkedMultiValueMap;
import demon.springframework.util.MultiValueMap;
import demon.springframework.web.method.HandlerMethod;
import demon.springframework.web.method.HandlerMethodSelector;
import demon.springframework.web.servlet.HandlerMapping;

public abstract class AbstractHandlerMethodMapping<T> extends
		AbstractHandlerMapping implements InitializingBean {

	// 所获得的applicationContext是classpathcontext继承了abstractbeanfactory的IOC水桶

	// 封装了一个getBeanNamesForType的方法来获得所有的bean
	
	private final Map<T, HandlerMethod> handlerMethods = new LinkedHashMap<T, HandlerMethod>();
	
	private final MultiValueMap<String, T> urlMap = new LinkedMultiValueMap<String, T>();

	public Map<T, HandlerMethod> getHandlerMethods() {
		return Collections.unmodifiableMap(this.handlerMethods);
	}
	
	@Override
	public void afterPropertiesSet() {
		initHandlerMethods();
	}

	protected void initHandlerMethods() {
		String[] beanNames = getApplicationContext().getBeanNamesForType(
				Object.class);
		for(String beanName : beanNames){
			if(isHandler(getApplicationContext().getType(beanName))){
				//search all has controller and requestMapping annotation class 
				//do detectHandlerMerhods
				detectHandlerMerhods(beanName);
			}
		}
	}

	protected void detectHandlerMerhods(final Object handler) {
		Class<?> handlerType =
				(handler instanceof String ? getApplicationContext().getType((String) handler) : handler.getClass());
		final Map<Method, T> mappings = new IdentityHashMap<Method, T>();
		final Class<?> userType = ClassUtils.getUserClass(handlerType);
		//匿名内部类的好处 在于可以直接访问该类内部的一些信息,局部信息和全局信息在其作用域里均可以访问
		//定义非内部类,而是new出一个实例时,只能访问类的全局信息
		//filter类似于策略模式
		Set<Method> methods = HandlerMethodSelector.selectMethods(userType, new MethodFilter() {
			@Override
			public boolean matches(Method method) {
				//获得符合map的methods,并增加过滤器,筛选不符合条件的method
				//过滤出没有requestMap修饰的方法
				T mapping =getMappingForMethod(method, userType);
				if(mapping !=null){
					mappings.put(method,mapping);
					return true;
				}
				else{
					return false;
				}
			}
		});
		
		for(Method method : methods){
			registerHandlerMethod(handler, method, mappings.get(method));
		}
		
	}

	@Override
	protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
		String lookupPath =getUrlPathHelper().getLookupPathForRequest(request);
		HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
		return (handlerMethod != null ? handlerMethod.createWithResolvedBean() : null);
	}
	
	protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
		List<Match> matches = new ArrayList<Match>();
		List<T> directPathMatches = this.urlMap.get(lookupPath);
		//又做了一层分类
		if (directPathMatches != null) {
			addMatchingMappings(directPathMatches, matches, request);
		}
		if (matches.isEmpty()) {
			// No choice but to go through all mappings...
			addMatchingMappings(this.handlerMethods.keySet(), matches, request);
		}
		if(!matches.isEmpty()){
			//排序算法来算出最佳的match
			Match bestMatch =matches.get(0);
			//走处理函数
			handleMatch(bestMatch.mapping,lookupPath,request);
			return bestMatch.handlerMethod;
		}
		else {
			return handleNoMatch(handlerMethods.keySet(), lookupPath, request);
		}
	}

	protected HandlerMethod handleNoMatch(Set<T> mappings, String lookupPath, HttpServletRequest request)
			throws Exception {

		return null;
	}
	
	private void handleMatch(T mapping, String lookupPath,
			HttpServletRequest request) {
		request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, lookupPath);
	}

	private void addMatchingMappings(Collection<T> mappings, List<Match> matches, HttpServletRequest request) {
		for (T mapping : mappings) {
			T match = getMatchingMapping(mapping, request);
			if (match != null) {
				matches.add(new Match(match, this.handlerMethods.get(mapping)));
			}
		}
	}

	protected abstract T getMatchingMapping(T mapping, HttpServletRequest request);
	
	protected abstract T getMappingForMethod(Method method, Class<?> handlerType);
	
	protected abstract boolean isHandler(Class<?> beanType);

	/**
	 * url映射已然变成一种数据结构,这种数据结构由统一接口requestCondition来管理特性
	 * 该函数主要是用来映射bean和mapping之间的关系
	 * @param handler
	 * @param method
	 * @param mapping
	 * patterns便是requestMapping所定义的value值
	 */
	protected void registerHandlerMethod(Object handler, Method method, T mapping) {
		HandlerMethod newHandlerMethod =createHandlerMethod(handler,method);
		//缓存
		HandlerMethod oldHandlerMethod = this.handlerMethods.get(mapping);
		if (oldHandlerMethod != null && !oldHandlerMethod.equals(newHandlerMethod)) {
			throw new IllegalStateException("防止一个mapping注册多个方法--有待测试");
		}
		this.handlerMethods.put(mapping, newHandlerMethod);
		Set<String> patterns = getMappingPathPatterns(mapping);
		for (String pattern : patterns) {
			if (!getPathMatcher().isPattern(pattern)) {
				this.urlMap.add(pattern, mapping);
			}
		}
	}
	
	
	protected abstract Set<String> getMappingPathPatterns(T mapping);

	protected HandlerMethod createHandlerMethod(Object handler, Method method) {
		HandlerMethod handlerMethod;
		if(handler instanceof String){
			String beanName =(String) handler;
			handlerMethod =new HandlerMethod(beanName, getApplicationContext(),method);
		}
		else{
			handlerMethod =new HandlerMethod(handler,method);
		}
		return handlerMethod;
	}
	
	private class Match {

		private final T mapping;

		private final HandlerMethod handlerMethod;

		public Match(T mapping, HandlerMethod handlerMethod) {
			this.mapping = mapping;
			this.handlerMethod = handlerMethod;
		}

		@Override
		public String toString() {
			return this.mapping.toString();
		}
	}
}
