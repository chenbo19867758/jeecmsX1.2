/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.directive;

import com.jeecms.common.web.freemarker.DirectiveUtils;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.util.Map;

/**
 * 栏目通用参数获取
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/15 17:23
 */

public abstract class ChannelDirectiveAbstract implements TemplateDirectiveModel {

	/**
	 * 输入参数，栏目id
	 */
	private static final String ID = "id";
	/**
	 * 输入参数，站点id
	 */
	private static final String SITE_ID = "siteId";
	/**
	 * 父栏目id
	 */
	private static final String PARENT_ID = "parentId";
	/**
	 * true 显示的 false 不显示的
	 */
	private static final String DISPLAY = "display";
	/**
	 * 栏目路径
	 */
	private static final String PATH = "path";

	protected Integer getId(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getInt(ID, params);
	}

	protected Integer getSiteId(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getInt(SITE_ID, params);
	}

	protected Integer getParentId(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getInt(PARENT_ID, params);
	}

	protected Boolean getDisplay(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getBool(DISPLAY, params);
	}

	protected String getPath(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getString(PATH, params);
	}

}
