package demon.springframework.web.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.web.servlet.mvc.Controller;

@demon.springframework.stereotype.Controller
public class AddController implements Controller {

	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("hello this is add controller");
	}

}
