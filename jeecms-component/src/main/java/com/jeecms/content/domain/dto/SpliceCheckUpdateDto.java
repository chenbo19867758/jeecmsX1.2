package com.jeecms.content.domain.dto;

import com.jeecms.common.util.HibernateProxyUtil;
import com.jeecms.common.util.MyBeanUtils;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentAttr;
import com.jeecms.content.util.ContentInitUtils;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.ContentMark;
import com.jeecms.system.domain.ContentTag;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 组装校验update的dto对象
 * @author: chenming
 * @date:   2019年5月28日 下午4:31:25
 */
public class SpliceCheckUpdateDto extends Content{
	private static final long serialVersionUID = 1L;
	/** content转成Map */
	private Map<String, Object> contentMap;
	/** contentExt转成map */
	private Map<String, Object> contentExtMap;
	/** 内容tag词集合 */
	private List<String> contentTagKeys;
	/** 内容扩展对象集合 */
	private List<ContentAttr> contentAttrs;
	/** 内容正文map */
	private Map<String, String> contentTxtMap;
	/** 栏目名称 */
	private String channelFistName;
	/** 来源名称 */
	private String sourceName;
	/** 来源链接 */
	private String sourceLink;
	/** 密级名称 */
	private String secretName;
	/** 发文-机关代号对象 */
	private ContentMark sueOrg;
	/** 发文-年号对象 */
	private ContentMark sueYear;
	/** 发文字号-顺序号 **/
	private String issueNum;
	/** 资源对象 */
	private ResourcesSpaceData spaceData;
	
	private ResourcesSpaceData docSpaceData;
	/** 新窗口打开外部链接（0-否 1-是） */
	private Boolean isNewTarget;
	
	public Map<String, Object> getContentMap() {
		return contentMap;
	}

	public void setContentMap(Map<String, Object> contentMap) {
		this.contentMap = contentMap;
	}

	public Map<String, Object> getContentExtMap() {
		return contentExtMap;
	}

	public void setContentExtMap(Map<String, Object> contentExtMap) {
		this.contentExtMap = contentExtMap;
	}

	public List<String> getContentTagKeys() {
		return contentTagKeys;
	}

	public void setContentTagKeys(List<String> contentTagKeys) {
		this.contentTagKeys = contentTagKeys;
	}

	@Override
	public List<ContentAttr> getContentAttrs() {
		return contentAttrs;
	}

	@Override
	public void setContentAttrs(List<ContentAttr> contentAttrs) {
		this.contentAttrs = contentAttrs;
	}



	public ContentMark getSueOrg() {
		return sueOrg;
	}

	public void setSueOrg(ContentMark sueOrg) {
		this.sueOrg = sueOrg;
	}

	public ContentMark getSueYear() {
		return sueYear;
	}

	public void setSueYear(ContentMark sueYear) {
		this.sueYear = sueYear;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSecretName() {
		return secretName;
	}

	public void setSecretName(String secretName) {
		this.secretName = secretName;
	}

	@Override
	public String getIssueNum() {
		return issueNum;
	}

	public void setIssueNum(String issueNum) {
		this.issueNum = issueNum;
	}
	
	public ResourcesSpaceData getSpaceData() {
		return spaceData;
	}

	public void setSpaceData(ResourcesSpaceData spaceData) {
		this.spaceData = spaceData;
	}

	public String getSourceLink() {
		return sourceLink;
	}

	public void setSourceLink(String sourceLink) {
		this.sourceLink = sourceLink;
	}

	public Map<String, String> getContentTxtMap() {
		return contentTxtMap;
	}

	public void setContentTxtMap(Map<String, String> contentTxtMap) {
		this.contentTxtMap = contentTxtMap;
	}

	public Boolean getIsNewTarget() {
		return isNewTarget;
	}

	public void setIsNewTarget(Boolean isNewTarget) {
		this.isNewTarget = isNewTarget;
	}

	public String getChannelFistName() {
		return channelFistName;
	}

	public void setChannelFistName(String channelFistName) {
		this.channelFistName = channelFistName;
	}

	public ResourcesSpaceData getDocSpaceData() {
		return docSpaceData;
	}

	public void setDocSpaceData(ResourcesSpaceData docSpaceData) {
		this.docSpaceData = docSpaceData;
	}
	/**
	 * 初始化spliceCheckUpdateDto
	 */
	public SpliceCheckUpdateDto initSpliceCheckUpdateDto(Content content, SpliceCheckUpdateDto dto,Map<String,String> txtMap) {
		dto.setContentExt(content.getContentExt());
		// 将content查询出的数据copy到spliceCheckUpdateDto中
		MyBeanUtils.copyProperties(content, dto);
		MyBeanUtils.copyProperties(content.getContentExt(), dto);
		dto.setContentMap(ContentInitUtils.toContentMap(content));
		dto.setContentExtMap(ContentInitUtils.toContentExtMap(content.getContentExt()));
		if (content.getContentTags() != null && content.getContentTags().size() > 0) {
			dto.setContentTagKeys(
					content.getContentTags().stream().map(ContentTag::getTagName).collect(Collectors.toList()));
		}
		/**立即加载集合*/
		HibernateProxyUtil.loadHibernateProxy(content.getContentAttrs());
		dto.setContentAttrs(content.getContentAttrs());
		dto.setContentTxtMap(txtMap); 
 		dto.setChannelFistName(content.getChannel().getName());
		// 如果没有来源则直接从对象中获取
		if (content.getContentExt().getContentSource() != null) {
			dto.setSourceName(content.getContentExt().getContentSource().getSourceName());
			dto.setSourceLink(content.getContentExt().getContentSource().getSourceLink());
		} else {
			dto.setSourceName("");
			dto.setSourceLink("");
		}
		dto.setSueOrg(content.getContentExt().getSueOrg());
		dto.setSueYear(content.getContentExt().getSueYear());
		if (content.getSecret() != null) {
			dto.setSecretName(content.getSecret().getName());
		}
		dto.setSpaceData(content.getContentExt().getReData());
		dto.setDocSpaceData(content.getContentExt().getDocResource());
		return dto;
	}

}
