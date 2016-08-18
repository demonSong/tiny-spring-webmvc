package demon.springframework.aop.framework;

import java.lang.reflect.Method;
import java.util.List;

public interface AdvisorChainFactory {
	
	List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised config, Method method, Class<?> targetClass);

}
