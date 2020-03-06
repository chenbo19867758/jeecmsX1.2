package com.jeecms.threadmsg.common;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.message.MqConstants;
import com.jeecms.message.dto.CommonMqConstants;
import com.jeecms.message.dto.CommonMqConstants.MessageSceneEnum;

/**
 * @Description:消息数据实体
 * @author: ztx
 * @date: 2018年8月23日 上午11:00:50
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MessageInfo implements Serializable {
	private static final long serialVersionUID = -3611858166129975690L;
	/** 接收对象类型 1-全部 2-全部管理员 3-全部会员 4-组织 5-指定管理员 6-会员等级 7-会员组 8-指定会员 */
	private Integer targetType;
	/** 组织id */
	private Integer orgId;
	/** 会员组id */
	private Integer memberGroupId;
	/** 会员等级id */
	private Integer memberLevelId;
	/** 接收方id->1.指定管理员，2.指定会员 */
	private List<Integer> receiveIds;
	/** 模板标识 */
	private String messageCode;
	/** 消息场景 */
	private MessageSceneEnum scene;
	/** 标题 */
	private String title;
	/** 文本内容 */
	private String textContent;
	/** 电话号码 */
	private List<String> phones;
	/** 邮箱 */
	private List<String> emails;
	/** 数据 */
	private JSONObject data;
	/** 发送类型：1.邮箱，2.短信，3.站内信，4.邮箱+短信，5.短信+邮箱+站内信，6. 邮箱+站内信，7. 短信+站内信 */
	private Short sendType;
	/** 发送方服务站点id值，当发送类型为：1,2,4,5时必填 */
	private Integer siteId;

	/**
	 * 获取发送方式
	 * 
	 * @Title: getSendType
	 * @param email
	 *            是否邮箱
	 * @param sms
	 *            是否短信
	 * @param info
	 *            是否站内信
	 * @return: Short
	 */
	public static Short getSendType(Boolean email, Boolean sms, Boolean info) {
		Short sendType = null;
		if (email != null && email) {
			sendType = MqConstants.SEND_EMAIL;
			if (sms != null && sms) {
				sendType = MqConstants.SEND_EMAIL_SMS;
				if (info != null && info) {
					sendType = MqConstants.SEND_ALL;
				}
			} else {
				if (info != null && info) {
					sendType = MqConstants.SEND_SYSTEM_STATION_EMAIL;
				}
			}
		} else {
			if (sms != null && sms) {
				sendType = MqConstants.SEND_SMS;
				if (info != null && info) {
					sendType = MqConstants.SEND_SYSTEM_STATION_SMS;
				}
			} else {
				if (info != null && info) {
					sendType = MqConstants.SEND_SYSTEM_STATION;
				}
			}
		}
		return sendType;
	}

	public static class MessageInfoBuilder {
		private List<String> phones;
		private List<String> emails;
		private JSONObject data;
		private Integer targetType;
		private Integer orgId;
		private Integer memberGroupId;
		private Integer memberLevelId;
		private List<Integer> receiveIds;
		private String messageCode;
		private MessageSceneEnum scene;
		private String title;
		private String textContent;
		private Short sendType;
		private Integer siteId;

		public MessageInfo build() {
			// 匹配消息场景与接收对象类型和发送类型
			if (scene == null || sendType == null) {
				throw new NullPointerException("scene、messageType、sendType is required.");
			}
			// 匹配
			if (StringUtils.isBlank(messageCode) && StringUtils.isBlank(textContent)) {
				throw new NullPointerException("messageCode or textContent is required.");
			}
			switch (scene) {
			case VALIDATE_CODE:
				Object emailExtParam = null;
				Object smsExtParam = null;
				boolean check = false;
				if (phones == null || phones.size() == 0) {
					if (emails == null || emails.size() == 0) {
						check = true;
					}
				}
				if ((emailExtParam = data.get(CommonMqConstants.EXT_DATA_KEY_EMAIL)) == null) {
					if ((smsExtParam = data.get(CommonMqConstants.EXT_DATA_KEY_SMS)) == null) {
						check = true;
					}
				}
				if (check || data == null) {
					throw new NullPointerException(
							String.format("phones: %s, emails: %s, data: %s, emailExtParam: %s, smsExtParam: %s.", phones,
									emails, data, emailExtParam, smsExtParam));
				}
				break;
			default:
				break;
			}
			return new MessageInfo(targetType, orgId, memberGroupId, memberLevelId, receiveIds, messageCode, scene,
					title, textContent, phones, emails, data, sendType, siteId);
		}

		public MessageInfoBuilder(List<String> phones, List<String> emails, JSONObject data, Integer targetType, Integer orgId,
				Integer memberGroupId, Integer memberLevelId, List<Integer> receiveIds, String messageCode,
				MessageSceneEnum scene, String title, String textContent, Short sendType, Integer siteId) {
			super();
			this.phones = phones;
			this.emails = emails;
			this.data = data;
			this.targetType = targetType;
			this.orgId = orgId;
			this.memberGroupId = memberGroupId;
			this.memberLevelId = memberLevelId;
			this.receiveIds = receiveIds;
			this.messageCode = messageCode;
			this.scene = scene;
			this.title = title;
			this.textContent = textContent;
			this.sendType = sendType;
			this.siteId = siteId;
		}

		public MessageInfoBuilder(List<String> phones, List<String> emails, JSONObject data, String messageCode,
				MessageSceneEnum scene, String title, String textContent, Short sendType, Integer siteId) {
			super();
			this.phones = phones;
			this.emails = emails;
			this.data = data;
			this.messageCode = messageCode;
			this.scene = scene;
			this.title = title;
			this.textContent = textContent;
			this.sendType = sendType;
			this.siteId = siteId;
		}

		public String getTextContent() {
			return textContent;
		}

		public void setTextContent(String textContent) {
			this.textContent = textContent;
		}

		

		public List<String> getPhones() {
			return phones;
		}

		public void setPhones(List<String> phones) {
			this.phones = phones;
		}

		public List<String> getEmails() {
			return emails;
		}

		public void setEmails(List<String> emails) {
			this.emails = emails;
		}

		public JSONObject getData() {
			return data;
		}

		public void setData(JSONObject data) {
			this.data = data;
		}

		public Integer getTargetType() {
			return targetType;
		}

		public void setTargetType(Integer targetType) {
			this.targetType = targetType;
		}

		public Integer getOrgId() {
			return orgId;
		}

		public void setOrgId(Integer orgId) {
			this.orgId = orgId;
		}

		public Integer getMemberGroupId() {
			return memberGroupId;
		}

		public void setMemberGroupId(Integer memberGroupId) {
			this.memberGroupId = memberGroupId;
		}

		public Integer getMemberLevelId() {
			return memberLevelId;
		}

		public void setMemberLevelId(Integer memberLevelId) {
			this.memberLevelId = memberLevelId;
		}

		public List<Integer> getReceiveIds() {
			return receiveIds;
		}

		public void setReceiveIds(List<Integer> receiveIds) {
			this.receiveIds = receiveIds;
		}

		public String getMessageCode() {
			return messageCode;
		}

		public void setMessageCode(String messageCode) {
			this.messageCode = messageCode;
		}

		public MessageSceneEnum getScene() {
			return scene;
		}

		public void setScene(MessageSceneEnum scene) {
			this.scene = scene;
		}

		public Short getSendType() {
			return sendType;
		}

		public void setSendType(Short sendType) {
			this.sendType = sendType;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Integer getSiteId() {
			return siteId;
		}

		public void setSiteId(Integer siteId) {
			this.siteId = siteId;
		}
	}

	public MessageInfo(Integer targetType, Integer orgId, Integer memberGroupId, Integer memberLevelId,
			List<Integer> receiveIds, String messageCode, MessageSceneEnum scene, String title, String textContent,
			List<String> phones, List<String> emails, JSONObject data, Short sendType, Integer siteId) {
		super();
		this.targetType = targetType;
		this.orgId = orgId;
		this.memberGroupId = memberGroupId;
		this.memberLevelId = memberLevelId;
		this.setReceiveIds(receiveIds);
		this.messageCode = messageCode;
		this.scene = scene;
		this.title = title;
		this.textContent = textContent;
		this.phones = phones;
		this.emails = emails;
		this.data = data;
		this.sendType = sendType;
		this.siteId = siteId;
	}

	public Integer getTargetType() {
		return targetType;
	}

	public void setTargetType(Integer targetType) {
		this.targetType = targetType;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getMemberGroupId() {
		return memberGroupId;
	}

	public void setMemberGroupId(Integer memberGroupId) {
		this.memberGroupId = memberGroupId;
	}

	public Integer getMemberLevelId() {
		return memberLevelId;
	}

	public void setMemberLevelId(Integer memberLevelId) {
		this.memberLevelId = memberLevelId;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public MessageSceneEnum getScene() {
		return scene;
	}

	public void setScene(MessageSceneEnum scene) {
		this.scene = scene;
	}

	

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public Short getSendType() {
		return sendType;
	}

	public void setSendType(Short sendType) {
		this.sendType = sendType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public List<Integer> getReceiveIds() {
		return receiveIds;
	}

	public void setReceiveIds(List<Integer> receiveIds) {
		this.receiveIds = receiveIds;
	}

}
