package com.jeecms.member.domain.dto;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 第三方用户绑定会员DTO
 * @author: ztx
 * @date: 2018年10月25日 下午4:12:58
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class PcMemberExtInfoDto extends PcMemberDto {
	private static final long serialVersionUID = -7124687195240072672L;
	
	/** 第三方用户唯一标识 */
	private String openId;
	/** 第三方类型sina.wechat,qq */
	private String typeCode;
	/** 第三方用户名 */
	private String nickName;
	/** 第三方用户头像 */
	private String headImgUrl;

	@NotBlank(groups = { RegisterByEmail.class, RegisterByPhone.class, BindingMember.class })
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@NotBlank(groups = { RegisterByEmail.class, RegisterByPhone.class, BindingMember.class })
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	
	@Override
	public String toString() {
		return "PCMemberExtInfoDTO.java";
	}

}
