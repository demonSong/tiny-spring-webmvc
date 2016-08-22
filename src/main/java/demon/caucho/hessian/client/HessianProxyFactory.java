package demon.caucho.hessian.client;

import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;

import demon.caucho.hessian.io.AbstractHessianOutput;
import demon.caucho.hessian.io.HessianOutput;
import demon.caucho.hessian.io.SerializerFactory;
import demon.caucho.services.client.ServiceProxyFactory;

/**
 * 创建 Hessian client stubs
 * 
 * @author demon.song
 *
 */
public class HessianProxyFactory implements ServiceProxyFactory {

	private final ClassLoader _loader;

	private String _basicAuth;

	private boolean _isOverloadEnabled = false;

	private SerializerFactory _serializerFactory;

	private HessianConnectionFactory _connFactory;

	private boolean _isHessian2Reply = true;
	private boolean _isHessian2Request = false;

	private long _connectTimeout = -1;
	private long _readTimeout = -1;

	public HessianProxyFactory() {
		this(Thread.currentThread().getContextClassLoader());
	}

	/**
	 * The socket connection timeout in milliseconds.
	 */
	public long getConnectTimeout() {
		return _connectTimeout;
	}

	/**
	 * The socket connect timeout in milliseconds.
	 */
	public void setConnectTimeout(long timeout) {
		_connectTimeout = timeout;
	}

	/**
	 * The socket timeout on requests in milliseconds.
	 */
	public long getReadTimeout() {
		return _readTimeout;
	}

	/**
	 * The socket timeout on requests in milliseconds.
	 */
	public void setReadTimeout(long timeout) {
		_readTimeout = timeout;
	}

	public void setConnectionFactory(HessianConnectionFactory factory) {
		_connFactory = factory;
	}

	public HessianConnectionFactory getConnectionFactory() {
		if (_connFactory == null) {
			_connFactory = createHessianConnectionFactory();
			_connFactory.setHessianProxyFactory(this);
		}

		return _connFactory;
	}

	public String getBasicAuth() {
		if (_basicAuth != null) {
			return _basicAuth;
		} else {
			return null;
		}
	}

	/**
	 * True if the proxy can read Hessian 2 responses.
	 */
	public void setHessian2Reply(boolean isHessian2) {
		_isHessian2Reply = isHessian2;
	}

	/**
	 * True if the proxy should send Hessian 2 requests.
	 */
	public void setHessian2Request(boolean isHessian2) {
		_isHessian2Request = isHessian2;

		if (isHessian2)
			_isHessian2Reply = true;
	}

	public AbstractHessianOutput getHessianOutput(OutputStream os) {
		AbstractHessianOutput out;

		if (_isHessian2Request)
			out = null;
		else {
			HessianOutput out1 = new HessianOutput(os);
			out = out1;

			if (_isHessian2Reply)
				out1.setVersion(2);
		}

		out.setSerializerFactory(getSerializerFactory());
		return out;
	}

	public void setSerializerFactory(SerializerFactory factory) {
		_serializerFactory = factory;
	}

	/**
	 * Gets the serializer factory.
	 */
	public SerializerFactory getSerializerFactory() {
		if (_serializerFactory == null)
			_serializerFactory = new SerializerFactory(_loader);

		return _serializerFactory;
	}

	protected HessianConnectionFactory createHessianConnectionFactory() {
		String className = System.getProperty(HessianConnectionFactory.class
				.getName());

		HessianConnectionFactory factory = null;

		try {
			if (className != null) {
				ClassLoader loader = Thread.currentThread()
						.getContextClassLoader();

				Class<?> cl = Class.forName(className, false, loader);

				factory = (HessianConnectionFactory) cl.newInstance();

				return factory;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return new HessianURLConnectionFactory();
	}

	public HessianProxyFactory(ClassLoader loader) {
		_loader = loader;
	}

	public void setOverloadEnabled(boolean isOverloadEnabled) {
		_isOverloadEnabled = isOverloadEnabled;
	}

	public boolean isOverloadEnabled() {
		return _isOverloadEnabled;
	}

	public Object create(Class api, String urlName)
			throws MalformedURLException {
		return create(api, urlName, _loader);
	}

	public Object create(Class<?> api, String urlName, ClassLoader loader)
			throws MalformedURLException {
		URL url = new URL(urlName);

		return create(api, url, loader);
	}

	public Object create(Class<?> api, URL url, ClassLoader loader) {
		if (api == null)
			throw new NullPointerException(
					"api must not be null for HessianProxyFactory.create()");
		InvocationHandler handler = null;

		handler = new HessianProxy(url, this, api);

		// 该函数的初始化含义是:当调用每个类时,其实是接口所定义的方法,自然会调用handler的invoke方法
		// 值得注意的一个细节是,所有Proxy生成的代理对象都不算是一个真正意义是的实现类
		// 实现类需要invoke时主动传入才能真正的被调用到实体方法!
		return Proxy.newProxyInstance(loader, new Class[] { api,
				HessianRemoteObject.class }, handler);
	}

}
