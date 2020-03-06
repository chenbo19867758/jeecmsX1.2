/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/8/17 11:01
 */

public class UpcomingVo {

	private long totalUser;
	private long totalComment;
	private long toDealCount;

	public long getTotalUser() {
		return totalUser;
	}

	public void setTotalUser(long totalUser) {
		this.totalUser = totalUser;
	}

	public long getTotalComment() {
		return totalComment;
	}

	public void setTotalComment(long totalComment) {
		this.totalComment = totalComment;
	}

	public long getToDealCount() {
		return toDealCount;
	}

	public void setToDealCount(long toDealCount) {
		this.toDealCount = toDealCount;
	}

	public UpcomingVo() {
		super();
	}

	public UpcomingVo(long totalUser, long totalComment, long toDealCount) {
		this.totalUser = totalUser;
		this.totalComment = totalComment;
		this.toDealCount = toDealCount;
	}
}
