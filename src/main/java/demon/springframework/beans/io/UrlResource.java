package demon.springframework.beans.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.util.ResourceUtils;

/**
 * @author yihua.huang@dianping.com
 */
public class UrlResource implements Resource {

    private final URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException{
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        return urlConnection.getInputStream();
    }

	@Override
	public URL getURL() throws IOException {
		// TODO Auto-generated method stub
		return this.url;
	}
	
	public File getFile() throws IOException {
		URL url = getURL();
		return ResourceUtils.getFile(url, getDescription());
	}
	
	public String getDescription() {
		return "URL [" + this.url + "]";
	}

	@Override
	public boolean isReadable() {
		try {
			URL url = getURL();
			if (ResourceUtils.isFileURL(url)) {
				// Proceed with file system resolution...
				File file = getFile();
				return (file.canRead() && !file.isDirectory());
			}
			else {
				return true;
			}
		}
		catch (IOException ex) {
			return false;
		}
	}
}
