package demon.springframework.beans.io;

import java.net.URL;

import org.springframework.util.ResourceUtils;

/**
 * @author yihua.huang@dianping.com
 */
public class ResourceLoader {
	
	public static String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	
	public static String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;

    public Resource getResource(String location){
        URL resource = this.getClass().getClassLoader().getResource(location);
        return new UrlResource(resource);
    }
    
    //一个类的接口方法
    public ClassLoader getClassLoader(){
		return null;
	}
}
