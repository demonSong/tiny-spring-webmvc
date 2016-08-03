package demon.springframework.web.test;

import org.springframework.stereotype.Service;

@Service
public class SpringService {
	
	private String name;
	
	public void sayhai() {
		System.out.println("hello my name is spring service!");
	}
	
	public void sayName(){
		System.out.println(name);
	}
	
	public void setName(String name){
		this.name=name ;
	}
}
