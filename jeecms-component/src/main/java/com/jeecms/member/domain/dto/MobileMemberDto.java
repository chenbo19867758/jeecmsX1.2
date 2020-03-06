/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.domain.dto;

import com.alibaba.fastjson.JSONObject;

/**
 * 手机端会员修改DTO
 * 
 * @author: ljw
 * @date: 2019年10月15日 上午9:13:34
 */
public class MobileMemberDto {

	/** 会员ID **/
	private Integer id;
	/** 来源站点ID **/
	private Integer siteId;
	/**************系统字段***********************/
	/** 手机号 **/
	private String telephone;
	/** 真实姓名 **/
	private String realname;
	/**************自定义字段***********************/
	/** 扩展字段名 **/
	private String attrName;
	/** 扩展字段值 **/
	private String attrValue;
	/** 数据类型(对应模型字段数据类型) **/
	private String attrType;

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public String getAttrType() {
		return attrType;
	}

	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}

	public MobileMemberDto() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

}
