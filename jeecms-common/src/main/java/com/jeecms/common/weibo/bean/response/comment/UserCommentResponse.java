/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.response.comment;

import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.weibo.bean.response.BaseResponse;
import com.jeecms.common.weibo.bean.response.user.WeiboUserResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 评论返回数据
 * 
 * @author: ljw
 * @date: 2019年6月18日 下午3:05:37
 */
public class UserCommentResponse extends BaseResponse {

	/** 评论创建时间 **/
	@XStreamAlias("created_at")
	private String createdAt;
	/** 评论的ID **/
	private Long id;
	/** 评论的内容 **/
	private String text;
	/** 评论的来源 **/
	private String source;
	/** 评论作者的用户信息字段 **/
	private WeiboUserResponse user;
	/** 评论的MID **/
	private String mid;
	/** 字符串型的评论ID **/
	private String idstr;
	/** 评论的微博信息字段 **/
	private JSONObject status;
	/** 评论来源评论，当本评论属于对另一评论的回复时返回此字段 **/
	@XStreamAlias("reply_comment")
	private JSONObject replyComment;

	public UserCommentResponse() {
		super();
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public WeiboUserResponse getUser() {
		return user;
	}

	public void setUser(WeiboUserResponse user) {
		this.user = user;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public Object getStatus() {
		return status;
	}

	public void setStatus(JSONObject status) {
		this.status = status;
	}

	public JSONObject getReplyComment() {
		return replyComment;
	}

	public void setReplyComment(JSONObject replyComment) {
		this.replyComment = replyComment;
	}
	
	/**获取微博ID**/
	@Transient
	public Long getWeiboId() {
		return status.getLong("id");
	}
	
	/**获取微博内容**/
	@Transient
	public String getWeiboContent() {
		return status.getString("text");
	}

	/**
	 * 得到回复
	* @Title: getReply 
	* @return
	 */
	@Transient
	public Long getReply() {
		Long obj = null;
		if (replyComment != null) {
			Long reply = replyComment.getLong("rootid");
			return reply;
		}
		return obj;
	}

}
