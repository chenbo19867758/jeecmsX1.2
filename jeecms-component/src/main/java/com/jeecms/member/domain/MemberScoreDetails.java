/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.domain;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 会员积分详情
 * @author ljw
 * @version 1.0
 * @date 2019-09-23
 */
@Entity
@Table(name = "jc_member_score_details")
public class MemberScoreDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 用户ID */
	private Integer userId;
	/** 积分途径类型 1-投稿 2-评论 3-注册 4-完善信息 */
	private Integer businessType;
	/** 积分值 */
	private Integer score;
	/** 创建时间 */
	protected Date createTime;

	public MemberScoreDetails() {
	}
	
	/**构造函数**/
	public MemberScoreDetails(Integer userId, Integer businessType, Integer score) {
		this.userId = userId;
		this.businessType = businessType;
		this.score = score;
		this.createTime = new Date();
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_member_score_details", pkColumnValue = "jc_member_score_details", 
		initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_member_score_details")
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

	@Column(name = "business_type", nullable = false, length = 11)
	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	@Column(name = "score", nullable = false, length = 11)
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")  
	@Column(name = "create_time", nullable = false)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**1-投稿 **/
	public static final int CONTRIBUTOR_SCORE_TYPE = 1;
	/**2-评论**/
	public static final int COMMENT_SCORE_TYPE = 2;
	/**3-注册**/
	public static final int REGISTER_SCORE_TYPE = 3;
	/**4-完善信息**/
	public static final int MESSAGE_SCORE_TYPE = 4;
	
}