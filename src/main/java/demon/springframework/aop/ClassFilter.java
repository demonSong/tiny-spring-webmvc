package demon.springframework.aop;

/**
 * @author yihua.huang@dianping.com
 */
public interface ClassFilter {

    boolean matches(Class<?> clazz);
    
    ClassFilter TRUE = TrueClassFilter.INSTANCE;
}
