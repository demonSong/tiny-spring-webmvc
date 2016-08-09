package demon.springframework.web.servlet.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import demon.springframework.web.context.bind.annotation.RequestMapping;
import demon.springframework.web.context.bind.annotation.RequestMethod;
import demon.springframework.web.servlet.mvc.Controller;

@demon.springframework.stereotype.Controller
@RequestMapping(value="/demon")
public class AddController implements Controller {

	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		System.out.println("hello this is add controller");
	}
	
	@RequestMapping(value="/sayHello",method=RequestMethod.POST)
	public void sayHelloToMVC(HttpServletRequest request,HttpServletResponse response){
		System.out.println("hello this is sayHello method");
	}
	
}
