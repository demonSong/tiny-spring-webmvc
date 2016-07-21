package demon.springframework.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class HttpServletBean extends HttpServlet {

	/**
	 * 1/思考下为什么官方需要加入final这样一个关键字
	 */
	@Override
	public final void init() throws ServletException {

		// set bean properties on servlet

		initServletBean();

	}

	/**
	 * 随处可见的模板方法
	 */
	protected void initServletBean() throws ServletException {
	}

	/**
	 * servletConfig是默认存在的，我们可以看看servlet输出后，是什么样的结果
	 * 1/为什么需要servletContext和servletName 为了能够根据servlet的运行环境
	 * 来初始化咱们需要的webApplicationContext
	 */
	@Override
	public final String getServletName() {
		return (getServletConfig() != null ? getServletConfig()
				.getServletName() : null);
	}

	@Override
	public final ServletContext getServletContext() {
		return (getServletConfig() != null ? getServletConfig()
				.getServletContext() : null);
	}

	/**
	 * 以上代码有如下缺点: 1:web 发送过来的不同请求,都需要重写servlet的get,put,delete等方法
	 * 2:映射关系需要context进行初始化,并且所有的映射都由xml文件所描述 3:作为一个框架,切忌使用写死的config信息
	 * 4:官方封装的servlet还没有重写任何get方法,可见官方的httpservletBean并没有具体的分发操作
	 */
	// @Override
	// protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	// // 获得由web前端传入的路径
	// String path = req.getServletPath();
	// String configLocation = path.substring(1, path.length());
	// // 根据获得的路径 建立和bean的映射关系,分发至不同的controller中去做处理
	// try {
	// // 要把配置文件抽象出去
	// ApplicationContext applicationContext = new
	// ClassPathXmlApplicationContext(
	// "tinyioc.xml");
	// Controller handler = (Controller) applicationContext
	// .getBean(configLocation);
	// handler.handleRequest(req, resp);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}
