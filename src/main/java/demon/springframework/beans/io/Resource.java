package demon.springframework.beans.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Resource是spring内部定位资源的接口。
 * 获得各种资源
 * @author yihua.huang@dianping.com
 */
public interface Resource {

    InputStream getInputStream() throws IOException;
    
    URL getURL() throws IOException;
    
    File getFile() throws IOException;
    
    boolean isReadable();
}
