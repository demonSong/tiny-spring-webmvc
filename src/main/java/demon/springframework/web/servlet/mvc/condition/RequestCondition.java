package demon.springframework.web.servlet.mvc.condition;

import javax.servlet.http.HttpServletRequest;

public interface RequestCondition<T> {

	T combine(T other);
	
	T getMatchingCondition(HttpServletRequest request);
	
}
