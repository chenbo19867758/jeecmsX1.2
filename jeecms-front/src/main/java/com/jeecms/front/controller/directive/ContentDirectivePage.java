/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.directive;

import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.content.domain.Content;
import com.jeecms.content.service.ContentFrontService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.FrontUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_PAGINATION;
import static com.jeecms.content.constants.ContentConstant.ORDER_TYPE_SORT_NUM_DESC;

/**
 * 内容分页
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/15 17:53
 */

public class ContentDirectivePage extends ContentDirectiveAbstract {

	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "cms_content_page";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
						TemplateDirectiveBody body) throws TemplateException, IOException {
		int pageNo = FrontUtils.getPageNo(env);
		int count = FrontUtils.getCount(params);
		Integer[] channelIds = getChannelId(params);
		Integer[] tagIds = getTagId(params);
		String[] channelPaths = getChannelPath(params);
		Integer siteId = getSiteId(params);
		Integer[] typeIds = getTypeId(params);
		String title = getTitle(params);
		Boolean isNew = getNew(params);
		Boolean isTop = getIsTop(params);
		Integer releaseTarget = getReleaseTarget(params);
		Date timeBegin = getTimeBegin(params);
		Date timeEnd = getTimeEnd(params);
		Integer[] excludeId = getExcludeId(params);
		Integer[] modelId = getModelId(params);
		Integer orderBy = getOrderNy(params);
		Pageable pageable = PageRequest.of(pageNo - 1, count);
		orderBy = orderBy != null ? orderBy : ORDER_TYPE_SORT_NUM_DESC;
		CmsSite site = FrontUtils.getSite(env);
		Page<Content> contents = contentFrontService.getPage(channelIds,
				tagIds, channelPaths, siteId, typeIds, title, isNew, releaseTarget,
				isTop, timeBegin, timeEnd, excludeId, modelId, orderBy, pageable, site);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
		/*List<Content> list = contentFrontService.getList(channelIds,
			tagIds, channelPaths, siteId, typeIds, title, isNew, releaseTarget,
			isTop, timeBegin, timeEnd, excludeId, modelId, orderBy, null, site);
		list = list.parallelStream().distinct().collect(Collectors.toList());
		List<Content> contents = list.parallelStream().skip(count * (pageNo - 1)).limit(count).collect(Collectors.toList());
		//List<Content> list = contentFrontService.initializeList(contents.getContent());
		PageImpl<Content> contentPage = new PageImpl<Content>(contents, pageable, list.size());*/
		paramWrap.put(OUT_PAGINATION, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(contents));
		Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@Autowired
	private ContentFrontService contentFrontService;
}
