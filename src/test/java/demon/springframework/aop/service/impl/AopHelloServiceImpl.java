package demon.springframework.aop.service.impl;

import demon.springframework.aop.service.AopHelloService;

public class AopHelloServiceImpl implements AopHelloService {

	@Override
	public String sayHello2Aop(String msg) throws Exception {
		System.out.println(AopHelloServiceImpl.class.getName()+": "+"hello,"+msg+"!");
		if(msg == null){
			throw new Exception("msg cannot be null");
		}
		return "OK";
	}
	
}
