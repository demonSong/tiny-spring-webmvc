package demon.caucho.hessian.client;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class HessianURLConnectionFactory implements HessianConnectionFactory {

	private HessianProxyFactory _proxyFactory;

	@Override
	public void setHessianProxyFactory(HessianProxyFactory factory) {
		this._proxyFactory = factory;
	}

	@Override
	public HessianConnection open(URL url) throws IOException {
		URLConnection conn = url.openConnection();

		long connectTimeout = _proxyFactory.getConnectTimeout();

		if (connectTimeout >= 0) {
			conn.setConnectTimeout((int) connectTimeout);
		}

		conn.setDoOutput(true);

		long readTimeout = _proxyFactory.getReadTimeout();

		if (readTimeout > 0) {
			try {
				conn.setReadTimeout((int) readTimeout);
			} catch (Throwable e) {
			}
		}

		return new HessianURLConnection(url, conn);
	}

}
