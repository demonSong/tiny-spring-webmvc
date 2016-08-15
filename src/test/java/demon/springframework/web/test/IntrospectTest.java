package demon.springframework.web.test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.junit.Test;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

public class IntrospectTest {
	
	@Test
	public void test(){
		//只搜寻符合set 和 get 命名方式的域
		try {
			BeanInfo beanInfo=Introspector.getBeanInfo(RmiProxyFactoryBean.class);
			PropertyDescriptor[] pd =beanInfo.getPropertyDescriptors();
			System.out.println(pd);
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
