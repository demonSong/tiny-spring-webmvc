package demon.springframework.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultController implements Controller{

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("this is a default controller");
	}

}
