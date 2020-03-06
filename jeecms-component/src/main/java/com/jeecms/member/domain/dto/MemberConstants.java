/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.domain.dto;

/**
 * 用户常量信息
 * @author: ztx
 * @date: 2018年9月17日 下午3:49:17
 */
public class MemberConstants {

	// 一级键名
	/** 第三方用户唯一标识 */
	public static final String THIRD_PARTY_ATTR_OPENID = "OPENID";
	/** 第三方类型 */
	public static final String THIRD_PARTY_ATTR_TYPE_CODE = "TYPECODE";
	/** 第三方用户名 */
	public static final String THIRD_PARTY_ATTR_USER_NAME = "THIRDUSERNAME";
	/** 回调前台页面URL */
	public static final String THIRD_PARTY_ATTR_BACK_URL = "backUrl";

	/** 交换机URL */
	public static final String REDIRECT_EXCHANGE_URL = "/exchange.htm";
	/** 第三方登录成功后重定向URL */
	public static final String THIRD_PARTY_LOGIN_REDIRECT_URL = "redirect:" + REDIRECT_EXCHANGE_URL
			+ "?thirdId=%s&typeCode=%s&thirdUsername=%s&headImgUrl"
			+ "=%s&isBinded=false&backUrl=%s&appId=%s&openId=%s";
	/** 第三方登录成功后已绑定会员重定向URL */
	public static final String ALREADY_EXIST_MEMBER_URL = "redirect:" + REDIRECT_EXCHANGE_URL + "?"
			+ "isBinded=true&authToken=%s&backUrl=%s";
	/** 第三方登录失败URL */
	public static final String THIRD_PARTY_LOGIN_FAILURE_URL = "/loginFailure.htm";
	/** 第三方登录失败重定向URL */
	public static final String THIRD_PARTY_LOGIN_FAILURE_REDIRECT_URL = "redirect:" + THIRD_PARTY_LOGIN_FAILURE_URL
			+ "?errorMsg=第三方登录失败,错误信息%s";
	
	// 二级键名
	/** qq */
	public static final String CODE_SECOND_LEVEL_IDENTITY_QQ = "qq_";
	/** 微信 */
	public static final String CODE_SECOND_LEVEL_IDENTITY_WEIXIN = "wechat_";
	/** 新浪 */
	public static final String CODE_SECOND_LEVEL_IDENTITY_SINA = "sina_";
	/** 会员注册 */
	public static final String CODE_SECOND_LEVEL_IDENTITY_REGISTER = "register_";
	/** 找回密码 */
	public static final String CODE_SECOND_LEVEL_IDENTITY_RETRIEVE_PASSWORD = "retrieve_";

	// 三级键名
	/** 第三方登录state */
	public static final String CODE_THREE_LEVEL_IDENTITY_STATE = "login_state";

	// 第三方平台标识
	/** 微信 */
	public static final String THIRD_TYPE_CODE_WEIXIN = "wechat";
	/** QQ */
	public static final String THIRD_TYPE_CODE_QQ = "qq";
	/** 新浪 */
	public static final String THIRD_TYPE_CODE_SINA = "sina";

	/** 权限标识--所有 */
	public static final String THIRD_SCOPE_ALL = "ALL";

	// 申请的第三方appId和密匙以及回调
	/** QQ */
	public static final String THIRD_IDENTITY_QQ_APPID = "101506052";
	public static final String THIRD_IDENTITY_QQ_APPKEY = "7cdaaf3b767cb927cc13aa5bd9127c1b";
	public static final String THIRD_IDENTITY_QQ_REDIRECT_URL = "http://dbc3f6cf.ngrok.io/thirdParty/qq/back";
	/** 微信 */
	public static final String THIRD_IDENTITY_WEIXIN_APPID = "wx8cd79f4b18b2994a";
	public static final String THIRD_IDENTITY_WEIXIN_SECRET = "16ba7bf1b8553a0cd85a78b94290c111";
	public static final String THIRD_IDENTITY_WEIXIN_REDIRECT_URL = "http://dbc3f6cf.ngrok.io/thirdParty/wechat/back";
	/** 新浪 */
	public static final String THIRD_IDENTITY_SINA_CLIENT_ID = "2516368551";
	public static final String THIRD_IDENTITY_SINA_CLIENT_SECRET = "380f3e7f75970ece92fdee72d782e5e2";
	public static final String THIRD_IDENTITY_SINA_REDIRECT_URL = "http://dbc3f6cf.ngrok.io/thirdParty/sina/back";

	/** 腾讯地图开发密钥 */
	public static final String THIRD_IDENTITY_TENCENT_MAP_KEY = "KZDBZ-2GBWS-GAYOV-63EX6-FZNCE-G3FJD";
	
	/**
	 * 第三方调用返回状态码
	 */
	public static final Integer THIRD_IDENTITY_TENCENT_MAP_OK = 0;
	
	// 交易类型
	/** 充值 */
	public static final String TRADE_TYPE_RECHARGE = "1";
	/** 退款 */
	public static final String TRADE_TYPE_REFUND = "2";
	/** 提现 */
	public static final String TRADE_TYPE_PRESENT = "3";
	/** 订单交易 */
	public static final String TRADE_TYPE_ORDER_TRANSACTION = "4";

	// 支付状态
	/** 待交易 */
	public static final Integer TRADE_STATUS_TOTRADE = 1;
	/** 未完成交易 */
	public static final Integer TRADE_STATUS_UNTRADE = 2;
	/** 交易完成 */
	public static final Integer TRADE_STATUS_OFFTRADE = 3;
	/** 交易失败 */
	public static final Integer TRADE_STATUS_FAILTRADE = 4;
	
	// 会员账户明细交易状态标识
	/** 成功 */
	public static final Integer ACCOUNT_DETAIL_STATUS_SUCCESS = 1;
	/** 失败 */
	public static final Integer ACCOUNT_DETAIL_STATUS_FAIL = 2;
	/** 处理中 */
	public static final Integer ACCOUNT_DETAIL_STATUS_PROCESSING = 3;

	/**
	 * 交易备注
	 */
	public static final String MEMBER_ACCOUNT_RECHARGE = "会员充值,充值金额:%s";
	public static final String MEMBER_ACCOUNT_PRESENT = "会员提现,提现金额:%s";
	public static final String STORE_BOUND_CONSUMER_SERVICE = "店铺保障服务开通,缴纳保证金:%s";

}
