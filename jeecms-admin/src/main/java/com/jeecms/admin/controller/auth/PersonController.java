package com.jeecms.admin.controller.auth;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.response.ResponseInfo;

/**
 * 个人公用controller
 * 
 * @author: tom
 * @date: 2018年3月3日 下午3:13:10
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RestController
@RequestMapping(value = "/person")
public class PersonController {

	/**
	 * 更改国际化(注册国际化拦截器实现)
	 * Locale locale = LocaleContextHolder.getLocale(); 获取国际化
	 * @Title: changeLocal
	 * @param lang 国际化语言
	 * @return: ResponseInfo
	 * @see WebConfig.localeChangeInterceptor
	 */
	@PutMapping(value = "/changeLocal")
	public ResponseInfo changeLocal(String lang) {
		return new ResponseInfo(lang);
	}
}
