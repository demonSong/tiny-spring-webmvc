package demon.springframework.web.test;

import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl {
	
	public void sayHello(){
		System.out.println("hello my name is demon!");
	}

}
