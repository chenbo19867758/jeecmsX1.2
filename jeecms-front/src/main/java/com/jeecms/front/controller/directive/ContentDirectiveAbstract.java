/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.directive;

import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.util.Date;
import java.util.Map;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/15 17:52
 */

public abstract class ContentDirectiveAbstract implements TemplateDirectiveModel {

	/**
	 * 手动关联
	 */
	static final Integer ASSOCIATION_HAND = 1;
	/**
	 * 手动加tag关联
	 */
	protected static final Integer ASSOCIATION_ALL = 2;
	/**
	 * Tag词关联
	 */
	static final Integer ASSOCIATION_TAG = 3;

	/**
	 * 内容id
	 */
	private static final String ID = "id";

	/**
	 * 内容ids
	 */
	private static final String IDS = "ids";

	/**
	 * tag关键词ID 可,逗号分隔传输多个ID
	 */
	private static final String TAG_ID = "tagId";
	/**
	 * 栏目ID 可,逗号分隔传输多个ID
	 */
	private static final String CHANNEL_ID = "channelId";
	/**
	 * 栏目路径 可,逗号分隔传输多个路径 （和channelId合并栏目ID）站点ID不为空则查询的栏目ID则是指定站点下的栏目，否则是当前访问站点的栏目
	 */
	private static final String CHANNEL_PATH = "channelPath";
	/**
	 * 多个栏目自身的内容（不含子栏目） 子栏目（只取栏目ID中第一个的所有子栏目的内容） 包含引用的内容（自身的内容和引用的内容）（是否需要待讨论）
	 */
	private static final String CHANNEL_OPTION = "channelOption";
	/**
	 * 内容类型ID 可,逗号分隔传输多个ID
	 */
	private static final String TYPE_ID = "typeId";
	/**
	 * 内容标题
	 */
	private static final String TITLE = "title";
	/**
	 * 是否内容标识为新内容的（本质也是发布时间）
	 */
	private static final String IS_NEW = "new";
	/**
	 * 是否置顶
	 */
	private static final String IS_TOP = "isTop";
	/**
	 * 发布渠道查询,未设置如果是pc访问则只取pc渠道，如果是手机或平板访问则只取手机渠道数据（标签不提供该参数，但是接口要提供此参数）
	 */
	private static final String RELEASE_TARGET = "releaseTarget";
	/**
	 * 发布时间查询开始时间
	 */
	private static final String TIME_BEGIN = "timeBegin";
	/**
	 * 发布时间查询结束时间
	 */
	private static final String TIME_END = "timeEnd";
	/**
	 * 排除的内容ID（仅作用于tagId参数查询）
	 */
	private static final String EXCLUDE_ID = "excludeId";
	/**
	 * 模型id
	 */
	private static final String MODEL_ID = "modelId";
	/**
	 * 排序方式 {@link com.jeecms.content.constants.ContentConstant}
	 */
	private static final String ORDER_BY = "orderBy";

	/**
	 * 站点ID 没有该参数则默认该站点 可,逗号分隔传输多个ID
	 */
	private static final String SITE_ID = "siteId";

	/**
	 * 0 取下一条 1上一条（channelId不为空则上一条下一条是本栏目的，否则取本站的）
	 */
	private static final String NEXT = "next";

	/**
	 * 1 手动关联内容  3tag关联 2读取手动关联和tagId关联的集合内容列表
	 */
	private static final String RELATE = "relate";


	Integer getId(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getInt(ID, params);
	}

	Integer[] getIds(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getIntArray(IDS, params);
	}

	Integer getSiteId(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getInt(SITE_ID, params);
	}

	Integer[] getTagId(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getIntArray(TAG_ID, params);
	}

	Integer[] getChannelId(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getIntArray(CHANNEL_ID, params);
	}

	Integer getReleaseTarget(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getInt(RELEASE_TARGET, params);
	}

	String[] getChannelPath(Map<String, TemplateModel> params) throws TemplateException {
		String channelPath = DirectiveUtils.getString(CHANNEL_PATH, params);
		return StrUtils.splitAndTrim(channelPath, ",", "，");
	}

	Integer getChannelOption(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getInt(CHANNEL_OPTION, params);
	}

	Integer[] getTypeId(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getIntArray(TYPE_ID, params);
	}

	String getTitle(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getString(TITLE, params);
	}

	Boolean getNew(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getBool(IS_NEW, params);
	}

	Boolean getIsTop(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getBool(IS_TOP, params);
	}

	Date getTimeBegin(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getDate(TIME_BEGIN, params);
	}

	Date getTimeEnd(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getDate(TIME_END, params);
	}

	Integer[] getExcludeId(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getIntArray(EXCLUDE_ID, params);
	}

	Integer[] getModelId(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getIntArray(MODEL_ID, params);
	}

	Integer getOrderNy(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getInt(ORDER_BY, params);
	}

	Boolean getNext(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getBool(NEXT, params);
	}

	Integer getRelate(Map<String, TemplateModel> params) throws TemplateException {
		return DirectiveUtils.getInt(RELATE, params);
	}

}
