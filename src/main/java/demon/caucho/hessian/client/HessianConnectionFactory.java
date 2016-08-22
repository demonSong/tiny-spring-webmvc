package demon.caucho.hessian.client;

import java.io.IOException;
import java.net.URL;

public interface HessianConnectionFactory {

	public void setHessianProxyFactory(HessianProxyFactory factory);

	public HessianConnection open(URL url) throws IOException;

}
