package com.jeecms.content.domain.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.DateTypeEditor;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentExt;
import com.jeecms.content.util.ContentInitUtils;
import com.jeecms.interact.domain.vo.CollectContent;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.domain.GlobalConfig;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 新增内容dto
 * 
 * @author: chenming
 * @date: 2019年5月16日 下午2:56:11
 */
@Valid
public class ContentSaveDto {

	/** 栏目ID */
	private Integer channelId;
	/** 模型ID */
	private Integer modelId;
	/** 内容标题 */
	private JSONObject title;
	/** 简短标题 */
	private String shortTitle;
	/** tag词 */
	private String contentTag;
	/** 摘要 */
	private String description;
	/** 发布时间 */
	private String releaseTime;
	/** 下线时间 **/
	private String offlineTime;
	/** 作者 */
	private String author;
	/** 评论设置 */
	private Integer allowComment;
	/** 手机模板 */
	private String tplMobile;
	/** PC模版 */
	private String tplPc;
	/** 外部链接 */
	private JSONObject outLink;
	/** 关键字 */
	private String keyword;
	/** 发布平台 */
	private List<String> releaseTerrace;
	/** 内容密级 */
	private Integer contentSecretId;
	/** 浏览设置（1-允许游客访问 2-登录后访问） */
	private Short viewControl;
	/** 来源 */
	private JSONObject contentSourceId;
	/** 状态 */
	private Integer type;
	/** 图片资源id */
	private Integer resource;
	/** 发文字号 */
	private JSONObject postContent;
	/** 撰写管理员ID */
	private Integer userId;
	/** 发布管理员 */
	private Integer publishUserId;
	/** 内容自定义属性List集合 */
	private JSONObject json;
	/** 文库文档*/
	private JSONArray textLibrary;
	/**创建方式*/
	private Integer createType;

	/**
	 * 给采集创建的内容 dto
	 * @param collectContent
	 * @param channelId 栏目id
	 * @param model 模型id
	 * @param userId 用户id
	 * @param defTxtModelField 默认富文本模型字段
	 * @param defPicModelField 默认多图字段
	 * @param resIds 多图id（一个）
	 * @param resourceId 单图id（一个系统默认字段）
	 * @param data 其余数据（含富文本、自定义数据）
	 * @return
	 */
	public static ContentSaveDto buildForCollect(CollectContent collectContent, Integer channelId,
												 CmsModel model, Integer userId, String defTxtModelField,
												 String defPicModelField, List<Integer>resIds,
												 Integer resourceId, Map<String,String> data){
		ContentSaveDto contentDto = new ContentSaveDto();
		String txt = collectContent.getContxt();
		JSONObject json = new JSONObject();
		if(StringUtils.isNotBlank(defTxtModelField)){
			json.put(defTxtModelField,txt);
		}
		if(StringUtils.isNotBlank(defPicModelField)){
			JSONArray picArr = new JSONArray();
			for(Integer i:resIds){
				JSONObject p = new JSONObject();
				p.put("resId",i);
				p.put("description","");
				picArr.add(p);
			}
			json.put(defPicModelField,picArr);
		}
		json.putAll(data);
		String desc = collectContent.getDescription();
		if(StringUtils.isNotBlank(collectContent.getDescription())&&collectContent.getDescription().length()>200){
			desc = desc.substring(0,200);
		}
		if(StringUtils.isNotBlank(desc)){
			desc = StrUtils.html2Text(desc,150);
		}
		contentDto.setDescription(desc);
		json.put("forward",collectContent.getForward());
		json.put("praised",collectContent.getPraised());
		json.put("repeat",collectContent.getRepeat());
		contentDto.setJson(json);
		/**判断模型是否存在外部链接字段*/
		if(model.existItem(CmsModelConstant.FIELD_SYS_CONTENT_OUTLINK)){
			JSONObject outLinkJson = new JSONObject();
			outLinkJson.put("isNewTarget",true);
			outLinkJson.put("outLink",collectContent.getOutLink());
			contentDto.setOutLink(outLinkJson);
		}
		JSONObject titleJson = new JSONObject();
		titleJson.put("titleColor","#666666");
		titleJson.put("titleIsBold",false);
		String title = collectContent.getTitle();
		/**采集百度可能带了html元素需要去除*/
		if(StringUtils.isNotBlank(title)){
			title = StrUtils.html2Text(title,150);
		}
		titleJson.put("title",title);
		contentDto.setTitle(titleJson);
		contentDto.setReleaseTime(new DateTypeEditor().getLongDateStr(collectContent.getReleaseTime()));
		contentDto.setReleaseTerrace(Arrays.asList(Content.RELEASE_PC_NAME,Content.RELEASE_MINIPROGRAM_NAME,Content.RELEASE_WAP_NAME,Content.RELEASE_APP_NAME));
		contentDto.setChannelId(channelId);
		contentDto.setModelId(model.getId());
		contentDto.setUserId(userId);
		contentDto.setPublishUserId(userId);
		contentDto.setAllowComment(1);
		contentDto.setResource(resourceId);
		Short viewControl = 3;
		contentDto.setViewControl(viewControl);
		contentDto.setType(ContentConstant.STATUS_FIRST_DRAFT);
		contentDto.setCreateType(ContentConstant.CONTENT_CREATE_TYPE_COLLECT);
		contentDto.setShortTitle(data.get(CmsModelConstant.FIELD_SYS_SHORT_TITLE));
		contentDto.setAuthor(data.get(CmsModelConstant.FIELD_SYS_AUTHOR));
		/**判断模型是否存在来源字段*/
		if(model.existItem(CmsModelConstant.FIELD_SYS_CONTENT_SOURCE)){
			JSONObject sourceJson = new JSONObject();
			sourceJson.put("sourceLink","");
			sourceJson.put("sourceName",collectContent.getContentSourceId());
			contentDto.setContentSourceId(sourceJson);
		}
		return contentDto;
	}


	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	@NotNull
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	@NotNull
	public JSONObject getTitle() {
		return title;
	}

