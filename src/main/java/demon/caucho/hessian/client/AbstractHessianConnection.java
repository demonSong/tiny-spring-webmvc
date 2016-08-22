package demon.caucho.hessian.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractHessianConnection implements HessianConnection {

	public void addHeader(String key, String value) {
	}

	/**
	 * Returns the output stream for the request.
	 */
	public abstract OutputStream getOutputStream() throws IOException;

	/**
	 * Sends the query
	 */
	public abstract void sendRequest() throws IOException;

	/**
	 * Returns the status code.
	 */
	public abstract int getStatusCode();

	/**
	 * Returns the status string.
	 */
	public abstract String getStatusMessage();

	/**
	 * Returns the InputStream to the result
	 */
	public abstract InputStream getInputStream() throws IOException;

	@Override
	public String getContentEncoding() {
		return null;
	}

	/**
	 * Close/free the connection, using keepalive if appropriate.
	 */
	public void close() throws IOException {
		destroy();
	}

	/**
	 * Destroy/disconnect the connection
	 */
	public abstract void destroy() throws IOException;

}
