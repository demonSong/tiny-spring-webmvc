package demon.springframework.beans.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import demon.springframework.beans.BeanDefinition;
import demon.springframework.beans.BeanPostProcessor;
import demon.springframework.beans.DmnBeanDefinition;
import demon.springframework.beans.config.InstantiationAwareBeanPostProcessor;
import demon.springframework.beans.factory.config.BeanExpressionContext;
import demon.springframework.beans.factory.config.BeanExpressionResolver;
import demon.springframework.beans.factory.config.ConfigurableBeanFactory;
import demon.springframework.beans.factory.config.Scope;
import demon.springframework.beans.factory.support.BeanDefinitionRegistry;
import demon.springframework.beans.factory.support.FactoryBeanRegistrySupport;
import demon.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author yihua.huang@dianping.com
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ListableBeanFactory ,ConfigurableBeanFactory,BeanDefinitionRegistry{

	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
	
	private Map<String, DmnBeanDefinition> testBeanDefinitionMap = new ConcurrentHashMap<String, DmnBeanDefinition>();
	
	private ClassLoader beanClassLoader =ClassUtils.getDefaultClassLoader();

	private final Map<String, RootBeanDefinition> mergedBeanDefinitions =
			new ConcurrentHashMap<String, RootBeanDefinition>(64);
	
	/** Resolution strategy for expressions in bean definition values */
	private BeanExpressionResolver beanExpressionResolver;
	
	private final List<String> beanDefinitionNames = new ArrayList<String>();

	private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();
	
	/** 存储所有类型的beanNames*/
	private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap<Class<?>, String[]>(64);
	
	private boolean hasInstantiationAwareBeanPostProcessors;
	
	@Override
	public void setBeanExpressionResolver(BeanExpressionResolver resolver) {
		this.beanExpressionResolver = resolver;
	}

	@Override
	public BeanExpressionResolver getBeanExpressionResolver() {
		return this.beanExpressionResolver;
	}

	@Override
	public Object getBean(String name) throws Exception {
		//需要重构的地方
		if(name.equals("rmiHelloWorldService") || name.equals("rmiServiceExporter") || name.equals("hessianHelloWorldService")){
			return doGetBean(name,null,null,false);
		}
		return doGetBean(name);
	}

	
	protected Object doGetBean(String name) throws Exception {
		BeanDefinition beanDefinition = beanDefinitionMap.get(name);
		if (beanDefinition == null) {
			throw new IllegalArgumentException("No bean named " + name + " is defined");
		}
		Object bean = beanDefinition.getBean();
		//bean的初始化过程
		if (bean == null) {
			//在bean进行初始化时 会先创建bean
			bean = doCreateBean(beanDefinition);
			//添加bean的处理器,可以xml文件形式动态配置 or 可以在程序中就实现配置
            bean = initializeBean(bean, name);
            //bean获得了增加,其实这是一种静态代理模式
            //类似于缓存,在map中获得beandefinition中后,下次getbean直接可以在definition中取
            beanDefinition.setBean(bean);
		}
		return bean;
	}
	
	@SuppressWarnings("unchecked")
	protected<T> T doGetBean(final String name,final Class<T> requeiredType,final Object[] args,boolean typeCheckOnly )
			throws BeansException{
		final String beanName =name;
		
		Object bean;
		Object sharedInstance;
		
		final RootBeanDefinition mbd =getMergedLocalBeanDefinition(name);
		//create bean instance 
		if(mbd.isSingleton()){
			sharedInstance =getSingleton(name,new ObjectFactory<Object>() {
				@Override
				public Object getObject() throws BeansException {
					return createBean(name,mbd,args);
				}
			});
			bean =getObjectForBeanInstance(sharedInstance,name,beanName,mbd);
		}
		else {
			return null;
		}
		return (T) bean;
	}
	
	private Object getObjectForBeanInstance(Object beanInstance, String name,
			String beanName, RootBeanDefinition mbd) {
		if(!(beanInstance instanceof FactoryBean) ||BeanFactoryUtils.isFactoryDereference(name)){
			return beanInstance;
		}
		Object object =null;
		
		if(object ==null){
			FactoryBean<?> factory =(FactoryBean<?>) beanInstance;
			if(mbd ==null){
				mbd =getMergedLocalBeanDefinition(beanName);
			}
			object =getObjectFromFactoryBean(factory, beanName, true);
		}
		return object;
	}
	
	protected abstract Object createBean(String beanName, RootBeanDefinition mbd, Object[] args)
			throws BeanCreationException;
	
	protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
		// Quick check on the concurrent map first, with minimal locking.
		RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
		if (mbd != null) {
			return mbd;
		}
		return getMergedBeanDefinition(beanName, getTestBeanDefinition(beanName));
	}
	
	protected RootBeanDefinition getMergedBeanDefinition(String beanName, DmnBeanDefinition bd)
			throws BeanDefinitionStoreException {

		return getMergedBeanDefinition(beanName, bd, null);
	}
	
	protected RootBeanDefinition getMergedBeanDefinition(
			String beanName, DmnBeanDefinition bd, DmnBeanDefinition containingBd)
			throws BeanDefinitionStoreException {
		synchronized (this.mergedBeanDefinitions) {
			RootBeanDefinition mbd =null;
			
			if(containingBd ==null){
				mbd=this.mergedBeanDefinitions.get(beanName);
			}
			
			if(mbd ==null){
				if(bd.getParentName() ==null){
					if(bd instanceof RootBeanDefinition){
						mbd =((RootBeanDefinition) bd).cloneBeanDefinition();
					}
					else {
						mbd =new RootBeanDefinition(bd);
					}
				}
				else{
					//child bean definition:needs to be merged with parent
				}
			
				if(!StringUtils.hasLength(mbd.getScope())){
					mbd.setScope(RootBeanDefinition.SCOPE_SINGLETON);
				}
				
				if(containingBd ==null){
					this.mergedBeanDefinitions.put(beanName, mbd);
				}
			}
			return mbd;
		}
	}
	
	
	public BeanDefinition getBeanDefinition(String beanName){
		return this.beanDefinitionMap.get(beanName);
	}
	
	public DmnBeanDefinition getTestBeanDefinition(String beanName){
		return this.testBeanDefinitionMap.get(beanName);
	}

	protected Object initializeBean(Object bean, String name) throws Exception {
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
		}

		
		//初始化时,便回调这些aware methods方法
		invokeAwareMethods(name, bean);
		
		//初始化init方法
		try {
			invokeInitMethods(name, bean);
		} catch (Throwable e) {
			throw new BeanCreationException(
					"Invocation of init method failed", e);
		}
		
		for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
		}
		
		return bean;
	}
	
	protected abstract void invokeAwareMethods(final String beanName,final Object bean);
	
	protected void invokeInitMethods(String beanName, final Object bean)
			throws Throwable {
		boolean isInitializingBean = (bean instanceof InitializingBean);
		if(isInitializingBean){
			((InitializingBean)bean).afterPropertiesSet();
		}
	}

	protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
		return beanDefinition.getBeanClass().newInstance();
	}

	@Override
	public void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws Exception {
		beanDefinitionMap.put(name, beanDefinition);
		beanDefinitionNames.add(name);
	}
	
	@Override
	public void registerBeanDefinition(String beanName,
			DmnBeanDefinition beanDefinition) throws Exception {
		testBeanDefinitionMap.put(beanName, beanDefinition);
	}
	
	public String[] getBeanDefinitionNames() {
		synchronized (this.beanDefinitionMap) {
			return StringUtils.toStringArray(this.beanDefinitionNames);
		}
	}

	public void preInstantiateSingletons() throws Exception {
		for (Iterator it = this.beanDefinitionNames.iterator(); it.hasNext();) {
			String beanName = (String) it.next();
			getBean(beanName);
		}
	}

	protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
		//根据获得beanDefinition进行判断
		//if singleton 走getSingleton逻辑
		
		Object bean = createBeanInstance(beanDefinition);
		beanDefinition.setBean(bean);
		
		//使用子类方法,并传入bean进行初始化
		Object exposedObject =bean;
		populateBean(beanDefinition.getBeanClassName(), beanDefinition, bean);
		
		//需要重写applyPropertyValues方法
		applyPropertyValues(exposedObject, beanDefinition);
		return exposedObject;
	}

	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {

	}

	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws Exception {
		Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
		this.beanPostProcessors.remove(beanPostProcessor);
		this.beanPostProcessors.add(beanPostProcessor);
		//当使用了autowire processor后 便会设置标志位为true
		if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
			this.hasInstantiationAwareBeanPostProcessors =true;
		}
	}
	
	protected boolean hasInstantiationAwareBeanPostProcessors() {
		return this.hasInstantiationAwareBeanPostProcessors;
	}

	public List getBeansForType(Class<?> type) throws Exception {
		List beans = new ArrayList<Object>();
		//从beandefinition中获得所有的跟type相关的beans
		for (String beanDefinitionName : beanDefinitionNames) {
			if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
				beans.add(getBean(beanDefinitionName));
			}
		}
		return beans;
	}
	
	protected Class<?> resolveBeanClass(final RootBeanDefinition mbd, String beanName, final Class<?>... typesToMatch)
			throws CannotLoadBeanClassException {
		try {
			if(mbd.hasBeanClass()){
				return mbd.getBeanClass();
			}
			else{
				return doResolveBeanClass(mbd,typesToMatch);
			}
		} catch (ClassNotFoundException e) {
			throw new CannotLoadBeanClassException(mbd.toString(), beanName, mbd.getBeanClassName(), e);
		}
	}
	
	private Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch) throws ClassNotFoundException {
		//临时的classLoader
		return mbd.resolveBeanClass(getBeanClassLoader());
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = (beanClassLoader != null ? beanClassLoader : ClassUtils.getDefaultClassLoader());
	}

	@Override
	public ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}

	@Override
	public Class<?> getType(String name){
		try {
			return getBean(name).getClass();
		} catch (Exception e) {
			throw new NoSuchBeanDefinitionException("没有找到{bean}");
		}
	}
	
	@Override
	public String[] getBeanNamesForType(Class<?> type){
		Map<Class<?>, String[]> cache =this.allBeanNamesByType;
		String[] resolvedBeanNames= cache.get(type);
		if(resolvedBeanNames !=null){
			return resolvedBeanNames;
		}
		resolvedBeanNames =doGetBeanNamesForType(type);
		cache.put(type, resolvedBeanNames);
		return resolvedBeanNames;
	}
	
	private String[] doGetBeanNamesForType(Class<?> type){
		List<String> result = new ArrayList<String>();
		String[] beanDefinitionNames = getBeanDefinitionNames();
		for(String beanName :beanDefinitionNames){
			boolean matchFound =isTypeMatch(beanName, type);
			if(matchFound){
				result.add(beanName);
			}
		}
		return StringUtils.toStringArray(result);
	}
	
	public boolean isTypeMatch(String name, Class<?> targetType){
		if(targetType.isAssignableFrom(beanDefinitionMap.get(name).getBeanClass())){
			return true;
		}
		else{
			return false;
		}
	}
	
	public List<BeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}
	
	public Object evaluateBeanDefinitionString(String value, DmnBeanDefinition beanDefinition) {
		if(this.beanExpressionResolver ==null){
			return value;
		}
		Scope scope =null;
		return this.beanExpressionResolver.evalute(value, new BeanExpressionContext(this, scope));
	}
	
	protected abstract void populateBean(String beanName,BeanDefinition mbd,Object bean)throws Exception;

}
