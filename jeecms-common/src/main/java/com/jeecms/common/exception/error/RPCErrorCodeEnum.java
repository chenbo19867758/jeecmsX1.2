package com.jeecms.common.exception.error;

import com.jeecms.common.exception.ExceptionInfo;

/**
 * 远程调用API类错误码枚举(如微信、支付宝、短信)  号段范围 12500~13000
 * 调用支付宝、微信、银联等第三方机构api出现的业务错误
 * @author: wangqq
 * @date:   2018年6月25日 上午11:33:47     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public enum RPCErrorCodeEnum implements ExceptionInfo {
	/**消息校验Token输入是否符合规则 必须是长度为16位的字符串，只能是字母和数字**/
	TOKEN_INPUT_ERROR("12500", "messageValidateToken is input error!"),
	/**消息加解密Key输入是否符合规则 必须是长度为43位的字符串，只能是字母和数字**/
	KEY_INPUT_ERROR("12501", "messageDecryptKey is input error!"),
	/**今日群发次数已用尽**/
	MASS_SUBSCRIPTION_ERROR("12502", "The number of mass deliveries today has been exhausted"),
	/**本月群发次数已用尽**/
	MASS_SERVICE_ERROR("12503", "The number of mass deliveries has been exhausted this month"),
	/** 签名错误*/
	SIGN_ERROR("12504","Signature error"),
	/** 统一下单失败*/
	UNIFIEDORDER_ERROR("12505","Failed to place an order"),
	/** 余额不足 */
	NOT_SUFFICIENT_FUNDS("12506","Sorry, your credit is running low!"),
	/** 支付密码未设置 */
	PAY_PASSWORD_NOT_SET("12507","Payment password is not set!"),
	/** 支付密码错误 */
	PAY_PASSWORD_ERROR("12508","Payment password is error!"),
	/** 交易失败 */
	TRANSACTION_FAIL("12509","Transaction is failure!"),
	/**第三方调用失败*/
	THIRD_PARTY_CALL_ERROR("12510","third party call error!"),
	/**未配置第三方信息*/
	THIRD_PARTY_INFO_UNCONFIGURATION("12511","thirdParty info is unconfiguration"),
	/**邮箱地址无效*/
	EMAIL_ADDRESS_INVALID("12512","Invalid e-mail address!"),
	/**消息队列消息处理失败*/
	QUEUE_MESSAGE_HANDLER_FAILED("12513","Message queue message processing failed!"),
	/**电话号码无效*/
	PHONE_NUMBER_INVALID("12514","Invalid phone address!"),
	/**未获取到用户信息*/
	USER_INFO_NOT_OBTAINED("12515","No user info was obtained!"),
	/**未获取到用户凭证*/
	USER_CREDENTIALS_NOT_OBTAINED("12516","No user credentials were obtained!"),
	/**业务已经处理完毕*/
	BUSINESS_ALREADY_EXIST_PROCESSED("12517","business already exist processed!"),
	/** 微信已经绑定了小程序*/
	WECHAT_IS_ALREADY_BOUND_TO_THE_APPLET("12518","WeChat is already bound to the applet!"),
	
	/** 系统错误 */
	SYSTEM_ERROR("12519", "system error!"),
	/** 未配置支付插件 */
	UNCONFIG_PAYMENT_PLUGIN("12520", "No Payment Plug-in Configured!"),
	/** 保证金不能小于零 **/
	BANLANCE_NOTLESS_ZERO("12521", "The margin must not be less than zero!"),
	/** 时间设置错误 **/
	TIME_SET_ERROR("12522", "Time setting error"),
	
	/** 上传微信素材图片，提示信息。 */
	UPLOAD_IMAGE_FORMAT_ERROR("12523", "image format error !"),
	/** 上传微信素材语音，提示信息。 */
	UPLOAD_VOICE_FORMAT_ERROR("12524", "voice format error !"),
	/** 上传微信素材略缩图，提示信息 */
	UPLOAD_THUMB_FORMAT_ERROR("12525", "thumb format error !"),
	/** 上传微信素材视频，提示信息 */
	UPLOAD_VIDEO_FORMAT_ERROR("12526", "video format error !"),
	/** 参数结构不能为空 */
	PARAMETER_NOTNULL("12527", "pushParams is already exist!"),
	
	/** 回复内容不能为空 */
	CONTENT_NOTNULL("12528", "Content is already exist!"),
	/** 缩略图不能为空(音乐消息) */
	THUMBMEDIAID_NOTNULL("12529", "ThumbMediaId is already exist!"),
	/** 素材不能为空(媒体消息) */
	MEDIAID_NOTNULL("12530", "mediaId is already exist!"),
	/** 标签名称已经存在**/
	WECHAT_TAGNAME_ALREADY_EXISTED("12531","The label name already exists!"),
	/** 参数错误，站点不允许为空*/
	SITE_CANNOT_BE_EMPTY("12532","Site cannot be empty"),
	/** 该小程序、公众号已绑定了某站点下，无法继续绑定在其它站点下(ps:此处需要配置国际化)*/
	THE_WECHAT_OR_APPLET_IS_ALREADY_BOUND("12533","The wechat or applet is already bound"),
	/** 该公众号或小程序不在该站点下*/
	WECHAT_OR_APPLET_ARE_NOT_UNDER_THE_SITE("12534","WeChat and or applet are not under the site"),
	/** 微信回复内容已存在*/
	WECHAT_REPLY_ALREADY_EXIST("12535","WeChat replies already exist!"),
	/** 上传的文件错误*/
	UPLOAD_FILE_ERROR("12536","upload file error"),
	/** 请求类型错误，只能是图片、视频、语音*/
	REQUEST_TYPE_ERROR("12537","request type error"),
	/** 默认状态下无法取消授权*/
	UNABLE_TO_DEAUTHORIZE_BY_DEFAULT("12538","Unable to deauthorize by default"),
	/** 传入的id类型不对*/
	INCOMING_ID_TYPE_IS_INCORRECT("12539", "Incoming id type is incorrect"),
	/** 微信菜单状态不对，请检查状态后重试*/
	WECHAT_MENU_STATUS_IS_WRONG("12540","WeChat menu status is wrong"),
	/** 小程序模板无法转换成模板*/
	TEMPLATE_CANNOT_BE_CONVERTED("12541","Template cannot be converted"),
	/** 小程序模板被设置为最新无法删除*/
	TEMPLATE_CANNOT_BE_DELETED("12542","Template cannot be deleted"),
	/** 没有设置最新模板，请设置后重试*/
	NO_LATEST_TEMPLATE_SET("12543","No latest template set"),
	/** 上线版本状态错误*/
	ONLINE_VERSION_STATUS_ERR("12544","Online version status error"),
	/** 站点已经绑定了小程序，无法继续绑定，请解绑后重试*/
	THE_SITE_HAS_BEEN_BOUND_TO_THE_APPLET("12545","The site has been bound to the applet"),
	/** 推送内容最多不能超过 8 条*/
	NO_MORE_THAN_PUSHED("12546","No more than 8 posts can be pushed"),
	/** 推送失败，内容尚未发布*/
	PUSH_ERROR_CONTENT_UNPUBLISH("12547","The push failed and the content has not yet been published"),
	/** 推送失败，封面不能为空*/
	PUSH_ERROR_IMAGE_NOT_NULL("12548","The push failed and the image not null"),
	/** 令牌已过期，请重新授权*/
	TOKEN_EXPIRY_DATE("12549","The token has expired, please reauthorize"),
	/** 推送失败，正文不能为空*/
	PUSH_ERROR_MAINBODY_NOTNULL("12550","Push failed, the text cannot be empty"),
	/** 未配置百度语音应用信息*/
	BAIDU_VOICE_NOT_CONFIGURED("12551","Baidu voice application information not configured!"),
	/** 未配置微信公众号*/
	NOT_CONFIG_WECHAT("12552","Not config whchat"),
	/** 同步SSO失败*/
	SYNC_SSO_ERROR("12553","Synchronization SSO failed"),
	/** 来源于其他应用的用户不可操作*/
	USER_CANNOT_OPERATE("12554","Users from other applications cannot operate"),
	/** 图文素材新增上传图片。*/
	UPLOAD_NEWS_IMAGE_FORMAT_ERROR("12555", "image format error !"),
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
	 * @param code
	 *            异常的代码。
	 * 
	 * @param defaultMessage
	 *            异常的默认提示信息。
	 */
	RPCErrorCodeEnum(String code, String defaultMessage) {
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
