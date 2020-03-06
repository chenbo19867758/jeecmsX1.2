package com.jeecms.common.exception.error;

import com.jeecms.common.exception.ExceptionInfo;

/**
 * 设置类 错误码枚举 号段范围 13000~13500
 *
 * @author: tom
 * @date: 2018年11月6日 下午6:53:26
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public enum SettingErrorCodeEnum implements ExceptionInfo {

	/** 验证区域编号唯一 */
	AREACODE_EXIST("13001", "AreaCode is already exist!"),
	/** 模版ID不能为空 */
	TPLID_NOTNULL("13002", "TplId is already exist!"),
	/** 消息内容不能为空 */
	MESSAGECONTENT_NOTNULL("13003", "MesContent is already exist!"),
	/** 模版标题不能为空 */
	MESTITLE_NOTNULL("13004", "MesTitle is already exist!"),
	/** 模板消息的code已存在，不允许重复 */
	MSG_CODE_EXIST("13008", "MesCode is already exist!"),

	/** 合作者身份（Partner ID）不能为空 */
	APP_ID_NOTNULL("13114", "AppId is already exist!"),
	/** 支付宝账号不能为空 */
	PAY_PUBLIC_KEY_NOTNULL("13115", "PayPublicKey is already exist!"),
	/** 交易安全校验码不能为空 */
	PAY_SECRET_KEY_NOTNULL("13116", "PaySecretKey is already exist!"),
	/** 银联商户证书路径不能为空 */
	UNIONPAY_CERT_PATH_NOTNULL("13117", "UnionpayCertPath is already exist!"),
	/** 银联商户密码不能为空 */
	UNIONPAYPASSWORD_NOTNULL("13118", "UnionpayPassword is already exist!"),
	/** 商户号不能为空 */
	PAY_ACCOUNT_NOTNULL("13119", "PayAccount is already exist!"),
	/** 微信密钥不能为空 */
	PAY_SECRET_KEY("13120", "PaySecretKey is already exist!"),

	/** 未配置手机短信模板 */
	SMS_TPL_UNCONFIGURED("13121", "No SMS template message is configured!"),
	/** 未配置消息模板 */
	MESSAGE_TPL_UNCONFIGURED("13122", "Unconfigured message template!"),
	/** 未配置邮箱模板 */
	EMAIL_TPL_UNCONFIGURED("13123", "No Email template message is configured!"),
	/** 公众号/微信小程序 secret 不能为空 */
	APP_SECRET_NOTNULL("13124", "AppSecret is already exist!"),
	/** 广告版位标识已存在 **/
	ADPOSITION_ALREADY_EXIST("13125", "Advertising placards already exist!"),

	/** 输入的区间格式错误 */
	INTERVAL_FORMAT_ERROR("13126", "interval Format error"),
	/** 定时任务名称已存在 */
	JOB_NAME("13127", "job's name is already exist !"),


	/** 菜单标识已经存在 */
	ROUTING_ALREADY_EXIST("13128","routing already exist!"),
	/** 字典类型已经存在 */
	DICTTYPE_ALREADY_EXIST("13129","dictType already exist!"),
	/** 字典编码已经存在*/
	DICTCODE_ALREADY_EXIST("13130","dictCode alreday exist!"),
	/** api地址已经存在 */
	API_URL_ALREADY_EXIST("13131","apiurl already exist"),
	/**推送类型传递参数不能为空**/
	PARAMETER_NOTNULL("13132","The push type passing parameter cannot be empty"),
	/**回复内容不能为空**/
	CONTENT_NOTNULL("13133","Reply content cannot be empty"),
	/**AppId不能为空*/
	APP_ID_NOT_NULL("13134", "AppId not null!"),
	/**地区码不能为空*/
	BUCKET_AREA_NOT_NULL("13135", "BucketArea not null!"),
	/**访问域名不能为空*/
	ACCESS_DO_MAIN_NOR_NULL("13136", "AccessDoMain not null!"),
	/**阿里云endPoint不能为空*/
	END_POINT_NOT_NULL("13137", "EndPoint not null!"),
	/**自动退出周期不能为空*/
	AUTO_LOGOUT_MINUTE_NOT_NULL("13138", "autoLogoutMinute not null!"),
	/**百度token不能为空*/
	BAIDU_PUSH_TOKEN_NOT_NULL("13139", "baidu token not null!"),
	/**所需积分最小值不能大于最大值*/
	MIN_CANNOT_EXCEED_MAX_ERROT("13140", "min cannotExceed max error!"),
	/**设置的所需积分超过范围*/
	INTEGRAL_EXCEED_RANGE_ERROR("13141", "integral exceed range error"),
	/**模型名称已经存在*/
	MODEL_NAME_ALREADY_EXIST("13142", "modelName already exist!"),
	/**栏目名称已经存在*/
	CHANNEL_NAME_ALREADY_EXIST("13143", "channel name already exist!"),
	/**栏目路径已经存在*/
	CHANNEL_PATH_ALREADY_EXIST("13144", "channel path already exist!"),
	/**内容类型名称已经存在*/
	TYPE_NAME_ALREADY_EXIST("13145", "TypeName already exist!"),
	/**来源名称已经存在*/
	SOURCE_NAME_ALREADY_EXIST("13146", "sourceName already exist!"),
	/**评论不是站点数据*/
	COMMENT_IS_NOT_SITE_DATA("13147", "Comment is not site data!"),
	/**用户或ip已经被禁止*/
	USER_OR_IP_HAS_BEEN_BANNED("13148", "User or Ip has been banned!"),
	/**禁言数据不是站点数据*/
	FORBIDDEN_DATA_IS_NOT_SITE_DATA("13149","Forbidden data is not site data"),
	/**ip地址错误*/
	IP_ADDRESS_ERROR("13150", "ip address error"),
	/**IP地址不在区间内*/
	IP_ADDRESS_IS_NOT_IN_THE_RANGE("13151", "Ip address is not in the range"),
	/** 回复类型错误*/
	REPLY_TYPE_ERROR("13152","Reply type error"),
	/**IP地址在区间内*/
	IP_ADDRESS_IS_IN_THE_RANGE("13153", "Ip address is in the range"),
	/** 不是底层栏目*/
	NOT_THE_BOTTOM_CHANNEL("13154","Not the bottom channel"),
	/** 栏目不允许为空*/
	CHANNEL_IS_NOT_NULL("13155","Channel is not null"),
	/**本站模型不允许添加会员模型*/
	LOCAL_SITE_MODEL_IS_NOT_ALLOW_CREATE_MEMBER_MODEL("13156" , "local site model is not allow create member model"),
	/**会员模型已存在，无法重复添加*/
	MEMBER_MODEL_IS_EXIST("13157" , "member model is exist, not allow create"),
	/**会员模型不允许修改*/
	MEMBER_IS_ALLOW_UPDATE("13158" , "member model is not allow update"),
	/** 热词名称已存在 */
	HOT_WORD_ALREADY_EXIST("13159" , "hot word already exist"),
	/** 采集任务已存在 */
	COLLECT_TASK_NAME_ALREADY_EXIST("13160", "collect task name already exist"),
	/** 路由地址已经存在 */
	ROUTING_ADDRESS_ALREADY_EXIST("13161","routing address already exist!"),
	/** 后台未设置模型*/
	MODEL_NOT_SET("13162","Model not set!"),
	/** 传入的模型错误*/
	INCOMING_MODEL_ERROR("13163","Incoming model error"),
	/** 执行方式为cron表达式时，必须输入cron表达式 */
	JOB_CRON_ALREADY_EXIST("13164", "job's cron is already exist !"),
	/** 执行方式为执行周期时，必须输入间隔类型和间隔时间 */
	JOB_INTERVAL_ALREADY_EXIST("13165", "job's Interval is already exist !"),
	/** 资源空间已存在 */
	RESOURCE_SPACE_ALREADY_EXIST("13166", "resource space is already exist !"),
	/**水印文字颜色格式错误 */
	WATERMARK_TEXT_COLOR_FORMAT_ERROR("13167", "Watermark text color format error!"),
	/** 用户没有登录，无法禁止*/
	USER_IS_NOT_LOGIN_UNABLE_TO_PROHIBIT("13168","User is not login unable to prohibit!"),
	/** 用户没有登录，无法进行举报操作*/
	USER_IS_NOT_LOGIN_UNABLE_TO_REPLY("13169","User is not login unable to reply!"),
	/** 系统正在初始化中，请稍后再进行操作*/
	THE_SYSTEM_IS_INITIALIZING("13170","The system is initializing!")
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
	SettingErrorCodeEnum(String code, String defaultMessage) {
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
