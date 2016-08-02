package demon.springframework.web.test;

import demon.springframework.stereotype.Controller;

@Controller
public class HelloServiceImpl {
	
	public void sayHello(){
		System.out.println("hello my name is demon!");
	}

}
