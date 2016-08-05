package demon.springframework.beans.factory;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;


/**
 * bean的容器
 * @author yihua.huang@dianping.com
 */
public interface BeanFactory {

    Object getBean(String name) throws Exception;
    
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

}
