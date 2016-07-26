package demon.springframework.web.servlet.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.util.CollectionUtils;


/**
 * 实际是handlerMapping是作为配置文件进行写入的
 * 在tomcat进行初始化时,这个类是必须要跟着服务一起进行初始化的操作的
 * @author demon.song
 *
 */
public class SimpleUrlHandlerMapping extends AbstractUrlHandlerMapping{
	
	private final Map<String, Object> urlMap =new HashMap<String, Object>();
	
	
	public void setMappings(Properties mappings) {
		CollectionUtils.mergePropertiesIntoMap(mappings, this.urlMap);
	}
	
	public void setUrlMap(Map<String, ?> urlMap) {
		this.urlMap.putAll(urlMap);
	}
	
	public Map<String, ?> getUrlMap() {
		return this.urlMap;
	}
	
	@Override
	public void initApplicationContext() throws BeansException {
		super.initApplicationContext();
		registerHandlers(this.urlMap);
	}
	
	protected void registerHandlers(Map<String, Object> urlMap) throws BeansException {
		if (urlMap.isEmpty()) {
			//do something logger
		}
		else {
			for (Map.Entry<String, Object> entry : urlMap.entrySet()) {
				String url = entry.getKey();
				Object handler = entry.getValue();
				// Prepend with slash if not already present.
				if (!url.startsWith("/")) {
					url = "/" + url;
				}
				// Remove whitespace from handler bean name.
				if (handler instanceof String) {
					handler = ((String) handler).trim();
				}
				registerHandler(url, handler);
			}
		}
	}
	
}

