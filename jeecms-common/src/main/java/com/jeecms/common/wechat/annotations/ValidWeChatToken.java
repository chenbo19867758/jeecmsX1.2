package com.jeecms.common.wechat.annotations;

import java.lang.annotation.*;

import com.jeecms.common.wechat.Const;

/**
 * 
 * @Description:定义验证微信接口所需的token是否存在或过期问题。
 * 添加该注解的方法，需要同时传递{@link}ValidateToken 对象，该对象中部分属性是必填，详情见该对象的注释说明
 *  （特别说明：当vlue值为componentAccessToken时，ValidateToken对象只需要accessToken）
 * @author: wangqq
 * @date:   2018年7月26日 下午1:52:34     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ValidWeChatToken {

	/**
	 * 定义验证类型，
	 * ACCESS_TOKEN 为验证公众号或小程序授权码是否过期或存在
	 * COMONTENT_ACCESS_TOKEN 验证开放平台第三方应用的component_access_token是否过期或存在
	 */
	String value() default Const.ValidTokenType.ACCESS_TOKEN;

}
