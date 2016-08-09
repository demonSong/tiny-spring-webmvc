package demon.springframework.web.servlet;

public class HandlerExecutionChain {
	
	private final Object handler;
	
	public HandlerExecutionChain(Object handler){
		this.handler =handler;
	}
	
	public Object getHandler() {
		return this.handler;
	}

}
