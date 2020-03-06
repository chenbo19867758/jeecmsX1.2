package com.jeecms.common.web.freemarker;

import freemarker.template.TemplateModelException;

/**
 * 非日期参数异常
 * @author: tom
 * @date:   2018年12月27日 上午9:33:31     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("serial")
public class MustDateException extends TemplateModelException {
	public MustDateException(String paramName) {
		super("The \"" + paramName + "\" parameter must be a date.");
	}
}
