package demon.springframework.aop.framework.adapter;

import java.util.ArrayList;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.adapter.UnknownAdviceTypeException;

import demon.springframework.aop.Advisor;

public class DefaultAdvisorAdapterRegistry implements AdvisorAdapterRegistry {

	@Override
	public MethodInterceptor[] getInterceptors(Advisor advisor)
			throws UnknownAdviceTypeException {
		List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>(3);
		Advice advice = advisor.getAdvice();
		if (advice instanceof MethodInterceptor) {
			interceptors.add((MethodInterceptor) advice);
		}
		if (interceptors.isEmpty()) {
			throw new UnknownAdviceTypeException(advisor.getAdvice());
		}
		return interceptors.toArray(new MethodInterceptor[interceptors.size()]);
	}

}
