package com.jeecms.content.domain.dto;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.audit.domain.AuditStrategy;
import com.jeecms.content.domain.CmsModelItem;

public class ContentReviewProcessDataDto {

	private JSONObject dtoJson;
	
	private List<CmsModelItem> items;
	
	private List<CmsModelItem> auditItems;
	
	private AuditStrategy auditStrategy;
	
	private String error;
	
	

	public ContentReviewProcessDataDto(String error) {
		super();
		this.setError(error);
	}

	public ContentReviewProcessDataDto(JSONObject dtoJson, List<CmsModelItem> items, List<CmsModelItem> auditItems,
			AuditStrategy auditStrategy) {
		super();
		this.dtoJson = dtoJson;
		this.items = items;
		this.auditItems = auditItems;
		this.auditStrategy = auditStrategy;
	}

	public JSONObject getDtoJson() {
		return dtoJson;
	}

	public void setDtoJson(JSONObject dtoJson) {
		this.dtoJson = dtoJson;
	}

	public List<CmsModelItem> getItems() {
		return items;
	}

	public void setItems(List<CmsModelItem> items) {
		this.items = items;
	}

	public List<CmsModelItem> getAuditItems() {
		return auditItems;
	}

	public void setAuditItems(List<CmsModelItem> auditItems) {
		this.auditItems = auditItems;
	}

	public AuditStrategy getAuditStrategy() {
		return auditStrategy;
	}

	public void setAuditStrategy(AuditStrategy auditStrategy) {
		this.auditStrategy = auditStrategy;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
}
