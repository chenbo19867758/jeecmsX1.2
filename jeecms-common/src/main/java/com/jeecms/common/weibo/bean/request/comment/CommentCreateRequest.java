/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.request.comment;

import com.jeecms.common.weibo.bean.request.BaseRequest;

/**
 * 对一条微博进行评论
 * 
   @url https://api.weibo.com/2/comments/create.json
 * @author: ljw
 * @date: 2019年6月20日 上午11:58:21
 */
public class CommentCreateRequest extends BaseRequest {

	/** 评论内容，必须做URLencode，内容不超过140个汉字。必填 **/
	private String comment;
	/** 需要评论的微博ID。必填 **/
	private Long id;
	/** 当评论转发微博时，是否评论给原微博，0：否、1：是，默认为0。 **/
	private Integer commentOri;
	/** 开发者上报的操作用户真实IP，形如：211.156.0.1。 **/
	private String rip;

	public CommentCreateRequest() {
		super();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCommentOri() {
		return commentOri;
	}

	public void setCommentOri(Integer commentOri) {
		this.commentOri = commentOri;
	}

	public String getRip() {
		return rip;
	}

	public void setRip(String rip) {
		this.rip = rip;
	}

}
