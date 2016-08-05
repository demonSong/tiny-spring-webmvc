package demon.springframework.web.servlet.handler;

import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;

import demon.springframework.beans.factory.InitializingBean;

public abstract class AbstractHandlerMethodMapping<T> extends
		AbstractHandlerMapping implements InitializingBean {

	// 所获得的applicationContext是classpathcontext继承了abstractbeanfactory的IOC水桶

	// 封装了一个getBeanNamesForType的方法来获得所有的bean

	@Override
	public void afterPropertiesSet() {
		initHandlerMethods();
	}

	protected void initHandlerMethods() {
		String[] beanNames = getApplicationContext().getBeanNamesForType(
				Object.class);
		for(String beanName : beanNames){
			if(isHandler(getApplicationContext().getType(beanName))){
				detectHandlerMerhods(beanName);
			}
		}
	}

	protected void detectHandlerMerhods(final Object handler) {
		Class<?> handlerType =
				(handler instanceof String ? getApplicationContext().getType((String) handler) : handler.getClass());
		final Map<Method, T> mappings = new IdentityHashMap<Method, T>();
		final Class<?> userType = ClassUtils.getUserClass(handlerType);

	}

	@Override
	protected HandlerMethod getHandlerInternal(HttpServletRequest request)
			throws Exception {
		return null;
	}

	protected abstract boolean isHandler(Class<?> beanType);

}
