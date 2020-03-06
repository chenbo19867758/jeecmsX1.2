package com.jeecms.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.jeecms.common.constants.ContentSecurityAway;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * 配置开启安全
 * 
 * @author: tom
 * @date: 2018年6月19日 下午8:33:13
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContentSecurity {
	/**
	 * 内容加密方式 默认DES
	 * 
	 * @return
	 */
	ContentSecurityAway away() default ContentSecurityAway.DES;
}
