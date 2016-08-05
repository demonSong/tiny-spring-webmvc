package demon.springframework.web.servlet.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.web.context.bind.annotation.RequestMapping;
import demon.springframework.web.servlet.mvc.Controller;

@demon.springframework.stereotype.Controller
@RequestMapping
public class AddController implements Controller {

	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("hello this is add controller");
	}
	
}
