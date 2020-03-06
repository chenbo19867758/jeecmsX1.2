/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.directive;

import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.content.domain.Content;
import com.jeecms.content.service.ContentFrontService;
import com.jeecms.util.FrontUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_BEAN;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/16 11:43
 */

public class ContentDirectiveBean extends ContentDirectiveAbstract {

	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "cms_content";

	public static final String PARAM_CHANNEL_ID = "channelId";
	public static final String PARAM_SITE_ID = "siteId";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
						TemplateDirectiveBody body) throws TemplateException, IOException {
		Integer id = getId(params);
		Boolean next = getNext(params);
		Content content;
		if (next == null) {
			content = contentFrontService.findById(id);
		} else {
			Integer siteId = DirectiveUtils.getInt(PARAM_SITE_ID, params);
			siteId = siteId == null ? FrontUtils.getSite(env).getId() : siteId;
			Integer channelId = DirectiveUtils.getInt(PARAM_CHANNEL_ID, params);
			content = contentFrontService.getSide(id, siteId, channelId, next);
		}
		Content bean = null;
		if (content != null) {
			bean = contentFrontService.initialize(content);
		}
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
		paramWrap.put(OUT_BEAN, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(bean));
		Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@Autowired
	private ContentFrontService contentFrontService;

}
