package demon.caucho.hessian.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface HessianConnection {
	
	public void addHeader(String key, String value);

	public OutputStream getOutputStream() throws IOException;

	public void sendRequest() throws IOException;

	public int getStatusCode();

	public String getStatusMessage();

	public String getContentEncoding();

	public InputStream getInputStream() throws IOException;

	public void close() throws IOException;

	public void destroy() throws IOException;
}
