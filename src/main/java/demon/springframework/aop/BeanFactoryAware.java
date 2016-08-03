package demon.springframework.aop;

import demon.springframework.beans.factory.BeanFactory;

/**
 * @author yihua.huang@dianping.com
 */

public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory) throws Exception;
}
