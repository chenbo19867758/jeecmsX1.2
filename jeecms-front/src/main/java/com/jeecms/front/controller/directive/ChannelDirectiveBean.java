/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.directive;

import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.util.FrontUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_BEAN;

/**
 * 栏目详情标签
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/17 14:09
 */

public class ChannelDirectiveBean extends ChannelDirectiveAbstract {
	private static final Logger logger = LoggerFactory.getLogger(ChannelDirectiveBean.class);
	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "cms_channel";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
						TemplateDirectiveBody body) throws TemplateException, IOException {
		Integer id = getId(params);
		Integer siteId = getSiteId(params);
		String path = getPath(params);
		if (siteId == null) {
			siteId = FrontUtils.getSite(env).getId();
		}
		Channel channel = channelService.get(id, siteId, path);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
		paramWrap.put(OUT_BEAN, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(channel));
		Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@Autowired
	private ChannelService channelService;
}
