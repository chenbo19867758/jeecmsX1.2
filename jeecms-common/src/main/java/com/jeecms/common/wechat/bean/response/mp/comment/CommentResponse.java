/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.wechat.bean.response.mp.comment;

import java.util.List;

import com.jeecms.common.wechat.bean.response.mp.BaseResponse;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 留言列表返回对象
 * @author: ljw
 * @date: 2019年5月29日 上午11:50:58
 */
public class CommentResponse extends BaseResponse {

	/** 总数，非comment的size around **/
	private Long total;
	/** 评论对象 **/
	private List<Comment> comment;

	public CommentResponse() {
	}

	public class Comment {

		/** 用户评论ID **/
		@XStreamAlias("user_comment_id")
		private Long userCommentId;
		/** openId */
		private String openId;
		/** 评论时间 **/
		@XStreamAlias("create_time")
		private Long createTime;
		/** 评论内容 **/
		private String content;
		/** 是否精选评论，0为即非精选，1为true，即精选 **/
		@XStreamAlias("comment_type")
		private Integer commentType;
		/** 回复对象 **/
		private Reply reply;

		public Comment() {
		}

		public Long getUserCommentId() {
			return userCommentId;
		}

		public void setUserCommentId(Long userCommentId) {
			this.userCommentId = userCommentId;
		}

		public Long getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Long createTime) {
			this.createTime = createTime;
		}

		public Integer getCommentType() {
			return commentType;
		}

		public void setCommentType(Integer commentType) {
			this.commentType = commentType;
		}

		public String getOpenId() {
			return openId;
		}

		public void setOpenId(String openId) {
			this.openId = openId;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public Reply getReply() {
			return reply;
		}

		public void setReply(Reply reply) {
			this.reply = reply;
		}

	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

}
