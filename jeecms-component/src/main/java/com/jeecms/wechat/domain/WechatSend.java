/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.wechat.Const;
import com.jeecms.wechat.domain.vo.MaterialVO;

/**
 * 微信群发设置
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-05-28
 */
@Entity
@Table(name = "jc_wechat_send")
public class WechatSend extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 群发对象标签ID **/
	private Integer tagId;
	/** 所属公众号appId */
	private String appId;
	/** 素材ID */
	private Integer materialId;
	/** 素材JSON */
	private String materialJson;
	/** 发送日期 */
	private Date sendDate;
	/** 发送小时（0至23） */
	private Integer sendHour;
	/** 发送分钟（0至59） */
	private Integer sendMinute;
	/** 发送状态 1-未发送 2-发送成功 3-发送失败 */
	private Integer status;
	/** 群发返回的msg_data_id,评论时需要用到该字段 **/
	private String msgDataId;
	/** 发送类型（1立即发送2.定时发送） **/
	private Integer type;
	/** 素材扩展 **/
	private MaterialVO material;
	/** 图文素材扩展 **/
	private JSONArray arrays;

	/** 素材 **/
	private WechatMaterial wechatMaterial;
	/** 粉丝标签 **/
	private WechatFansTag tag;

	public WechatSend() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_wechat_send", pkColumnValue = "jc_wechat_send", 
			initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_send")
	@Override
	public Integer getId() {
		return id;
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

	@Column(name = "material_id", nullable = false, length = 11)
	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	@Column(name = "send_date", nullable = false, length = 10)
	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	@Column(name = "send_hour", nullable = false, length = 11)
	public Integer getSendHour() {
		return sendHour;
	}

	public void setSendHour(Integer sendHour) {
		this.sendHour = sendHour;
	}

	@Column(name = "tag_id", nullable = true, length = 11)
	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	@Column(name = "status", nullable = true, length = 6)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "material_id", insertable = false, updatable = false)
	public WechatMaterial getWechatMaterial() {
		return wechatMaterial;
	}

	public void setWechatMaterial(WechatMaterial wechatMaterial) {
		this.wechatMaterial = wechatMaterial;
	}

	@Column(name = "msg_data_id", length = 50)
	public String getMsgDataId() {
		return msgDataId;
	}

	public void setMsgDataId(String msgDataId) {
		this.msgDataId = msgDataId;
	}

	@Column(name = "material_json", nullable = false)
	public String getMaterialJson() {
		return materialJson;
	}

	public void setMaterialJson(String materialJson) {
		this.materialJson = materialJson;
	}

	@Column(name = "send_minute", nullable = false, length = 11)
	public Integer getSendMinute() {
		return sendMinute;
	}

	public void setSendMinute(Integer sendMinute) {
		this.sendMinute = sendMinute;
	}

	/**
	 * 获得时间
	 * 
	 * @Title: getDateString
	 * @return
	 */
	@Transient
	public Date getDateString() {
		StringBuilder builder = new StringBuilder();
		String date = MyDateUtils.formatDate(getSendDate());
		String dates = builder.append(date).append(" ")
				.append(getSendHour()).append(":").append(getSendMinute())
				.toString();
		Date sb = MyDateUtils.parseDate(dates, MyDateUtils.COM_Y_M_D_H_M_PATTERN);
		return sb;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id", insertable = false, updatable = false)
	public WechatFansTag getTag() {
		return tag;
	}

	public void setTag(WechatFansTag tag) {
		this.tag = tag;
	}

	@Transient
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/** 获取标签信息 **/
	@Transient
	public String getPushName() {
		if (tag != null) {
			return tag.getTagName();
		}
		return "全部用户";
	}

	@Transient
	public MaterialVO getMaterial() {
		return material;
	}

	public void setMaterial(MaterialVO material) {
		this.material = material;
	}

	@Transient
	public JSONArray getArrays() {
		return arrays;
	}

	public void setArrays(JSONArray arrays) {
		this.arrays = arrays;
	}

}