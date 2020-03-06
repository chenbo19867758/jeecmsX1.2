package com.jeecms.content.domain.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentAttr;

/**
 * 内容前台显示封装vo
 * @author: chenming
 * @date:   2019年10月14日 下午4:05:11
 */
public class ContentFrontVo {

	/** 内容ID */
	private Integer id;
	/** 内容标题 */
	private String title;
	/** 内容标题是否加粗 */
	private Boolean titleIsBold;
	/** 内容标题的颜色 */
	private String titleColor;
	/** 栏目名称 */
	private String channelName;
	/** 创建时间 */
	private String createTime;
	/** 浏览量 */
	private Integer views;
	/** 评论量 */
	private Integer comments;
	/** 点赞数 */
	private Integer ups;
	/** 浏览URL*/
	private String url;
	/** 视频JSON */
	private JSONObject videoJson;
	/** 图片JSON */
	private JSONObject imageJson;
	/** "多图上传"字段，string集合*/
	private List<String> multiImageUploads;
	/** 发布时间 */
	private String publishTime;
	/** 内容模型ID*/
	private Integer modelId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getTitleIsBold() {
		return titleIsBold;
	}

	public void setTitleIsBold(Boolean titleIsBold) {
		this.titleIsBold = titleIsBold;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}

	public Integer getUps() {
		return ups;
	}

	public void setUps(Integer ups) {
		this.ups = ups;
	}

	public JSONObject getVideoJson() {
		return videoJson;
	}

	public void setVideoJson(JSONObject videoJson) {
		this.videoJson = videoJson;
	}

	public JSONObject getImageJson() {
		return imageJson;
	}

	public void setImageJson(JSONObject imageJson) {
		this.imageJson = imageJson;
	}

	public List<String> getMultiImageUploads() {
		return multiImageUploads;
	}

	public void setMultiImageUploads(List<String> multiImageUploads) {
		this.multiImageUploads = multiImageUploads;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
	
	public JSONObject initVideoJson(List<CmsModelItem> items,List<ContentAttr> attrs) {
		JSONObject json = new JSONObject();
		items = items.stream()
				.filter(item -> CmsModelConstant.VIDEO_UPLOAD.equals(item.getDataType()))
				.collect(Collectors.toList());
		json = this.initJson(items, attrs, json);
		return json;
	}
	
	public JSONObject initImageJson(List<CmsModelItem> items,List<ContentAttr> attrs,Content content) {
		JSONObject json = new JSONObject();
		items = items.stream()
				.filter(item -> CmsModelConstant.SINGLE_CHART_UPLOAD.equals(item.getDataType())
						||
						CmsModelConstant.MANY_CHART_UPLOAD.equals(item.getDataType()))
				.collect(Collectors.toList());
		json = this.initJson(items, attrs, json);
		json.put(CmsModelConstant.FIELD_SYS_CONTENT_RESOURCE, content.getContentExt().getReData());
		return json;
	}
	
	private JSONObject initJson(List<CmsModelItem> items,List<ContentAttr> attrs,JSONObject json) {
		if (items != null && items.size() > 0) {
			Map<String,CmsModelItem> itemMap = items.stream().collect(Collectors.toMap(CmsModelItem::getField, c->c));
			if (attrs != null && attrs.size() > 0) {
				for (ContentAttr contentAttr : attrs) {
					String attrName = contentAttr.getAttrName();
					if (itemMap.get(attrName) != null) {
						if (CmsModelConstant.MANY_CHART_UPLOAD.equals(itemMap.get(attrName).getDataType())) {
							json.put(attrName, contentAttr.getContentAttrRes());
						} else {
							json.put(attrName, contentAttr.getResourcesSpaceData());
						}
					}
				}
			}
		}
		return json;
	}

	public List<String> initMultiImageUploads(List<CmsModelItem> items) {
		List<String> multiImageUploads = new ArrayList<String>();
		items = items.stream()
				.filter(item -> CmsModelConstant.MANY_CHART_UPLOAD.equals(item.getDataType()))
				.collect(Collectors.toList());
		if (items != null && items.size() > 0) {
			multiImageUploads = items.stream().map(CmsModelItem::getField).collect(Collectors.toList());
		}
		return multiImageUploads;
	}

}
