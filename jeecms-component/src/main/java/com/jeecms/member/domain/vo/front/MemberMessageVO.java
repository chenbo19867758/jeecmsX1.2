package com.jeecms.member.domain.vo.front;

import java.io.Serializable;

/**
 * @Description:会员消息ViewModel
 * @author: ztx
 * @date: 2018年11月19日 下午5:34:48
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MemberMessageVO implements Serializable {
	private static final long serialVersionUID = 2828118646509484452L;
	/** 未读消息数 */
	private Long unreadNum;

	public MemberMessageVO() {
		super();
	}

	public MemberMessageVO(Long unreadNum) {
		super();
		this.unreadNum = unreadNum;
	}

	public Long getUnreadNum() {
		return unreadNum;
	}

	public void setUnreadNum(Long unreadNum) {
		this.unreadNum = unreadNum;
	}
	
	@Override
	public String toString() {
		return "MemberMessageVo.java";
	}
}
