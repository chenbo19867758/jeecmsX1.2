/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.member.domain.vo;

import com.alibaba.fastjson.JSONObject;

/**   
 * 会员详情VO
 * @author: ljw
 * @date:   2019年7月15日 下午4:48:18     
 */
public class MemberInfoVo {

	/** 会员id*/
	private Integer id;
	/**字段值数据对象**/
	private JSONObject dataField;
	/** 渲染字段数据 */
	private JSONObject renderingField;
	/**来源站点名称**/
	private String siteName;
	/**注册时间**/
	private String registerTime;
	/**是否管理员**/
	private Boolean admin;
	/** 是否来源于第三方登录 **/
	private Boolean third;
	/** 等级图标 **/
	private String levelIcon;
	
	public MemberInfoVo() {}
	
	public JSONObject getRenderingField() {
		return renderingField;
	}

	public void setRenderingField(JSONObject renderingField) {
		this.renderingField = renderingField;
	}

	public JSONObject getDataField() {
		return dataField;
	}

	public void setDataField(JSONObject dataField) {
		this.dataField = dataField;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public Boolean getThird() {
		return third;
	}

	public void setThird(Boolean third) {
		this.third = third;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getLevelIcon() {
		return levelIcon;
	}

	public void setLevelIcon(String levelIcon) {
		this.levelIcon = levelIcon;
	}

}
