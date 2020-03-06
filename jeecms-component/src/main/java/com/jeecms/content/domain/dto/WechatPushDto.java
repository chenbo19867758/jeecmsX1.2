/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.jeecms.content.domain.vo.WechatPushVo;

/**
 * 微信推送Dto
 * 
 * @author: ljw
 * @date: 2019年7月24日 下午2:23:55
 */
public class WechatPushDto {

	/** 微信公众号APPID **/
	private String appid;
	/**标签ID,不传默认全部**/
	private Integer tagId;
	/**留言设置 1.所有人都可以留言 2.仅关注后留言 3.不允许留言**/
	private Integer message;
	/** 发布内容集合 **/
	private List<WechatPushVo> vo = new ArrayList<WechatPushVo>(10);
	/**发送类型1.群发 2.定时群发**/
	private Integer type;
	/** 发送日期 */
	private Date sendDate;
	/** 发送小时（0至23） */
	private Integer sendHour;
	/** 发送分钟（0至59） */
	private Integer sendMinute;
	
	public WechatPushDto() {
	}

	@NotNull
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	@NotNull
	public Integer getMessage() {
		return message;
	}

	public void setMessage(Integer message) {
		this.message = message;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@NotEmpty
	public List<WechatPushVo> getVo() {
		return vo;
	}

	public void setVo(List<WechatPushVo> vo) {
		this.vo = vo;
	}
	
	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
	
	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public Integer getSendHour() {
		return sendHour;
	}

	public void setSendHour(Integer sendHour) {
		this.sendHour = sendHour;
	}

	public Integer getSendMinute() {
		return sendMinute;
	}

	public void setSendMinute(Integer sendMinute) {
		this.sendMinute = sendMinute;
	}

	/**留言设置 1.所有人都可以留言 2.仅关注后留言 3.不允许留言**/
	public static Integer MESSAGE_1 = 1;
	/**留言设置 1.所有人都可以留言 2.仅关注后留言 3.不允许留言**/
	public static Integer MESSAGE_2 = 2;
	/**留言设置 1.所有人都可以留言 2.仅关注后留言 3.不允许留言**/
	public static Integer MESSAGE_3 = 3;
	
	/**发送类型1.立即群发 2.定时群发**/
	public static Integer TYPE_1 = 1;
	/**发送类型1.立即群发 2.定时群发**/
	public static Integer TYPE_2 = 2;
}
