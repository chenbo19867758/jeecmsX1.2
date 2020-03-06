/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.constants;

/**
 * 消息模板code常量
 * 
 * @author: tom
 * @date: 2018年8月29日 上午11:05:35
 */
public class MessageTplCodeConstants {

	/** 用户注册成功服务 */
	public static final String USER_REGISTER_TPL = "userRegister";
	/** 用户注册验证码模板 */
	public static final String USER_REGISTER_VALIDATE_CODE_TPL = "userRegisterValidateCodeTpl";
	/** 身份验证 */
	public static final String VALIDATE_USER_INFO_TPL = "validateUserInfoTpl";
	/** 验证新号码(手机或邮箱) */
	public static final String VALIDATE_NEW_USER_INFO_TPL = "validateNewUserInfoTpl";
	/** 用户修改消息 */
	public static final String USER_UPDATE_NEWS_TPL = "userUpdateNewsTpl";
	/** 用户密码找回验证码模板 */
	public static final String USER_RECOVERY_VALIDATE_CODE_TPL = "userRecoveryValidateCodeTpl";
	/** 用户登录验证码模板 */
	public static final String USER_LOGIN_VALIDATE_CODE_TPL = "userLoginValidateCodeTpl";

	/** 内容审核消息模板-待审核 */
	public static final String CONTENT_TPL_TO_DEAL = "todealContentTpl";
	/** 内容审核消息模板-审核通过 */
	public static final String CONTENT_TPL_PASS = "passContentTpl";
	/** 内容审核消息模板-审核不通过 */
	public static final String CONTENT_TPL_REJECT = "rejectContentTpl";
	/**
	 * 消息模板占位符
	 */
	public static final String PLACE_HOLDER_CONTENT_TITLE = "title";
	
	/** 会员注册审核通过 */
	public static final String MEMBER_AUDIT_PASS_TPL = "memberAuditPassTpl";
	/** 会员注册审核不通过 */
	public static final String MEMBER_AUDIT_UNPASS_TPL = "memberAuditUnPassTpl";

}
