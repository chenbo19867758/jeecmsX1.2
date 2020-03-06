/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.response.comment.vo;

import java.util.Date;

/**
 * 回复VO
 * 
 * @author: ljw
 * @date: 2019年7月29日 上午11:30:42
 */
public class ReplyVO {

	/** 评论ID **/
	private Long commentId;
	/** 回复内容 **/
	private String commentText;
	/**评论创建时间**/
	private Date createTime;
	/** 回复作者昵称 **/
	private String screenName;

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
