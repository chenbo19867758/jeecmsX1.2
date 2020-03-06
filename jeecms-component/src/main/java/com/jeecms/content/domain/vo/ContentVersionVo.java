package com.jeecms.content.domain.vo;

import java.util.Date;
import java.util.Map;

/**
 * 比较版本返回VO
 * 
 * @author: chenming
 * @date: 2019年6月20日 下午6:06:07
 */
public class ContentVersionVo {

	/**
	 * 来源版本创建日期
	 */
	private Date sourceCreateDate;
	/** 来源 */
	private Map<String, String> source;

	/**
	 * 来源版本创建日期
	 */
	private Date targetCreateDate;
	/** 目标 */
	private Map<String, String> target;

	public ContentVersionVo() {
		super();
	}

	public ContentVersionVo(Date sourceCreateDate, Map<String, String> source, Date targetCreateDate, Map<String, String> target) {
		this.sourceCreateDate = sourceCreateDate;
		this.source = source;
		this.targetCreateDate = targetCreateDate;
		this.target = target;
	}

	public Date getSourceCreateDate() {
		return sourceCreateDate;
	}

	public void setSourceCreateDate(Date sourceCreateDate) {
		this.sourceCreateDate = sourceCreateDate;
	}

	public Map<String, String> getSource() {
		return source;
	}

	public void setSource(Map<String, String> source) {
		this.source = source;
	}

	public Date getTargetCreateDate() {
		return targetCreateDate;
	}

	public void setTargetCreateDate(Date targetCreateDate) {
		this.targetCreateDate = targetCreateDate;
	}

	public Map<String, String> getTarget() {
		return target;
	}

	public void setTarget(Map<String, String> target) {
		this.target = target;
	}


}
