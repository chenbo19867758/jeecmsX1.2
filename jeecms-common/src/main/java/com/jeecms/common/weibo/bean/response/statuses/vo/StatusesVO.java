/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.weibo.bean.response.statuses.vo;

import java.util.List;

import com.jeecms.common.weibo.bean.response.comment.vo.CommentVO;
import com.jeecms.common.weibo.bean.response.user.WeiboUserResponse;

/**
 * @ 我的微博VO
 * @author: ljw
 * @date: 2019年6月20日 上午8:59:53
 */
public class StatusesVO {

	/** 微博ID **/
	private Long id;
	/** 微博作者的用户信息字段 **/
	private WeiboUserResponse user;
	/** 微博内容 **/
	private String text;
	/**评论列表**/
	private List<CommentVO> comments;
	/**评论总数**/
	private Integer totalNumber;
	/**发布微博时间**/
	private String createTime;

	public StatusesVO() {}
	
	/**
	 * 构造函数
	 */
	public StatusesVO(Long id, String screenName, String profileImageUrl, String text, Integer totalNumber) {
		this.id = id;
		this.text = text;
		this.totalNumber = totalNumber;
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
	
	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public List<CommentVO> getComments() {
		return comments;
	}

	public void setComments(List<CommentVO> comments) {
		this.comments = comments;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public WeiboUserResponse getUser() {
		return user;
	}

	public void setUser(WeiboUserResponse user) {
		this.user = user;
	}
	
}
