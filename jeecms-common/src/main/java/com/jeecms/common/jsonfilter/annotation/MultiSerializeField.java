package com.jeecms.common.jsonfilter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义两个bean过滤返回注解，配合@SerializeField使用
 * 
 * @Description:
 * @author: wangqq
 * @date: 2018年3月19日 下午3:00:37
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("rawtypes")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiSerializeField {
	/**
	 * 获取注解类
	 * 
	 * @Title: clazz
	 * @return: Class 
	 */
	Class clazz();

	/**
	 * 需要返回的字段
	 * 
	 * @return
	 */
	String[] includes() default {};

	/**
	 * 需要去除的字段
	 * 
	 * @return
	 */
	String[] excludes() default {};

}
