package demon.springframework.web.test;

import demon.springframework.stereotype.Service;

@Service
public class OutputServiceImpl {
	
	public void output(){
		System.out.println("hello my name is output service!");
	}
}
