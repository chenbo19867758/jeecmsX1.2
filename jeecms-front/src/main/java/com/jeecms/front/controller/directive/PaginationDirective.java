/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.directive;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/9/11 14:37
 */

import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.FrontUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Map;

import static com.jeecms.common.constants.TplConstants.TPLDIR_STYLE_PAGE;
import static com.jeecms.common.constants.TplConstants.TPL_STYLE_PAGE_CONTENT;
import static com.jeecms.common.constants.TplConstants.TPL_SUFFIX;
import static com.jeecms.common.constants.WebConstants.UTF8;
import static com.jeecms.common.util.FrontUtilBase.PARAM_SYS_PAGE;
import static com.jeecms.common.util.FrontUtilBase.PARAM_USER_PAGE;
import static com.jeecms.util.FrontUtils.getTplPath;

public class PaginationDirective implements TemplateDirectiveModel {
	/**
	 * 是否为内容分页。1：内容分页；0：栏目分页。默认栏目分页。
	 */
	public static final String PARAM_CONTENT = "content";

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
						TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSite site = FrontUtils.getSite(env);
		String content = DirectiveUtils.getString(PARAM_CONTENT, params);
		if ("1".equals(content)) {
			String sysPage = DirectiveUtils.getString(PARAM_SYS_PAGE, params);
			String userPage = DirectiveUtils.getString(PARAM_USER_PAGE, params);
			if (!StringUtils.isBlank(sysPage)) {
				String tpl = TPL_STYLE_PAGE_CONTENT + sysPage + TPL_SUFFIX;
				env.include(tpl, UTF8, true);
			} else if (!StringUtils.isBlank(userPage)) {
				String tpl = getTplPath(site.getSolutionPath(),
						TPLDIR_STYLE_PAGE, userPage);
				env.include(tpl, UTF8, true);
			}
		} else {
			FrontUtils.includePagination(site, params, env);
		}
	}
}