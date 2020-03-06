package com.jeecms.message.dto;


/**
 * @Description:消息队列公共参数
 * @author: ztx
 * @date: 2018年10月23日 上午10:29:39
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class CommonMqConstants {

	/**
	 * @Description: 消息场景枚举. 注意: 在使用线程处理消息时需和 {@link MessageSceneOperationEnum}
	 *               中的字段名称保持一致
	 */
	public enum MessageSceneEnum {
		/** 验证码 */
		VALIDATE_CODE,
		/** 用户消息*/
		USER_MESSAGE
	}

	/**
	 * @Description:消息发送方类型
	 */
	public enum MessageFromTypeEnum {
		/** 会员 */
		MEMBER,
		/** 平台 */
		PLATFORM
	}

	/**
	 * 消息附加参数Key
	 */
	public static final String EXT_DATA_KEY_EMAIL = "email";
	public static final String EXT_DATA_KEY_SMS = "sms";
	public static final String EXT_DATA_KEY_SYSTEM = "system";
	public static final String EXT_DATA_KEY_MSGPUSH = "msgpush";

}
