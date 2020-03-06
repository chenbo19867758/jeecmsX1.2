/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.directive;

import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentRelation;
import com.jeecms.content.service.ContentFrontService;
import com.jeecms.content.service.ContentRelationService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.util.FrontUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_LIST;
import static com.jeecms.content.constants.ContentConstant.ORDER_TYPE_SORT_NUM_DESC;

/**
 * 内容列表
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/15 17:52
 */

public class ContentDirectiveList extends ContentDirectiveAbstract {

	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "cms_content_list";

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
						TemplateDirectiveBody body) throws TemplateException, IOException {
		Integer[] channelIds = getChannelId(params);
		Integer[] ids = getIds(params);
		Integer[] tagIds = getTagId(params);
		String[] channelPaths = getChannelPath(params);
		Integer siteId = getSiteId(params);
		Integer[] typeIds = getTypeId(params);
		String title = getTitle(params);
		Boolean isNew = getNew(params);
		Boolean isTop = getIsTop(params);
		Date timeBegin = getTimeBegin(params);
		Date timeEnd = getTimeEnd(params);
		int count = FrontUtils.getCount(params);
		Integer[] excludeId = getExcludeId(params);
		Integer[] modelId = getModelId(params);
		Integer orderBy = getOrderNy(params);
		Integer id = getId(params);
		Integer relate = getRelate(params);
		List<Content> contents = new ArrayList<>();
		orderBy = orderBy != null ? orderBy : ORDER_TYPE_SORT_NUM_DESC;
		//ids 如果有值 则只取固定ID的内容列表  第一优先
		if (ids != null && ids.length > 0) {
			contents = contentFrontService.findAllById(Arrays.asList(ids), orderBy);
		} else {
			CmsSite site = FrontUtils.getSite(env);
			if (ASSOCIATION_HAND.equals(relate)) {
				//手动关联内容
				List<ContentRelation> relations = contentRelationService.findByContentId(id);
				Integer[] relationIds = new Integer[relations.size()];
				for (int i = 0; i < relations.size(); i++) {
					relationIds[i] = relations.get(i).getRelationContentId();
				}
				contents = contentFrontService.getList(relationIds, orderBy, count);
			} else if (ASSOCIATION_TAG.equals(relate)) {
				//tag词关联内容
				contents = contentFrontService.getList(channelIds, tagIds,
					channelPaths, siteId, typeIds, title, isNew, null, isTop,
					timeBegin, timeEnd, excludeId, modelId, orderBy, count, site);
			} else if (ASSOCIATION_ALL.equals(relate)) {
				//手动关联
				if (id != null) {
					List<ContentRelation> relations = contentRelationService.findByContentId(id);
					Integer[] relationIds = new Integer[relations.size()];
					for (int i = 0; i < relations.size(); i++) {
						relationIds[i] = relations.get(i).getRelationContentId();
					}
					contents = contentFrontService.getList(relationIds, orderBy, count);
				}
				if (contents.size() < count) {
					//tag词关联
					contents.addAll(contentFrontService.getList(channelIds, tagIds,
						channelPaths, siteId, typeIds, title, isNew, null, isTop,
						timeBegin, timeEnd, excludeId, modelId, orderBy, count - contents.size(), site));
				}
			} else {
				//普通条件查询
				contents = contentFrontService.getList(channelIds, null,
					channelPaths, siteId, typeIds, title, isNew, null, isTop,
					timeBegin, timeEnd, excludeId, modelId, orderBy, count, site);
			}
		}
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
		paramWrap.put(OUT_LIST, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(contents));
		Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@Autowired
	private ContentFrontService contentFrontService;
	@Autowired
	private ContentRelationService contentRelationService;
}
