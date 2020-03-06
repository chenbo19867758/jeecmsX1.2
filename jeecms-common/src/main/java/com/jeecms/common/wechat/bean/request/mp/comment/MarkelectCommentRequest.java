/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.bean.request.mp.comment;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 将评论标记精选 && 取消评论精选请求对象 && 删除评论 && 删除回复
 * 
 * @link https://api.weixin.qq.com/cgi-bin/comment/markelect?access_token=ACCESS_TOKEN
 * @link https://api.weixin.qq.com/cgi-bin/comment/unmarkelect?access_token=ACCESS_TOKEN
 * @link https://api.weixin.qq.com/cgi-bin/comment/delete?access_token=ACCESS_TOKEN
 * @link https://api.weixin.qq.com/cgi-bin/comment/reply/delete?access_token=ACCESS_TOKEN
 *       http请求方式: POST（请使用https协议）
 * 
 * @author: ljw
 * @date: 2019年5月29日 上午11:19:24
 */
public class MarkelectCommentRequest {

	/** 群发返回的msg_data_id,必填 **/
	@XStreamAlias("msg_data_id")
	private Long msgDataId;
	/** 多图文时，用来指定第几篇图文，从0开始，不带默认返回该msg_data_id的第一篇图文,非必填 **/
	private Long index;
	/** 用户评论ID，必填 **/
	@XStreamAlias("user_comment_id")
	private Long userCommentId;
	/**回复内容**/
	private String content;
	

	public MarkelectCommentRequest() {
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

	public Long getUserCommentId() {
		return userCommentId;
	}

	public void setUserCommentId(Long userCommentId) {
		this.userCommentId = userCommentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
