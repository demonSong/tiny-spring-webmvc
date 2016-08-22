package demon.caucho.hessian.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.WeakHashMap;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import com.caucho.hessian.client.HessianRuntimeException;
import com.caucho.hessian.io.HessianProtocolException;

import demon.caucho.hessian.io.AbstractHessianOutput;

public class HessianProxy implements InvocationHandler {

	protected HessianProxyFactory _factory;
	private Class<?> _type;
	private URL _url;

	private WeakHashMap<Method, String> _mangleMap = new WeakHashMap<Method, String>();

	protected HessianProxy(URL url, HessianProxyFactory factory) {
		this(url, factory, null);
	}

	/**
	 * Protected constructor for subclassing
	 */
	protected HessianProxy(URL url, HessianProxyFactory factory, Class<?> type) {
		_factory = factory;
		_url = url;
		_type = type;
	}

	public URL getURL() {
		return _url;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("TEST:hessianProxy接口调用处理方法,用来建立STUBS");
		String mangleName;
		synchronized (_mangleMap) {
			mangleName = _mangleMap.get(method);
		}

		if (mangleName == null) {

			if (!_factory.isOverloadEnabled()) {
				mangleName = method.getName();
			}

			synchronized (_mangleMap) {
				_mangleMap.put(method, mangleName);
			}

		}

		InputStream is = null;
		HessianConnection conn = null;
		try {
			conn = sendRequest(mangleName, args);

			is = getInputStream(conn);

			// int code =is.read();

			return null;
		}
		catch (HessianProtocolException e) {
			throw new HessianRuntimeException(e);
		} 
		finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
			}

			try {
				if (conn != null)
					conn.destroy();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 证明数据发送成功
	 * 
	 * @param methodName
	 * @param args
	 * @return
	 * @throws IOException
	 */
	protected HessianConnection sendRequest(String methodName, Object[] args)
			throws IOException {
		HessianConnection conn = null;

		conn = _factory.getConnectionFactory().open(_url);
		boolean isValid = false;

		try {
			addRequestHeaders(conn);

			OutputStream os = null;

			try {
				os = conn.getOutputStream();
			} catch (Exception e) {
				throw new HessianRuntimeException(e);
			}

			// 进行序列化操作,建立连接后便可以获得os进行写入操作
			AbstractHessianOutput out =_factory.getHessianOutput(os);
			out.call(methodName, args);
			out.flush();
			
			// 表明由服务器端返回数据成功
			conn.sendRequest();
			isValid = true;

			return conn;
		} finally {
			if (!isValid && conn != null) {
				conn.destroy();
			}
		}

	}

	protected InputStream getInputStream(HessianConnection conn)
			throws IOException {
		InputStream is = conn.getInputStream();
		if ("deflate".equals(conn.getContentEncoding())) {
			is = new InflaterInputStream(is, new Inflater(true));
		}
		return is;
	}

	protected void addRequestHeaders(HessianConnection conn) {
		conn.addHeader("Content-Type", "x-application/hessian");
		conn.addHeader("Accept-Encoding", "deflate");

		String basicAuth = _factory.getBasicAuth();

		if (basicAuth != null)
			conn.addHeader("Authorization", basicAuth);
	}

}
