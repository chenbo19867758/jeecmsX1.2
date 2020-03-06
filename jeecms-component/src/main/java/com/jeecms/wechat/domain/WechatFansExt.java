/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 微信粉丝扩展
 * @author ljw
 * @version 1.0
 * @date 2019-05-29
 */
@Entity
@Table(name = "jc_wechat_fans_ext")
public class WechatFansExt extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 用户的标识 */
	private String openid;
	/** 留言数 */
	private Integer commentCount;
	/** 精选留言数 */
	private Integer topCommentCount;
	/** 消息数 */
	private Integer mesCount;
	/**微信粉丝**/
	private WechatFans fans;
	
	public WechatFansExt() {
	}

	@Override
	@Id
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "openid", nullable = false, length = 50)
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "comment_count", nullable = false, length = 11)
	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	@Column(name = "top_comment_count", nullable = false, length = 11)
	public Integer getTopCommentCount() {
		return topCommentCount;
	}

	public void setTopCommentCount(Integer topCommentCount) {
		this.topCommentCount = topCommentCount;
	}

	@Column(name = "mes_count", nullable = false, length = 11)
	public Integer getMesCount() {
		return mesCount;
	}

	public void setMesCount(Integer mesCount) {
		this.mesCount = mesCount;
	}

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", nullable = false)
	public WechatFans getFans() {
		return fans;
	}

	public void setFans(WechatFans fans) {
		this.fans = fans;
	}

}