/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.directive;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.questionnaire.domain.vo.QuestionnaireVo;
import com.jeecms.questionnaire.service.SysQuestionnaireService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.FrontUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
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
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/6 14:30
 */
public class QuestionnaireDirectiveBean implements TemplateDirectiveModel {

	private static Logger logger = LoggerFactory.getLogger(QuestionnaireDirectiveBean.class);
	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "cms_questionnaire";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
						TemplateDirectiveBody body) throws TemplateException, IOException {
		Integer siteId = DirectiveUtils.getInt("siteId", params);
		Integer id = DirectiveUtils.getInt("id", params);
		if (siteId == null) {
			CmsSite site = FrontUtils.getSite(env);
			if (site != null) {
				siteId = site.getId();
			}
		}
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
		QuestionnaireVo vo = null;
		try {
			vo = service.findById(id, siteId, true);
		} catch (GlobalException e) {
			logger.error("问卷详情获取失败，error:{}", e.getMessage());
		}
		paramWrap.put(OUT_BEAN, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(vo));
		Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@Autowired
	private SysQuestionnaireService service;
}
