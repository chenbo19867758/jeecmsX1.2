package com.jeecms.common.jsonfilter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义多个bean过滤返回注解
 * @Description:   
 * @author: wangqq
 * @date:   2018年3月19日 下午3:00:37     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
//@Target(ElementType.METHOD)
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MoreSerializeField {

	/**
	 * 获取注解SerializeField
	 * @Title: value  
	 * @return: SerializeField[]
	 */
	SerializeField[] value() default {};

}
