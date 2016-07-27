package demon.springframework.aop;

/**
 * @author yihua.huang@dianping.com
 */
public interface PointcutAdvisor extends Advisor{

   Pointcut getPointcut();
}
