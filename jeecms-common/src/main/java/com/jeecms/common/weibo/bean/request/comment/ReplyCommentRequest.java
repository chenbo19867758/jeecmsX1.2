/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.request.comment;

/**
 * 回复评论
 * 
 * @author: ljw
 * @date: 2019年6月20日 下午2:20:45
 */
public class ReplyCommentRequest extends CommentCreateRequest {

	/** 需要回复的评论ID。 **/
	private Long cid;
	/** 回复中是否自动加入“回复@用户名”，0：是、1：否，默认为0。 **/
	private Integer withoutMention;

	public ReplyCommentRequest() {
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Integer getWithoutMention() {
		return withoutMention;
	}

	public void setWithoutMention(Integer withoutMention) {
		this.withoutMention = withoutMention;
	}

}