	public void setTitle(JSONObject title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getContentTag() {
		return contentTag;
	}

	public void setContentTag(String contentTag) {
		this.contentTag = contentTag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(String offlineTime) {
		this.offlineTime = offlineTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(Integer allowComment) {
		this.allowComment = allowComment;
	}

	public String getTplMobile() {
		return tplMobile;
	}

	public void setTplMobile(String tplMobile) {
		this.tplMobile = tplMobile;
	}

	public String getTplPc() {
		return tplPc;
	}

	public void setTplPc(String tplPc) {
		this.tplPc = tplPc;
	}

	public JSONObject getOutLink() {
		return outLink;
	}

	public void setOutLink(JSONObject outLink) {
		this.outLink = outLink;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<String> getReleaseTerrace() {
		return releaseTerrace;
	}

	public void setReleaseTerrace(List<String> releaseTerrace) {
		this.releaseTerrace = releaseTerrace;
	}

	public Integer getContentSecretId() {
		return contentSecretId;
	}

	public void setContentSecretId(Integer contentSecretId) {
		this.contentSecretId = contentSecretId;
	}

	public Short getViewControl() {
		return viewControl;
	}

	public void setViewControl(Short viewControl) {
		this.viewControl = viewControl;
	}

	public JSONObject getContentSourceId() {
		return contentSourceId;
	}

	public void setContentSourceId(JSONObject contentSourceId) {
		this.contentSourceId = contentSourceId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getResource() {
		return resource;
	}

	public void setResource(Integer resource) {
		this.resource = resource;
	}

	public JSONObject getPostContent() {
		return postContent;
	}

	public void setPostContent(JSONObject postContent) {
		this.postContent = postContent;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPublishUserId() {
		return publishUserId;
	}

	public void setPublishUserId(Integer publishUserId) {
		this.publishUserId = publishUserId;
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public JSONArray getTextLibrary() {
		return textLibrary;
	}

	public void setTextLibrary(JSONArray textLibrary) {
		this.textLibrary = textLibrary;
	}
	
	/**
	 * 初始化内容拼装对象
	 */
	public Content initContent(ContentSaveDto dto, Content content, CmsSite site, GlobalConfig globalConfig,
			boolean isUpdate, Channel channel) {
		content = this.copy(content, dto);
		CmsSiteConfig cmsSiteConfig = site.getConfig();
		// 如果没有设置浏览设置，则进行向上查询(栏目、站点配置)该项设置
		if (dto.getViewControl() == null) {
			Short viewControl = null;
			if (channel.getChannelExt().getViewControl() != null) {
				viewControl = ContentInitUtils.initViewControl(channel.getChannelExt().getViewControl());
			} else {
				viewControl = ContentInitUtils.initViewControl(Short.valueOf(cmsSiteConfig.getChannelVisitLimitType()));
			}
			content.setViewControl(viewControl);
		}
		// 判断是否开启内容密级
		if (!globalConfig.getConfigAttr().getOpenContentSecurity()) {
			content.setContentSecretId(null);
		}
		// 此处特殊处理，因为底部使用其它方式判断比较冗余，所以此处采用两种初始化的方式进行对数据的判断
		content = ContentInitUtils.initFalseContentRelease(content);
		List<String> releaseTerrace = dto.getReleaseTerrace();
		// 没有发文字段设置为全网发布
		if (releaseTerrace != null && releaseTerrace.size() > 0) {
			if (releaseTerrace.contains(Content.RELEASE_PC_NAME)) {
				content.setReleasePc(true);
			}
			if (releaseTerrace.contains(Content.RELEASE_WAP_NAME)) {
				content.setReleaseWap(true);
			}
			if (releaseTerrace.contains(Content.RELEASE_APP_NAME)) {
				content.setReleaseApp(true);
			}
			if (releaseTerrace.contains(Content.RELEASE_MINIPROGRAM_NAME)) {
				content.setReleaseMiniprogram(true);
			}
		} else {
			content = ContentInitUtils.initTrueContentRelease(content);
		}
		content.setStatus(dto.getType());
		// 如果是新增，则必须将部分值强制置为空，或者初始值，但是如果是修改则无需进行此项操作
		if (isUpdate) {
			content.setEdit(true);
		} else {
			// 如果小于4，则说明其内容状态是待发布之下，所以发布管理员必须置空
			if (content.getStatus() < 4) {
				content.setPublishUserId(null);
			}
			if (dto.getCreateType() != null) {
				content.setCreateType(content.getCreateType());
			} else {
				content.setCreateType(ContentConstant.CONTENT_CREATE_TYPE_ADD);
			}
			content.setSiteId(site.getId());
			content = ContentInitUtils.initContentDefault(content);
			content = ContentInitUtils.initContentNum(content);
		}
		if (dto.getAllowComment() == null) {
			// 此处无需判断栏目为空，因为栏目那边会保证其值存在
			content.setCommentControl(Integer.valueOf(channel.getChannelExt().getCommentControl() + ""));
		}
//		if (ContentConstant.STATUS_PUBLISH == content.getStatus()) {
//			content.setReleaseTime(new Date());
//		}
		return content;
	}

	/**
	 * 初始化内容扩展拼装对象
	 */
	public ContentExt initContentExt(ContentSaveDto dto, ContentExt contentExt, Integer siteId, 
			boolean isUpdate) {
		contentExt.setKeyWord(dto.getKeyword());
		contentExt = ContentSaveDto.copy(contentExt, dto);
		// 外部链接
		JSONObject outLink = dto.getOutLink();
		if (outLink != null) {
			contentExt.setOutLink(outLink.getString(CmsModelConstant.FIELD_SYS_CONTENT_OUTLINK));
			contentExt.setIsNewTarget(outLink.getBoolean(ContentExt.IS_NEW_TARGET_NAME));
		} else {
			contentExt.setIsNewTarget(false);
		}
		contentExt.setPicResId(dto.getResource());
		// 发文字段
		JSONObject postContent = dto.getPostContent();
		if (postContent != null) {
			contentExt.setIssueOrg(postContent.getInteger(ContentExt.SUE_ORG_NAME));
			contentExt.setIssueYear(postContent.getInteger(ContentExt.SUE_YEAR_NAME));
			contentExt.setIssueNum(postContent.getString(ContentExt.SUE_NUM_NAME));
		}
		// 当不是在修改，即是在新增时，部分字段必须初始化
		if (!isUpdate) {
			contentExt = ContentInitUtils.initContentExtCount(contentExt);
		}
		if (dto.getTextLibrary() != null && dto.getTextLibrary().size() > 0) {
			Integer docResourceId = dto.getTextLibrary().getJSONObject(0).getInteger("resId");
			contentExt.setDocResourceId(docResourceId);
		} else {
			contentExt.setDocResourceId(null);
		}
		return contentExt;
	}

	/**
	 * 此处只能使用这种手动copy的方式
	 * 因为此处是新增和修改同时使用，所以必须copy为空的字段，但是该字段和数据库的字段类型不同，所以此处必须使用该手动的方式
	 */
	public Content copy(Content content, ContentSaveDto dto) {
		content.setChannelId(dto.getChannelId());
		content.setModelId(dto.getModelId());
		JSONObject title = dto.getTitle();
		content.setTitle(title.getString(Content.TITLE_NAME));
		content.setTitleIsBold(title.getBoolean(Content.TITLE_IS_BOLD_NAME));
		content.setTitleColor(title.getString(Content.TITLE_COLOR_NAME));
		content.setShortTitle(dto.getShortTitle());
		if (StringUtils.isNotBlank(dto.getReleaseTime())) {
			content.setReleaseTime(MyDateUtils.parseDate(dto.getReleaseTime(), MyDateUtils.COM_Y_M_D_H_M_S_PATTERN));
		} else {
			content.setReleaseTime(new Date());
		}
		if (StringUtils.isNotBlank(dto.getOfflineTime())) {
			content.setOfflineTime(MyDateUtils.parseDate(dto.getOfflineTime(), MyDateUtils.COM_Y_M_D_H_M_S_PATTERN));
		} else {
			content.setOfflineTime(null);
		}
		if (content.getUserId() == null) {
			content.setUserId(dto.getUserId());
		}
		if (dto.getType() > 4) {
			content.setPublishUserId(dto.getPublishUserId());
		}
		content.setViewControl(dto.getViewControl());
		content.setContentSecretId(dto.getContentSecretId());
		content.setCommentControl(dto.getAllowComment());
		content.setStatus(dto.getType());
		return content;
	}
	public static ContentExt copy(ContentExt contentExt, ContentSaveDto dto) {
		contentExt.setDescription(dto.getDescription());
		contentExt.setAuthor(dto.getAuthor());
		contentExt.setTplMobile(dto.getTplMobile());
		contentExt.setTplPc(dto.getTplPc());
		return contentExt;
	}

}
