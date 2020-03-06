/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.util.ArrayList;
/**
 * 站内信信息DTO
 * @author: ljw
 * @date:   2019年1月23日 上午10:31:39     
 */
import java.util.List;

public class SysMessageDto {

	// TODO 接受对象类型为4时，传递的为用户id集合
	
	/** 信息标题 */
	private String title;
	/** 内容 */
	private String content;
	/**发送对象IDs**/
	private List<Integer> sendIds = new ArrayList<Integer>(10);
	/** 接收对象类型  1-全部  2-全部管理员   3-全部会员   4-组织  5-指定管理员  6-会员等级   7-会员组  8-指定会员 */
	private Integer recTargetType;
	/**组织ID**/
	private Integer orgId;
	/** 会员等级id */
	private Integer memeberLevelId;
	/** 会员组id */
	private Integer memeberGroupId;
	/** 会员id */
	private List<Integer> memeberId;
	/** 用户id */
	private List<Integer> userId;

	public SysMessageDto() {
	}

	/** 接受对象类型：1,2,3*/
	public SysMessageDto(String title, String content, Integer recTargetType) {
		super();
		this.title = title;
		this.content = content;
		this.recTargetType = recTargetType;
	}

	/** 接受对象类型：4,5*/
	public SysMessageDto(String title, String content, Integer recTargetType, List<Integer> userId) {
		super();
		this.title = title;
		this.content = content;
		this.recTargetType = recTargetType;
		this.userId = userId;
	}

	/** 接受对象类型：6,7,8*/
	public SysMessageDto(String title, String content, Integer recTargetType, Integer memeberLevelId,
			Integer memeberGroupId, List<Integer> memeberId, List<Integer> userId) {
		super();
		this.title = title;
		this.content = content;
		this.recTargetType = recTargetType;
		this.memeberLevelId = memeberLevelId;
		this.memeberGroupId = memeberGroupId;
		this.memeberId = memeberId;
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getRecTargetType() {
		return recTargetType;
	}

	public void setRecTargetType(Integer recTargetType) {
		this.recTargetType = recTargetType;
	}

	public Integer getMemeberGroupId() {
		return memeberGroupId;
	}

	public void setMemeberGroupId(Integer memeberGroupId) {
		this.memeberGroupId = memeberGroupId;
	}

	public List<Integer> getMemeberId() {
		return memeberId;
	}

	public void setMemeberId(List<Integer> memeberId) {
		this.memeberId = memeberId;
	}

	public List<Integer> getUserId() {
		return userId;
	}

	public void setUserId(List<Integer> userId) {
		this.userId = userId;
	}

	public Integer getMemeberLevelId() {
		return memeberLevelId;
	}

	public void setMemeberLevelId(Integer memeberLevelId) {
		this.memeberLevelId = memeberLevelId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public List<Integer> getSendIds() {
		return sendIds;
	}

	public void setSendIds(List<Integer> sendIds) {
		this.sendIds = sendIds;
	}

}
