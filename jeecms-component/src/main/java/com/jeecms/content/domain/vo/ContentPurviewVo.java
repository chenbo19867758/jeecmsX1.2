package com.jeecms.content.domain.vo;

import com.jeecms.content.domain.Content;

/**
 * 内容权限vo
 * 
 * @author: chenming
 * @date: 2019年9月16日 上午11:48:27
 */
public class ContentPurviewVo {
	/** 是否可修改内容 */
	private Boolean editContentAble;
	/** 是否可删除 */
	private Boolean deleteContentAble;
	/** 是否可归档 */
	private Boolean fileContentAble;
	/** 是否可置顶 */
	private Boolean topContentAble;
	/** 是否可移动 */
	private Boolean moveContentAble;
	/** 是否可排序 */
	private Boolean sortContentAble;
	/** 是否可复制 */
	private Boolean copyContentAble;
	/** 是否可引用 */
	private Boolean quoteContentAble;
	/** 是否可操作内容类型 */
	private Boolean typeContentAble;
	/** 是否可发布 */
	private Boolean publishContentAble;
	/** 是否可站群推送 */
	private Boolean sitePushContentAble;
	/** 是否可微信推送 */
	private Boolean wechatPushContentAble;
	/** 是否可微博推送 */
	private Boolean weiboPushContentAble;

	public Boolean getEditContentAble() {
		return editContentAble;
	}

	public void setEditContentAble(Boolean editContentAble) {
		this.editContentAble = editContentAble;
	}

	public Boolean getDeleteContentAble() {
		return deleteContentAble;
	}

	public void setDeleteContentAble(Boolean deleteContentAble) {
		this.deleteContentAble = deleteContentAble;
	}

	public Boolean getFileContentAble() {
		return fileContentAble;
	}

	public void setFileContentAble(Boolean fileContentAble) {
		this.fileContentAble = fileContentAble;
	}

	public Boolean getTopContentAble() {
		return topContentAble;
	}

	public void setTopContentAble(Boolean topContentAble) {
		this.topContentAble = topContentAble;
	}

	public Boolean getMoveContentAble() {
		return moveContentAble;
	}

	public void setMoveContentAble(Boolean moveContentAble) {
		this.moveContentAble = moveContentAble;
	}

	public Boolean getSortContentAble() {
		return sortContentAble;
	}

	public void setSortContentAble(Boolean sortContentAble) {
		this.sortContentAble = sortContentAble;
	}

	public Boolean getCopyContentAble() {
		return copyContentAble;
	}

	public void setCopyContentAble(Boolean copyContentAble) {
		this.copyContentAble = copyContentAble;
	}

	public Boolean getQuoteContentAble() {
		return quoteContentAble;
	}

	public void setQuoteContentAble(Boolean quoteContentAble) {
		this.quoteContentAble = quoteContentAble;
	}

	public Boolean getTypeContentAble() {
		return typeContentAble;
	}

	public void setTypeContentAble(Boolean typeContentAble) {
		this.typeContentAble = typeContentAble;
	}

	public Boolean getPublishContentAble() {
		return publishContentAble;
	}

	public void setPublishContentAble(Boolean publishContentAble) {
		this.publishContentAble = publishContentAble;
	}

	public Boolean getSitePushContentAble() {
		return sitePushContentAble;
	}

	public void setSitePushContentAble(Boolean sitePushContentAble) {
		this.sitePushContentAble = sitePushContentAble;
	}

	public Boolean getWechatPushContentAble() {
		return wechatPushContentAble;
	}

	public void setWechatPushContentAble(Boolean wechatPushContentAble) {
		this.wechatPushContentAble = wechatPushContentAble;
	}

	public Boolean getWeiboPushContentAble() {
		return weiboPushContentAble;
	}

	public void setWeiboPushContentAble(Boolean weiboPushContentAble) {
		this.weiboPushContentAble = weiboPushContentAble;
	}
	
	public static ContentPurviewVo initContentPurviewVo(Content content,ContentPurviewVo purviewVo) {
		purviewVo.setEditContentAble(content.getEditContentAble());
		purviewVo.setDeleteContentAble(content.getDeleteContentAble());
		purviewVo.setFileContentAble(content.getFileContentAble());
		purviewVo.setTopContentAble(content.getTopContentAble());
		purviewVo.setMoveContentAble(content.getMoveContentAble());
		purviewVo.setSortContentAble(content.getSortContentAble());
		purviewVo.setCopyContentAble(content.getCopyContentAble());
		purviewVo.setQuoteContentAble(content.getQuoteContentAble());
		purviewVo.setSitePushContentAble(content.getSitePushContentAble());
		purviewVo.setWechatPushContentAble(content.getWechatPushContentAble());
		purviewVo.setWeiboPushContentAble(content.getWeiboPushContentAble());
		purviewVo.setPublishContentAble(content.getPublishContentAble());
		purviewVo.setTypeContentAble(content.getTypeContentAble());
		return purviewVo;
	}

}
