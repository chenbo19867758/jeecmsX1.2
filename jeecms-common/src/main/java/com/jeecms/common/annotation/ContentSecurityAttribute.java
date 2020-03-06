package com.jeecms.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * 配置该注解表示从request.attribute内读取对应实体参数值
 * 
 * @author: tom
 * @date: 2018年6月19日 下午8:32:52
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContentSecurityAttribute {
	/**
	 * 参数值 对应配置@ContentSecurityAttribute注解的参数名称即可
	 * 
	 * @return
	 */
	String value();
}
