/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.domain.vo;

import java.math.BigDecimal;

/**
 * 微信粉丝VO
 * 
 * @author: ljw
 * @date: 2019年5月31日 下午1:56:09
 */
public class WechatFansVO {

	/** 名称 **/
	private String name;
	/**用户的性别，值为1时是男性，值为2时是女性，值为0时是未知**/
	private Integer sex;
	/** 数量 **/
	private Long number;
	/**占比**/
	private BigDecimal proportion;
	
	public WechatFansVO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public BigDecimal getProportion() {
		return proportion;
	}

	public void setProportion(BigDecimal proportion) {
		this.proportion = proportion;
	}
	
	
}
