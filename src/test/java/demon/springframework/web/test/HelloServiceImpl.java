package demon.springframework.web.test;


import demon.springframework.beans.factory.annotation.Autowired;
import demon.springframework.stereotype.Service;

@Service
public class HelloServiceImpl {
	
	@Autowired
	private OutputServiceImpl outputServiceImpl;
	
	public void sayHello(){
		outputServiceImpl.output();
	}

}
