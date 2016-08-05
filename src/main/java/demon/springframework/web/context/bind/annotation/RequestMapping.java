package demon.springframework.web.context.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RequestMapping {
	
	String[] value() default {};
	
	RequestMethod[] method() default {};
	
	String[] params() default {};
	
	String[] headers() default {};
	
}
