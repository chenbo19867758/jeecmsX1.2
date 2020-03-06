package com.jeecms.common.wechat;

/**
 * 微信相关常量.
 *
 * @author vioao
 */
public class Const {
	public interface DoMain {
		String API_URI_HTTP = "http://api.weixin.qq.com";
		String API_URI = "https://api.weixin.qq.com";
		String FILE_URI = "http://file.api.weixin.qq.com";
		String MP_URI = "https://mp.weixin.qq.com";
		String MCH_URI = "https://api.mch.weixin.qq.com";
		String OPEN_URI = "https://open.weixin.qq.com";
		String RISK_URL = "https://fraud.mch.weixin.qq.com";
	}

	public interface Charset {
		String UTF_8 = "UTF-8";
		String ISO_8859_1 = "iso-8859-1";
	}

	public interface Lang {
		String ZH_CN = "zh_CN";
	}

	/**
	 * 定义@ValidWeChatToken注解支持的类型
	 */
	public interface ValidTokenType {
		/** 验证公众平台第三方应用的componentAccessToken */
		String COMONTENT_ACCESS_TOKEN = "componentAccessToken";
		/** 验证公众平台第三方应用代授权公众号或小程序调用凭证authorizerAccessToken */
		String ACCESS_TOKEN = "authorizerAccessToken";
	}

	/**
	 * 定义token失效时间，单位毫秒，一个半小时
	 */
	public interface Expire {
		Long VALUE = 5400000L;
	}

	public interface WeCahtType {
		/** 公众号 */
		Short PUBLIC_ACCOUNT = new Short((short) 1);
		/** 小程序 */
		final Short SMALL_PROGRAM = new Short((short) 2);
		/** 平台 */
		Short PLATFORM_TYPE = new Short((short) 1);
		/** 商家 */
		Short STORE_TYPE = new Short((short) 2);
	}

	public interface Mssage {
		/** 请求消息：小视频消息*/
		public static final String REQ_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";
		/**
		 * 请求消息类型：文本
		 */
		public static final String REQ_MESSAGE_TYPE_TEXT = "text";
		/** 请求消息类型：图文 */
		public static final String REQ_MESSAGE_TYPE_NEWS = "news";
		/** 请求消息类型：图片 */
		public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
		/** 请求消息类型:音乐 */
		public static final String REQ_MESSAGE_TYPE_MUSIC = "music";
		/** 请求消息类型：链接 */
		public static final String REQ_MESSAGE_TYPE_LINK = "link";
		/** 请求消息类型：地理位置 */
		public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
		/** 请求消息类型：音频 */
		public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
		/** 请求消息类型：视频 */
		public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
		/** 请求消息类型：推送 */
		public static final String REQ_MESSAGE_TYPE_EVENT = "event";
		/** 事件类型：subscribe(订阅) */
		public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
		/** 事件类型：unsubscribe(取消订阅) */
		public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
		/** 事件类型：CLICK(自定义菜单点击事件) */
		public static final String EVENT_TYPE_CLICK = "CLICK";
		/** 事件类型: view(自定义菜单view事件) */
		public static final String EVENT_TYPE_VIEW = "VIEW";
		/** 事件类型:scan(用户已关注时的事件推送) */
		public static final String EVENT_TYPE_SCAN = "SCAN";
		/** 事件类型:scancode_push(扫码推送事件) */
		public static final String EVENT_TYPE_MESSAGE_SCANCODE = "scancode_push";
		/** 事件类型:LOCATION(上报地理位置事件) */
		public static final String EVENT_TYPE_LOCATION = "location";

		/**
		 * 下方的审核通过事件类型微信官方文档有问题，以我写的为准
		 */
		/** 审核通过事件类型 */
		public static final String EVENT_TYPE_AUDIT_STATE = "weapp_audit_success";
		/** 审核失败事件类型 */
		public static final String EVENT_TYPE_AUDIT_FAIL = "weapp_audit_fail";

		/** 此字段是通信标识，通信失败 */
		public static final String FAIL = "FAIL";
		/** 此字段是通信标识，通信成功 */
		public static final String SUCCESS = "SUCCESS";
		/** 签名类型 */
		public static final String HMACSHA256 = "HMAC-SHA256";
		public static final String MD5 = "MD5";
		public static final String FIELD_SIGN = "sign";
		public static final String FIELD_SIGN_TYPE = "sign_type";

		/**此字段是全网发布时，微信发送的测试内容*/
	    public static final String TESTCOMPONENT_MSG_TYPE_TEXT = "TESTCOMPONENT_MSG_TYPE_TEXT";
	    public static final String QUERY_AUTH_CODE = "QUERY_AUTH_CODE";
	    
		/**
		 * 判断微信小程序的返回状态码作为是否成功的依据
		 */
		public static final String SUCCESS_CODE = "200";
	}
}
