package com.jeecms.common.web.freemarker;

import freemarker.template.TemplateModelException;

/**
 * 非数字参数异常
 * @author: tom
 * @date:   2018年12月27日 上午9:34:08     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("serial")
public class MustSplitNumberException extends TemplateModelException {
	public MustSplitNumberException(String paramName) {
		super("The \"" + paramName
				+ "\" parameter must be a number split by ','");
	}

	public MustSplitNumberException(String paramName, Exception cause) {
		super("The \"" + paramName
				+ "\" parameter must be a number split by ','", cause);
	}
}
