package demon.caucho.services.client;

public interface ServiceProxyFactory {
	
	public Object create(Class api, String url)
		    throws java.net.MalformedURLException;

}
