/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 用户问卷详情Vo
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/8 11:53
 */
public class QuestionnaireAnswerViewVo {

	/**
	 * 标题
	 */
	private String title;
	/**
	 * 问卷描述
	 */
	private String describe;
	/**
	 * 参与人
	 */
	private String replayName;
	/**
	 * 参与时间
	 */
	private Date participateTime;
	/**
	 * 是否有效
	 */
	private Boolean isEffective;
	/**
	 * 访问ip
	 */
	private String ip;
	/**
	 * 省市
	 */
	private String address;
	/**
	 * 访问设备
	 */
	private String device;

	private VoteStyle voteStyle;

	private JSONArray answer;

	public QuestionnaireAnswerViewVo() {
		super();
	}

	public QuestionnaireAnswerViewVo(String title, String describe, String replayName, Date participateTime, Boolean isEffective, String ip, String address, String device, VoteStyle voteStyle) {
		this.title = title;
		this.describe = describe;
		this.replayName = replayName;
		this.participateTime = participateTime;
		this.isEffective = isEffective;
		this.ip = ip;
		this.address = address;
		this.device = device;
		this.voteStyle = voteStyle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getReplayName() {
		return replayName;
	}

	public void setReplayName(String replayName) {
		this.replayName = replayName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time", nullable = false)
	public Date getParticipateTime() {
		return participateTime;
	}

	public void setParticipateTime(Date participateTime) {
		this.participateTime = participateTime;
	}

	public Boolean getEffective() {
		return isEffective;
	}

	public void setEffective(Boolean effective) {
		isEffective = effective;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public VoteStyle getVoteStyle() {
		return voteStyle;
	}

	public void setVoteStyle(VoteStyle voteStyle) {
		this.voteStyle = voteStyle;
	}

	public JSONArray getAnswer() {
		return answer;
	}

	public void setAnswer(JSONArray answer) {
		this.answer = answer;
	}

	public static class VoteStyle {
		private BgConfigBean bgConfig;
		private HeadConfigBean headConfig;
		private ContConfigBean contConfig;
		private FontConfigBean fontConfig;
		private SubConfigBean subConfig;

		public VoteStyle() {
			super();
		}

		public VoteStyle(BgConfigBean bgConfig, HeadConfigBean headConfig, ContConfigBean contConfig, FontConfigBean fontConfig, SubConfigBean subConfig) {
			this.bgConfig = bgConfig;
			this.headConfig = headConfig;
			this.contConfig = contConfig;
			this.fontConfig = fontConfig;
			this.subConfig = subConfig;
		}

		public BgConfigBean getBgConfig() {
			return bgConfig;
		}

		public void setBgConfig(BgConfigBean bgConfig) {
			this.bgConfig = bgConfig;
		}

		public HeadConfigBean getHeadConfig() {
			return headConfig;
		}

		public void setHeadConfig(HeadConfigBean headConfig) {
			this.headConfig = headConfig;
		}

		public ContConfigBean getContConfig() {
			return contConfig;
		}

		public void setContConfig(ContConfigBean contConfig) {
			this.contConfig = contConfig;
		}

		public FontConfigBean getFontConfig() {
			return fontConfig;
		}

		public void setFontConfig(FontConfigBean fontConfig) {
			this.fontConfig = fontConfig;
		}

		public SubConfigBean getSubConfig() {
			return subConfig;
		}

		public void setSubConfig(SubConfigBean subConfig) {
			this.subConfig = subConfig;
		}
	}
}
