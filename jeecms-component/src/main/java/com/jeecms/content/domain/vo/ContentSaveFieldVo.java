package com.jeecms.content.domain.vo;

import java.util.List;

import com.jeecms.content.domain.CmsModelItem;

/**
 * 内容新增字段返回VO
 * @author: chenming
 * @date: 2019年5月21日 下午4:40:54
 */
public class ContentSaveFieldVo {
	// 主体
	private List<CmsModelItem> subject;
	// 基本
	private List<CmsModelItem> basic;
	// 扩展
	private List<CmsModelItem> ext;

	public List<CmsModelItem> getSubject() {
		return subject;
	}

	public void setSubject(List<CmsModelItem> subject) {
		this.subject = subject;
	}

	public List<CmsModelItem> getBasic() {
		return basic;
	}

	public void setBasic(List<CmsModelItem> basic) {
		this.basic = basic;
	}

	public List<CmsModelItem> getExt() {
		return ext;
	}

	public void setExt(List<CmsModelItem> ext) {
		this.ext = ext;
	}

}
