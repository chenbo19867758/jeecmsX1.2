/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.directive;

import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysSearchWord;
import com.jeecms.system.service.SysSearchWordService;
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
 * 搜索词
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/16 15:11
 */

public class SearchWordDirectiveList implements TemplateDirectiveModel {

	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "cms_search_word_list";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
						TemplateDirectiveBody body) throws TemplateException, IOException {
		Map<String, String[]> map = new HashMap<String, String[]>(1);
		Integer count = FrontUtils.getCount(params);
		Integer siteId = DirectiveUtils.getInt("siteId", params);
		Integer orderBy = DirectiveUtils.getInt("orderBy", params);
		if (siteId == null) {
			CmsSite site = FrontUtils.getSite(env);
			if (site != null) {
				siteId = site.getId();
			}
		}
		map.put("EQ_siteId_Integer", new String[]{siteId != null ? siteId.toString() : ""});
		Paginable paginable = new PaginableRequest(0, count);
		orderBy = orderBy == null ? SysSearchWord.ODER_BY_SORT_NUM_DESC : orderBy;
		List<SysSearchWord> searchWords = searchWordService.getList(siteId, orderBy, paginable);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
		paramWrap.put(OUT_LIST, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(searchWords));
		Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@Autowired
	private SysSearchWordService searchWordService;
}
