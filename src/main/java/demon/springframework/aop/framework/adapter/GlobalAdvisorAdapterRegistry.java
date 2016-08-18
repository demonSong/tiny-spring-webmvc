package demon.springframework.aop.framework.adapter;

public abstract class GlobalAdvisorAdapterRegistry {
	
	private static AdvisorAdapterRegistry instance = new DefaultAdvisorAdapterRegistry();

	public static AdvisorAdapterRegistry getInstance() {
		return instance;
	}
	
	static void reset() {
		instance = new DefaultAdvisorAdapterRegistry();
	}
}
