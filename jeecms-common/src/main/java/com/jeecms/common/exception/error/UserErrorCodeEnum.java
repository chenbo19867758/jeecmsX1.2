package com.jeecms.common.exception.error;

import com.jeecms.common.exception.ExceptionInfo;

/**
 * 用户信息枚举 号段范围 14001~14500
 * 
 * @author: tom
 * @date: 2018年11月13日 上午9:54:44
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public enum UserErrorCodeEnum implements ExceptionInfo {

	/** 邮箱已经存在 */
	EMAIL_ALREADY_EXIST("14001", "email already exist!"),
	/** 电话号码已经存在 */
	PHONE_ALREADY_EXIST("14002", "phone already exist!"),
	/** 等级会员名称已经存在 */
	MEMBERLEVEL_NAME_ALREADY_EXIST("14006", "memberLevelName already exist!"),
	/** 账户密码错误、默认提示信息。 */
	ACCOUNT_CREDENTIAL_ERROR("14008", "account credential error !"),
	/** 两次密码填写不一致 */
	PASSWORD_INCONSISTENT_ERROR("14009", "Password inconsistent error !"),
	/** 账户已经存在 **/
	USERNAME_ALREADY_EXIST("14010", "username already exist!"),
	/** 验证码已经发送过 */
	VALIDATE_CODE_ALREADY_SEND("14011", "validateCode is already send!"),
	/** 验证码已经超出次数(发送) */
	VALIDATE_CODE_EXCEEDCOUNT("14012", "validateCode already exceedcount!"),
	/** 验证码未通过 */
	VALIDATE_CODE_UNTHROUGH("14013", "validateCode is unthrough!"),
	/** 验证码不合法 */
	VALIDATE_CODE_ILLEGAL("14014", "validateCode is illegal!"),
	/** 邮箱地址无效 */
	EMAIL_INVALID("14015", "invalid e-mail address!"),
	/** 电话号码无效 */
	PHONE_INVALID("14016", "invalid phone number!"),
	/** 手机号未绑定会员 */
	PHONE_UNBOUND_MEMBER("14017", "Mobile phone number unbound member!"),
	/** 邮箱未绑定会员 */
	EMAIL_UNBOUND_MEMBER("14018", "Email unbound member!"),
	/** 组织不可为空 */
	ORG_CANNOT_EMPTY("14019", "Organization cannot be empty!"),
	/** 非法操作 */
	ILLEGAL_OPERATION("14020", "Illegal operation!"),
	/** 组织名称不可为空 */
	ORGNAME_CANNOT_EMPTY("14021", "Organization name cannot be empty!"),
	/** 密码格式不正确 */
	PASSWORD_FORMAT_IS_INCORRECT("14022", "Password format is incorrect!"),
	/** 存在不可操作数据 **/
	ALREADY_DATA_NOT_OPERATION("14023", "There is unworkable data"),
	/** 组织名称已经存在 */
	ORGNAME_ALREADY_EXIST("14024", "The organization name already exists!"),
	/** 用户必须是管理员*/
	USER_MUST_BE_AN_ADMINISTRATOR("14025", "User must be an administrator"),
	/** 邮箱格式错误*/
	EMAIL_FORM_ERROR("14026","Email from error"),
	/** 短信格式错误*/
	SMS_FORM_ERROR("14027","Sms from error"),
	/** 当前用户无权操作公众号、小程序及授权微博账号*/
	NO_OPERATE_WECHAT_PERMISSION("14028","you is not operate permission"),
	/** 用户已绑定第三方信息 */
	USER_ALREADY_BINDED_THIEDPARTY("14029", "user already binded thirdparty!"),
	/** 绑定第三方失败 */
	THIRDPARTY_BINDING_FAIL("14030", "Failure to bind a third party!"),
	/** 未找到第三方账号信息 */
	THIRDPARTY_INFO_NOTFOUND("14031", "No third party information was found!"),
	/** 未成功绑定第三方账号，请绑定后重试*/
	THIRDPARTY_INFO_UNSUCCESSFUL_BINDING("14032", "The third party account has not been "
			+ "successfully bound, please bind and try again!"),
	/** 该内容不允许评论*/
	THE_CONTENT_NOT_COMMENT("14033","The content not comment!"),
	/** 该内容不允许游客评论*/
	THE_CONTENT_NOT_TOURIST_REVIEWS("14034","The content not tourist reviews!"),
	/** 传入的内容错误*/
	INCOMING_CONTENT_ERROR("14035","Incoming content error!"),
	/** 该评论包含敏感词无法发布*/
	THE_COMMENT_CONTAIN_SENSITIVE_WORD("14036","The comment contain sensitive word!"),
	/** 该评论无法继续发送*/
	THE_COMMENT_UNABLE_TO_SEND("14037","The comment unable to send!"),
	/** 该评论包含链接无法发布*/
	THE_COMMENT_CONTAIN_LINK("14038","The comment contain link!"),
	/** 系统检测您为后台管理员，请到后台修改密码*/
	MOVE_TO_BACKGROUND_UPDATE("14039", "The system detects that you are the background administrator,"
			+ " please go to the background to change the password!"),
	/** 该用户不允许评论*/
	THE_USER_NOT_COMMENT("14040","The user not comment!"),
	/** 该ip不允许评论*/
	THE_IP_NOT_COMMENT("14041","The ip not comment!"),
	/** 用户未登录*/
	THE_USER_NOT_LOGIN("14042","The user not login!"),
	/** 新密码不能与原密码相同*/
	PASSWORD_SAME_OLD_ERROR("14043", "The new password cannot be the same as the original password !"),
	/** 密码重置无效，不能重置自身的密码*/
	PASSWORD_ERROR_RESET_MYSELF("14044", "Password reset invalid, cannot reset its own password !"),
	/** 传入的微信、小程序错误*/
	INCOMING_WECHAT_ERROR("14045","Incoming whchat error"),
	;
	
	/** 异常代码。 */
	private String code;

	/** 异常对应的默认提示信息。 */
	private String defaultMessage;

	/** 异常对应的原始提示信息。 */
	private String originalMessage;

	/** 当前请求的URL。 */
	private String requestUrl;

	/** 需转向（重定向）的URL，默认为空。 */
	private String defaultRedirectUrl = "";

	/** 异常对应的响应数据。 */
	private Object data = new Object();

	/**
	 * Description: 根据异常的代码、默认提示信息构建一个异常信息对象。
	 *
	 * @param code           异常的代码。
	 * 
	 * @param defaultMessage 异常的默认提示信息。
	 */
	UserErrorCodeEnum(String code, String defaultMessage) {
		this.code = code;
		this.defaultMessage = defaultMessage;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDefaultMessage() {
		return defaultMessage;
	}

	@Override
	public String getOriginalMessage() {
		return originalMessage;
	}

	@Override
	public void setOriginalMessage(String originalMessage) {
		this.originalMessage = originalMessage;
	}

	@Override
	public String getRequestUrl() {
		return requestUrl;
	}

	@Override
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	@Override
	public String getDefaultRedirectUrl() {
		return defaultRedirectUrl;
	}

	@Override
	public void setDefaultRedirectUrl(String defaultRedirectUrl) {
		this.defaultRedirectUrl = defaultRedirectUrl;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		this.data = data;
	}

}
