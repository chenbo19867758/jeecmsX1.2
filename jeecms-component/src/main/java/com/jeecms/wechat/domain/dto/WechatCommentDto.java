/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.domain.dto;

/**
 * 留言Dto
 * @author: ljw
 * @date: 2019年6月3日 上午11:23:48
 */
public class WechatCommentDto {

	/** 留言ID **/
	private Integer id;
	/** 回复内容 **/
	private String reply;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

}
