package com.jeecms.common.wechat.bean.request.applet;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:37:57
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class TemplateDraftRequest {

	/**草稿ID，本字段可通过“ 获取草稿箱内的所有临时代码草稿 ”接口获得*/
	private Integer draftId;
	
	/**要删除的模版ID*/
	private Integer templateId;



	public Integer getDraftId() {
		return draftId;
	}



	public void setDraftId(Integer draftId) {
		this.draftId = draftId;
	}



	public Integer getTemplateId() {
		return templateId;
	}



	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}



	@Override
	public String toString() {
		return "TemplateDraftRequest [draft_id=" + draftId + ", template_id=" + templateId + "]";
	}

	public TemplateDraftRequest(Integer draftId, Integer templateId) {
		super();
		this.draftId = draftId;
		this.templateId = templateId;
	}

	public TemplateDraftRequest() {
		super();
	}
	
	
}
