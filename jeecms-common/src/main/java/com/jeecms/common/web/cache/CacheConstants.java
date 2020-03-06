package com.jeecms.common.web.cache;

/**
 * 缓存常量
 * 
 * @author: tom
 * @date: 2018年9月27日 上午11:02:14
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class CacheConstants {
	/**
	 * 验证码有效期
	 */
	public static final int CAPTCHA_EXPIRE = 60;
	public static final String MEMBER = "memberCache";
	public static final String SEARCH_WORD = "searchWordCache";
	public static final String CAPTCHA = "captchaCache";
	public static final String SMS = "smsCache";
	public static final String TOKEN = "tokenCache";
	public static final String BASE_CACHE = "BaseService-";
	public static final String USER_OWNER_SITE_CACHE = "userOwnerSiteCache";
	public static final String USER_OWNER_MENU_CACHE = "userOwnerMenuCache";
	public static final String USER_OWNER_SITE_DATA_CACHE = "userOwnerSiteDataCache";
	public static final String USER_OWNER_CHANNEL_CACHE = "userOwnerChannelDataCache";
	public static final String USER_OWNER_CONTENT_CACHE = "userOwnerContentDataCache";
	
	public static final String ORG_OWNER_SITE_CACHE = "orgOwnerSiteCache";
	public static final String ORG_OWNER_MENU_CACHE = "orgOwnerMenuCache";
	public static final String ORG_OWNER_SITE_DATA_CACHE = "orgOwnerSiteDataCache";
	public static final String ORG_OWNER_CHANNEL_CACHE = "orgOwnerChannelDataCache";
	public static final String ORG_OWNER_CONTENT_CACHE = "orgOwnerContentDataCache";
	
	public static final String ROLE_OWNER_SITE_CACHE = "roleOwnerSiteCache";
	public static final String ROLE_OWNER_MENU_CACHE = "roleOwnerMenuCache";
	public static final String ROLE_OWNER_SITE_DATA_CACHE = "roleOwnerSiteDataCache";
	public static final String ROLE_OWNER_CHANNEL_CACHE = "roleOwnerChannelDataCache";
	public static final String ROLE_OWNER_CONTENT_CACHE = "roleOwnerContentDataCache";
	
	
	/**
	 * 一次会话访问缓存
	 */
	public static final String ACCESSRECORD_CACHE = "accessRecordCache";
	/**
	 * 最新会话访问缓存
	 */
	public static final String UV_ACCESSRECORD_CACHE = "uvAccessRecordCache";
	public static final String IP_ACCESSRECORD_CACHE = "ipAccessRecordCache";
	public static final String LAST_ACCESSRECORD_CACHE = "lastAccessRecordCache";
	public static final String SYSTEM_CACHE = "systemCache";
	public static final String IP_LOCATION_CACHE = "ipLocationCache";
	public static final String SITE_FLOW_CACHE = "siteFlowCache";

	public static final String AREA = "areaCache";
	
	public static final String CONTENT_NUM = "contentNumCache";
	
	/**验证码使用场景---微博PC登录**/
	public static final String LOGIN_SCENE = "loginScene";
	/**验证码使用场景---微博手机登录**/
	public static final String LOGIN_SCENE_MOBILE = "loginSceneMobile";
	/**微博授权**/
	public static final String AUTH_SCENE = "authScene";
	
	/**验证码使用场景---QQ手机登录**/
	public static final String QQ_LOGIN_SCENE_MOBILE = "qqLoginSceneMobile";
	
	public static final String TIME = "time";
	
	/**百度语音Token**/
	public static final String BAIDU_VOICE_TOKEN_CACHE = "baiduVoiceTokenCache";
	/**会员用户名缓存*/
	public static final String SESSION_USER_CACHE = "sessionUserCache";
	/**用户SSO缓存*/
	public static final String SSO_USER_CACHE = "ssoUserCache";
	/**日志log缓存*/
	public static final String SYS_LOG_CACHE = "sysLogCache";
}
