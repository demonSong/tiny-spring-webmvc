package demon.springframework.remoting.support;

import demon.springframework.beans.factory.InitializingBean;

public abstract class UrlBasedRemoteAccessor extends RemoteAccessor implements InitializingBean{

	private String serviceUrl;
	
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
	public String getServiceUrl() {
		return this.serviceUrl;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(getServiceUrl() == null){
			throw new IllegalArgumentException("Property 'serviceUrl' is required");
		}
	}
}
