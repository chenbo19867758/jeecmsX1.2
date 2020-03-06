/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.bean.request.mp.comment;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 关闭已群发文章评论 && 打开已群发文章评论请求对象
 * 
 * @link https://api.weixin.qq.com/cgi-bin/comment/close?access_token=ACCESS_TOKEN
 * @link https://api.weixin.qq.com/cgi-bin/comment/open?access_token=ACCESS_TOKEN
 *       http请求方式: POST（请使用https协议）
 * @author: ljw
 * @date: 2019年5月29日 上午10:44:25
 */
public class CommentRequest {

	/** 群发返回的msg_data_id,必填 **/
	@XStreamAlias("msg_data_id")
	private Long msgDataId;
	/** 多图文时，用来指定第几篇图文，从0开始，不带默认操作该msg_data_id的第一篇图文，非必填 **/
	private Long index;

	public CommentRequest() {
	}

	public Long getMsgDataId() {
		return msgDataId;
	}

	public void setMsgDataId(Long msgDataId) {
		this.msgDataId = msgDataId;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

}
