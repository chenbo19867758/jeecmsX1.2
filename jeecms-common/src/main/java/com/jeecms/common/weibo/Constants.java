/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo;

/**   
 * 微博常量
 * @author: ljw
 * @date:   2019年6月15日 上午10:02:25     
 */
public class Constants {

	/**微博基础请求**/
	public static final String BASE_URL = "https://api.weibo.com/2/";
	/**微博用户token请求**/
	public static final String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
	/**微博授权请求**/
	public static final String AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize";
	/**取消微博授权**/
	public static final String REVOKE_AUTHORIZE_URL = "https://api.weibo.com/oauth2/revokeoauth2";
	
	/** QQ获取Authorization Code **/
	public static final String QQ_CODE_URL = "https://graph.qq.com/oauth2.0/authorize";
	/** WECHAT获取Authorization Code **/
	public static final String WECHAT_PC_CODE_URL = "https://open.weixin.qq.com/connect/qrconnect";
	
}
