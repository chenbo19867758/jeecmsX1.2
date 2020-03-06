package com.jeecms.channel.domain.vo;

/**
 * 查询中Map的value对象
 * 
 * @author: chenming
 * @date: 2019年4月28日 下午4:58:44
 */
public class ChannelDtoVo {
	/** 是否必填 */
	private Boolean isRequired;
	/** 名称 */
	private String itemLabel;
	/** 值 */
	private Object value;
	/** 业务值 */
	private Object businessValue;
	/** 是否是自定义字段 */
	private Boolean isCustom;
	/** 字段详细参数,json格式 */
	private String content;
	/**
	 * 数据类型(1文本2多行文本 3单选 4多选 5下拉 6日期 7单图上传 8多图上传 9视频上传 10音频上传 11附件上传 12富文本 13组织 14地址
	 * 15城市）
	 */
	private Short dataType;
	/** 分组类型(1-基本信息 2-扩展信息 3-seo信息) */
	private Short groupType;
	/** 默认提示文字 */
	private String placeholder;

	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getItemLabel() {
		return itemLabel;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getBusinessValue() {
		return businessValue;
	}

	public void setBusinessValue(Object businessValue) {
		this.businessValue = businessValue;
	}

	public Boolean getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(Boolean isCustom) {
		this.isCustom = isCustom;
	}

	public Short getDataType() {
		return dataType;
	}

	public void setDataType(Short dataType) {
		this.dataType = dataType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Short getGroupType() {
		return groupType;
	}

	public void setGroupType(Short groupType) {
		this.groupType = groupType;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

}
