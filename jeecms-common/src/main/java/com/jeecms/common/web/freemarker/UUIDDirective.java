package com.jeecms.common.web.freemarker;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * UUID生成器标签
 * @author: tom
 * @date:   2018年12月27日 上午9:35:24     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UUIDDirective implements TemplateDirectiveModel {
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String uuid = UUID.randomUUID().toString();
		uuid = StringUtils.remove(uuid, '-');
		env.getOut().append(uuid);
	}
}
