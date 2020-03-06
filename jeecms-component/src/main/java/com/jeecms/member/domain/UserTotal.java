/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * 用户投稿评论累计表
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-09-23
 */
@Entity
@Table(name = "jc_user_total")
public class UserTotal implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 用户ID */
	private Integer userId;
	/** 投稿数 */
	private Integer contributorNum;
	/** 评论数 */
	private Integer commentNum;
	/** 用户是否完善信息 */
	private Boolean userPerfect;

	public UserTotal() {
	}
	
	/**构造函数**/
	public UserTotal(Integer userId, Integer contributorNum, Integer commentNum) {
		this.userId = userId;
		this.contributorNum = contributorNum;
		this.commentNum = commentNum;
		this.userPerfect = false;
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_user_total", pkColumnValue = "jc_user_total", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_user_total")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "user_id", nullable = false, length = 11)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "contributor_num", nullable = true, length = 11)
	public Integer getContributorNum() {
		return contributorNum;
	}

	public void setContributorNum(Integer contributorNum) {
		this.contributorNum = contributorNum;
	}

	@Column(name = "comment_num", nullable = true, length = 11)
	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	@Column(name = "user_perfect")
	public Boolean getUserPerfect() {
		return userPerfect;
	}

	public void setUserPerfect(Boolean userPerfect) {
		this.userPerfect = userPerfect;
	}
}