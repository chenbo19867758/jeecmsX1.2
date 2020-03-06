/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.directive;

import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.questionnaire.domain.vo.QuestionnaireFrontListVo;
import com.jeecms.questionnaire.service.SysQuestionnaireService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.FrontUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_LIST;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/6 14:24
 */
public class QuestionnaireDirectiveList implements TemplateDirectiveModel {
	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "cms_questionnaire_list";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
						TemplateDirectiveBody body) throws TemplateException, IOException {
		Integer siteId = DirectiveUtils.getInt("siteId", params);
		Integer orderBy = DirectiveUtils.getInt("orderBy", params);
		int count = FrontUtils.getCount(params);
		if (siteId == null) {
			CmsSite site = FrontUtils.getSite(env);
			if (site != null) {
				siteId = site.getId();
			}
		}
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
		List<QuestionnaireFrontListVo> list = service.list(siteId, orderBy, count);
		paramWrap.put(OUT_LIST, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(list));
		Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@Autowired
	private SysQuestionnaireService service;
}
