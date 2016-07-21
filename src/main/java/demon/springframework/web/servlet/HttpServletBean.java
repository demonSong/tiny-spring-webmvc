package demon.springframework.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.web.servlet.mvc.Controller;
import us.codecraft.tinyioc.context.ApplicationContext;
import us.codecraft.tinyioc.context.ClassPathXmlApplicationContext;

public class HttpServletBean extends HttpServlet {

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}
	
	/**
	 * 以上代码有如下缺点:
	 * 1:web 发送过来的不同请求,都需要重写servlet的get,put,delete等方法
	 * 2:映射关系需要context进行初始化,并且所有的映射都由xml文件所描述
	 * 3:作为一个框架,切忌使用写死的config信息
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		//获得由web前端传入的路径
		String path =req.getServletPath();
		String configLocation =path.substring(1, path.length());
		//根据获得的路径 建立和bean的映射关系,分发至不同的controller中去做处理
		try {
			ApplicationContext applicationContext =new ClassPathXmlApplicationContext("tinyioc.xml");
			Controller handler =(Controller) applicationContext.getBean(configLocation);
			handler.handleRequest(req, resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
