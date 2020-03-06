/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.response.comment.vo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jeecms.common.weibo.bean.response.user.WeiboUserResponse;

/**
 * 评论的VO
 * 
 * @author: ljw
 * @date: 2019年6月20日 上午10:24:54
 */
public class CommentVO {

	/**微博ID**/
	private Long weiboId;
	/**微博内容**/
	private String weiboContent;
	/** 评论ID **/
	private Long commentId;
	/** 评论内容 **/
	private String commentText;
	/** 评论作者的用户信息字段 **/
	private WeiboUserResponse user;
	/**评论创建时间**/
	private Date createTime;
	/**评论的回复**/
	private List<ReplyVO> replys;
	/**来源评论ID**/
	private Long replyCommentId;
	/**评论总数**/
	private Integer totalNumber;
	
	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	/** 应前端请求，将账户用空格隔开 **/
	public String getCommentText() {
		StringBuilder str = new StringBuilder();
		if (commentText != null && commentText.length() > 0) {
			if (commentText.contains(":")) {
				Integer index = commentText.indexOf(":");
				//截取前半部分
				commentText = str.append(commentText.subSequence(0, index))
				.append(" ")
				.append(commentText.substring(commentText.indexOf(":")))
				.toString();
			}
		}
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getWeiboId() {
		return weiboId;
	}

	public void setWeiboId(Long weiboId) {
		this.weiboId = weiboId;
	}

	public String getWeiboContent() {
		return weiboContent;
	}

	public void setWeiboContent(String weiboContent) {
		this.weiboContent = weiboContent;
	}

	public WeiboUserResponse getUser() {
		return user;
	}

	public void setUser(WeiboUserResponse user) {
		this.user = user;
	}

	public List<ReplyVO> getReplys() {
		return replys;
	}

	public void setReplys(List<ReplyVO> replys) {
		this.replys = replys;
	}

	public Long getReplyCommentId() {
		return replyCommentId;
	}

	public void setReplyCommentId(Long replyCommentId) {
		this.replyCommentId = replyCommentId;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}
	
}
