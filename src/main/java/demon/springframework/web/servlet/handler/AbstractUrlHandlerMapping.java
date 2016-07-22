package demon.springframework.web.servlet.handler;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.util.Assert;

import demon.springframework.web.util.UrlPathHelper;

/**
 * 所以说 这个类还是要封装成  applicationContext获得其中部分的功能；
 * @author demon.song
 *
 */
public abstract class AbstractUrlHandlerMapping extends AbstractHandlerMapping {
	
	//组合urlpathhelper中所暴露的一些方法,强大urlhandlerMapping类
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private Object rootHandler;
	
	private final Map<String, Object> handlerMap = new LinkedHashMap<String, Object>();

	public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
		this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
	}

	public void setUrlDecode(boolean urlDecode) {
		this.urlPathHelper.setUrlDecode(urlDecode);
	}

	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
		this.urlPathHelper = urlPathHelper;
	}
	
	public void setRootHandler(Object rootHandler) {
		this.rootHandler = rootHandler;
	}
	
	public Object getRootHandler() {
		return rootHandler;
	}
	
	public final Map<String, Object> getHandlerMap() {
		return Collections.unmodifiableMap(this.handlerMap);
	}
	
	@Override
	protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
		String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
		Object handler = lookupHandler(lookupPath, request);
		if (handler == null) {
			// We need to care for the default handler directly, since we need to
			// expose the PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE for it as well.
			Object rawHandler = null;
			if ("/".equals(lookupPath)) {
				rawHandler = getRootHandler();
			}
			if (rawHandler == null) {
				rawHandler = getDefaultHandler();
			}

			//这里面 为什么rawhandler 是 string 类型的?
			if (rawHandler != null) {
				if (rawHandler instanceof String) {
					String handlerName = (String) rawHandler;
					// rawHandler =
					// getApplicationContext().getBean(handlerName);
				}
			}
		}
		return handler;
	}

	
	protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
		// Direct match handlerMap 匹配urlPath String 和 controller的匹配
		Object handler = this.handlerMap.get(urlPath);
		//handler 为String 类型的主要原因在于handlerMap可能保存的是String String的键值对
		if (handler != null) {
			// Bean name or resolved handler?
			if (handler instanceof String) {
				String handlerName = (String) handler;
				// handler = getApplicationContext().getBean(handlerName);
			}
			return handler;
		}
		// Pattern match? 支持正则表达式
		
		// No handler found...
		return null;
	}
	
	protected void registerHandler(String urlPath, Object handler)throws BeansException, IllegalStateException {
		Assert.notNull(urlPath, "URL path must not be null");
		Assert.notNull(handler, "Handler object must not be null");

		Object resolvedHandler = handler;

		// Eagerly resolve handler if referencing singleton via name.
		if (handler instanceof String) {
			String handlerName = (String) handler;
			// if (getApplicationContext().isSingleton(handlerName)) {
			// resolvedHandler = getApplicationContext().getBean(handlerName);
			// }
		}
		Object mappedHandler = this.handlerMap.get(urlPath);
		if (mappedHandler != null) {
			if (mappedHandler != resolvedHandler) {
				throw new IllegalStateException("Cannot map "
						+ getHandlerDescription(handler) + " to URL path ["
						+ urlPath + "]: There is already "
						+ getHandlerDescription(mappedHandler) + " mapped.");
			}
		}
		else {
			if (urlPath.equals("/")) {
				setRootHandler(resolvedHandler);
			} 
			else if (urlPath.equals("/*")) {
				setDefaultHandler(resolvedHandler);
			} 
			else {
				this.handlerMap.put(urlPath, resolvedHandler);
			}
		}
	}
	
	private String getHandlerDescription(Object handler) {
		return "handler " + (handler instanceof String ? "'" + handler + "'" : "of type [" + handler.getClass() + "]");
	}
}
