package demon.springframework.web.servlet.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.web.servlet.mvc.Controller;

public class UpdateController implements Controller{
	
	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("hello this is update Controller");
	}

}
