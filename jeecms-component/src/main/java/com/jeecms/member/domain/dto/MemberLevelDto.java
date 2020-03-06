package com.jeecms.member.domain.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.jeecms.member.domain.MemberLevel;

/**
 * 
 * @Description: 会员等级信息DTO
 * @author: ztx
 * @date: 2018年6月7日 上午11:19:15
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MemberLevelDto {

	/** 会员等级 */
	private MemberLevel memberLevel;

	public MemberLevelDto(MemberLevel memberLevel) {
		this.memberLevel = memberLevel;
	}

	@NotNull
	@Valid
	public MemberLevel getMemberLevel() {
		return memberLevel;
	}

}
