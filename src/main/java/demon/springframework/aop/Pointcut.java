package demon.springframework.aop;


/**
 * @author yihua.huang@dianping.com
 */
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();
    
    Pointcut TRUE = TruePointcut.INSTANCE;
    
}
