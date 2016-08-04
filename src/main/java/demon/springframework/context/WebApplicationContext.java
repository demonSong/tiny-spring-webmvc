package demon.springframework.context;

//inteface 定义常量 是否存在缺陷
public interface WebApplicationContext extends ApplicationContext {
	
	//请仔细思考加上这句话的作用
	String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

}
