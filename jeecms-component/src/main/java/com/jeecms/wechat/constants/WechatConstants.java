/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.wechat.constants;

/**   
 * 微信常量
 * @author: ljw
 * @date:   2019年5月28日 下午5:36:06     
 */
public class WechatConstants {

	/**群发信息成功,Log 使用**/
	public static final Integer SEND_MESSAGE_SUCCESS = 1;
	/**群发信息失败,Log 使用**/
	public static final Integer SEND_MESSAGE_FAILED = 2;
	
	/**粉丝发送**/
	public static final Integer SEND_TYPE_FANS = 1;
	/**公众号发送**/
	public static final Integer SEND_TYPE_WECHAT = 2;
	
	/**返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语**/
	public static final String LANGUAGE_TYPE_CN = "zh_CN";
	public static final String LANGUAGE_TYPE_TW = "zh_TW";
	public static final String LANGUAGE_TYPE_EN = "en";
	
	/**用户的性别，值为1时是男性，值为2时是女性，值为0时是未知**/
	public static final Integer FANS_SEX_UNKNOW = 0;
	public static final Integer FANS_SEX_MAN = 1;
	public static final Integer FANS_SEX_WOMAN = 2;
	
	/** 未发送 **/
	public static Integer SEND_STATUS_WAIT = 1;
	/** 发送成功 **/
	public static Integer SEND_STATUS_SUCCESS = 2;
	/** 发送失败 **/
	public static Integer SEND_STATUS_FAIL = 3;
	
	/**留言时间倒序**/
	public static final int ORDER_TYPE_CREATETIME_DES = 1;
	/**留言时间顺序**/
	public static final int ORDER_TYPE_CREATETIME_ASC = 2;
	
	/**自动同步类型 0关闭 1.30秒 2.60秒 3.120秒 4.180秒 5.300秒**/	
	public static final int AUTO_TYPE_CLOSE = 0;
	public static final int AUTO_TYPE_30 = 1;
	public static final int AUTO_TYPE_60 = 2;
	public static final int AUTO_TYPE_120 = 3;
	public static final int AUTO_TYPE_180 = 4;
	public static final int AUTO_TYPE_300 = 5;
	
	/**每隔30秒CORN表达式**/
	public static String AUTO_TIME_30 = "0/30 * * * * ?";
	/**每隔60秒CORN表达式**/
	public static String AUTO_TIME_60 = "0/59 * * * * ?";
	/**每隔120秒CORN表达式**/
	public static String AUTO_TIME_120 = "* 0/2 * * * ?";
	/**每隔180秒CORN表达式**/
	public static String AUTO_TIME_180 = "* 0/3 * * * ?";
	/**每隔300秒CORN表达式**/
	public static String AUTO_TIME_300 = "* 0/5 * * * ?";
	
	/**发送类型1立即发送**/
	public static final Integer SEND_TYPE_NOW = 1;
	/**发送类型2定时发送**/
	public static final Integer SEND_TYPE_TIME = 2;
	
	/**语言**/
	public static final Integer LANGUAGE = 1;
	/**性别**/
	public static final Integer SEX = 2;
	/**省份**/
	public static final Integer PROVINCE = 3;
	/**城市**/
	public static final Integer CITY = 4;
	
}
