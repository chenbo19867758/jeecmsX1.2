/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 粉丝发送记录实体类
 * 
 * @author ljw
 * @version 1.0
 * @date 2018-08-09
 */
@Entity
@Table(name = "jc_wechat_fans_send_log")
public class WechatFansSendLog extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 发送类型（1-粉丝发送 2-公众号发送） **/
	private Integer sendType;
	/** 粉丝所属公众号app_id */
	private String appId;
	/** openId */
	private String openId;
	/** 发送状态（1-发送成功 2-发送失败） */
	private Integer sendStatus;
	/** 消息类型（text-文本，mpnews-图文 ，image-图片，voice-音乐，video-视频，music-音乐） */
	private String msgType;
	/** 素材json */
	private String mediaJson;
	/** 是否回复(0-否 1-是)，只有发送类型为1时，允许回复 **/
	private Boolean reply;
	/** 是否收藏(0-否 1-是)，只有发送类型为1时，允许收藏 **/
	private Boolean collect;
	/** materialJson转换成Json类型 */
	private JSONObject madia = new JSONObject();
	/** 微信粉丝 **/
	private WechatFans fans;

	public WechatFansSendLog() {
	}

	/**
	 * 全
	 */
	public WechatFansSendLog(Integer sendType, String appId, String openId, Integer sendStatus, String msgType,
			String mediaJson, Boolean reply, Boolean collect) {
		super();
		this.sendType = sendType;
		this.appId = appId;
		this.openId = openId;
		this.sendStatus = sendStatus;
		this.msgType = msgType;
		this.mediaJson = mediaJson;
		this.reply = reply;
		this.collect = collect;
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_wechat_fans_send_log", pkColumnValue = "jc_wechat_fans_send_log", 
				initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_fans_send_log")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "app_id", nullable = false, length = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "open_id", nullable = false, length = 50)
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@Column(name = "send_status", nullable = false, length = 6)
	public Integer getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}

	@Column(name = "msg_type", nullable = false, length = 50)
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	@Column(name = "media_json", nullable = true, length = 11)
	public String getMediaJson() {
		return mediaJson;
	}

	public void setMediaJson(String mediaJson) {
		this.mediaJson = mediaJson;
	}

	@Column(name = "send_type", nullable = true, length = 11)
	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	@Column(name = "is_reply", length = 1)
	public Boolean getReply() {
		return reply;
	}

	public void setReply(Boolean reply) {
		this.reply = reply;
	}

	@Column(name = "is_collect", length = 1)
	public Boolean getCollect() {
		return collect;
	}

	public void setCollect(Boolean collect) {
		this.collect = collect;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "open_id", referencedColumnName = "openid", insertable = false, updatable = false)
	public WechatFans getFans() {
		return fans;
	}

	public void setFans(WechatFans fans) {
		this.fans = fans;
	}

	@Transient
	public JSONObject getMadia() {
		madia = JSONObject.parseObject(getMediaJson());
		return madia;
	}

	public void setMadia(JSONObject madia) {
		this.madia = madia;
	}

}