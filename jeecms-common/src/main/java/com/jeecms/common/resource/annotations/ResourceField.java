package com.jeecms.common.resource.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * 配置统一资源字段 该注解配置在普通字段上，根据配置信息自动查询对应的资源地址
 * 
 * @ResourceTargetId 如果注解不存在或目标编号不存在或者为null、""时不执行查询资源
 * @author tom
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Documented
public @interface ResourceField {

	/**
	 * 读取资源是单条或者读条 true：读取多条资源地址，对应设置到List集合内
	 * false：读取单条资源地址，对应设置配置ResourceField注解的字段value
	 * 
	 * @return
	 */
	boolean multiple() default false;

	/**
	 * 如果配置该字段则不会去找@Id配置的字段 该字段默认为空，则默认使用@Id标注的字段的值作为查询统一资源的target_id
	 * 
	 * @return
	 */
	String targetIdField() default "";
}
