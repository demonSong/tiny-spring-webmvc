package demon.caucho.hessian.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import demon.caucho.hessian.io.AbstractHessianInput;
import demon.caucho.hessian.io.AbstractHessianOutput;
import demon.caucho.hessian.io.SerializerFactory;

public class HessianSkeleton extends AbstractSkeleton {

	private Object _service;

	public HessianSkeleton(Object service, Class<?> apiClass) {
		super(apiClass);

		if (service == null) {
			service = this;
		}
		this._service = service;

		if (!apiClass.isAssignableFrom(service.getClass())) {
			throw new IllegalArgumentException("Service " + service
					+ " must be an instance of " + apiClass.getName());
		}
	}

	public void invoke(InputStream is, OutputStream os) throws Exception {
		invoke(is, os, null);
	}

	public void invoke(InputStream is, OutputStream os,
			SerializerFactory serializerFactory) throws Exception {

	}

	public void invoke(AbstractHessianInput in, AbstractHessianOutput out)
			throws Exception {
		invoke(_service, in, out);
	}

	/**
	 * Invoke the object with the request from the input stream.
	 *
	 * @param in
	 *            the Hessian input stream
	 * @param out
	 *            the Hessian output stream
	 */
	public void invoke(Object service, AbstractHessianInput in,
			AbstractHessianOutput out) throws Exception {

		String methodName = in.readMethod();
		int argLength = in.readMethodArgLength();

		Method method;
		method = getMethod(methodName + "__" + argLength);
		
		if(method == null){
			method = getMethod(methodName);
		}
		
		Class<?>[] args =method.getParameterTypes();
		if (argLength != args.length && argLength >= 0) {
		      out.close();
		      return;
		}
		
		Object[] values = new Object[args.length];
		
		for(int i = 0; i<args.length; i++){
			values[i] =in.readObject(args[i]);
		}
		
		Object result = null;
		
		result =method.invoke(service, values);
		
	}

}
