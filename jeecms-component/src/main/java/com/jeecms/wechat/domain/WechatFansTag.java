/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 微信粉丝标签
* @author ljw
* @version 1.0
* @date 2018-08-03
*/
@Entity
@Table(name = "jc_wechat_fan_tag")
public class WechatFansTag extends AbstractDomain<Integer>  implements Serializable {
   
	private static final long serialVersionUID = 1L;
	/** 微信分配的id */
	private Integer id;
	/** 用户所属公众号的appId */
	private String appId;
	/** 标签名 */
	private String tagName;
	/** 用户数 */
	private Integer userCount;

	public WechatFansTag() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "tag_name", nullable = false, length = 50)
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@Column(name = "user_count", nullable = true, length = 11)
	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	@Column(name = "app_id", nullable = false, length = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
    
}