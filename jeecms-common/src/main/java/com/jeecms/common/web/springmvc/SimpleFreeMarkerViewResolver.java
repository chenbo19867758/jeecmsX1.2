package com.jeecms.common.web.springmvc;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/**
 * freemarker视图解析器
 * ViewResolver for SimpleFreeMarkerView
 * Override buildView, if viewName start with / , then ignore prefix.
 * @author: tom
 * @date:   2018年12月27日 上午9:43:04     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class SimpleFreeMarkerViewResolver extends AbstractTemplateViewResolver {
	static final String SPT = "/";
	
	public SimpleFreeMarkerViewResolver() {
		setViewClass(SimpleFreeMarkerView.class);
	}

	/**
	 * if viewName start with / , then ignore prefix.
	 */
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		AbstractUrlBasedView view = super.buildView(viewName);
		// start with / ignore prefix
		if (viewName.startsWith(SPT)) {
			view.setUrl(viewName + getSuffix());
		}
		return view;
	}
}
