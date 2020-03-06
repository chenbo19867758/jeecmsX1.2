/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *          仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

/**
 * 发送第三方验证码常量
 * @author: ljw
 * @date: 2019年7月16日 下午3:13:18
 */
public class ValidateCodeConstants {

	/** 新创建 */
	public static final int STATUS_NEW = 1;
	/** 通过(这个值的意义不大,但约定了通过则返回0) */
	public static final int STATUS_PASS = 0;
	/** 已过期 */
	public static final int STATUS_EXPIRED = -1;
	/** 未过期 */
	public static final int STATUS_UNEXPIRED = -2;
	/** 超出发送次数 */
	public static final int STATUS_EXCEEDCOUNT = -3;
	/** 未通过 */
	public static final int STATUS_UNTHROUGH = -4;
	/** 不合法 */
	public static final int STATUS_ILLEGAL = -5;

	/** 默认消息过期时间(单位:秒) */
	public static final long DEFAULT_EXPIRE_TIME = 600L;
	/** 重发时间(单位:秒) */
	public static final long DEFAULT_REPLY_TIME = 60L;
	/** 最大重发次数 */
	public static final int MAX_RESEND_COUNT = 10;
	/** 默认验证码长度 */
	public static final int DEFAULT_VALIDATE_CODE_LENGTH = 6;

	// 操作标识
	/** 用户注册 */
	public static final int CODE_TYPE_REGISTER = 1;
	/** 密码找回 */
	public static final int CODE_TYPE_RETRIEVE_PASSWORD = 2;
	/** 身份验证 */
	public static final int CODE_TYPE_VALIDATE_USER_INFOR = 3;
	/** 验证新号码(手机或邮箱) */
	public static final int CODE_TYPE_NEW_VALIDATE_USER_INFOR = 4;
	/** 用户登录(根据手机号) */
	public static final int CODE_TYPE_MEMBER_LOGIN_PHONE = 5;
	/** 更换登录密码 */
	public static final int CODE_TYPE_UPDATE_PASSWORD = 6;
	/** 邮箱验证 */
	public static final int CODE_TYPE_EMAIL_BINDING = 7;
	/** 手机验证 */
	public static final int CODE_TYPE_PHONE_BINDING = 8;
	/** 更换支付密码 */
	public static final int CODE_TYPE_UPDATE_PAY_PASSWORD = 9;

	// 验证码二级前缀
	/** 会员注册 */
	public static final String CODE_SECOND_LEVEL_IDENTITY_REGISTER = "register_";
	/** 找回密码 */
	public static final String CODE_SECOND_LEVEL_IDENTITY_RETRIEVE_PSTR = "retrieve_";
	/** 身份验证 */
	public static final String CODE_SECOND_LEVEL_IDENTITY_VALIDATE_USER_INFOR = "userInfo_";
	/** 验证新号码(手机或邮箱) */
	public static final String CODE_SECOND_LEVEL_IDENTITY_NEW_VALIDATE_USER_INFOR = "newUserInfo_";
	/*****************内容审核登录也是该标识***********************/
	/** 用户登录(使用手机号) */
	public static final String CODE_SECOND_LEVEL_IDENTITY_USER_LOGIN_PHONE = "loginPhone_";
	/****************************************/
	/** 更换登录密码 */
	public static final String CODE_SECOND_LEVEL_IDENTITY_UPDATE_PSTR = "updatePassword_";
	/** 邮箱绑定 */
	public static final String CODE_SECOND_LEVEL_IDENTITY_EMAIL_BINDING = "emailBinding_";
	/** 手机绑定 */
	public static final String CODE_SECOND_LEVEL_IDENTITY_PHONE_BINDING = "phoneBinding_";
	/** 更换支付密码 */
	public static final String CODE_SECOND_LEVEL_IDENTITY_UPDATE_PAY_PSTR = "updatePayPassword_";
	
}
