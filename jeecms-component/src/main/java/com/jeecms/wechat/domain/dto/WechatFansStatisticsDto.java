/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.domain.dto;

import java.util.Date;

/**
 * 微信粉丝统计Dto
 * 
 * @author: ljw
 * @date: 2019年5月30日 上午11:45:47
 */
public class WechatFansStatisticsDto {

	/** 开始时间 **/
	private Date beginDate;
	/** 结束时间 **/
	private Date endDate;
	/**查询类型1.新增人数 2.取消关注 3.净增人数 4.累计人数**/
	private Integer type;
	/** 站点ID **/
	private Integer siteId;
	/** 公众号APPID **/
	private String appid;
	/**用户来源:0代表其他合计 1代表公众号搜索 17代表名片分享 30代表扫描二维码 43代表图文页右上角菜单
	 51代表支付后关注（在支付完成页） 57代表图文页内公众号名称 75代表公众号文章广告 78代表朋友圈广告**/
	private Integer userSource; 
	
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getUserSource() {
		return userSource;
	}

	public void setUserSource(Integer userSource) {
		this.userSource = userSource;
	}

}
