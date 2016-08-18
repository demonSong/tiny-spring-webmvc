package demon.springframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.adapter.UnknownAdviceTypeException;

import demon.springframework.aop.Advisor;

public interface AdvisorAdapterRegistry {

	MethodInterceptor[] getInterceptors(Advisor advisor) throws UnknownAdviceTypeException;
}
