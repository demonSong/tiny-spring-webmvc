package demon.springframework.web.context;

import demon.springframework.context.ConfigurableApplicationContext;
import demon.springframework.context.WebApplicationContext;

public interface ConfigurableWebApplicationContext extends WebApplicationContext,ConfigurableApplicationContext{

	/**
	 * Set the namespace for this web application context,
	 * to be used for building a default context config location.
	 * The root web application context does not have a namespace.
	 */
	void setNamespace(String namespace);

	/**
	 * Return the namespace for this web application context, if any.
	 */
	String getNamespace();

	void setConfigLocation(String configLocation);

	/**
	 * Set the config locations for this web application context.
	 * <p>If not set, the implementation is supposed to use a default for the
	 * given namespace or the root web application context, as appropriate.
	 */
	void setConfigLocations(String[] configLocations);

	/**
	 * Return the config locations for this web application context,
	 * or <code>null</code> if none specified.
	 */
	String[] getConfigLocations();

}
