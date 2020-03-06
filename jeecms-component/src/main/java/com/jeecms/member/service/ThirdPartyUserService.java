/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.service;

import com.jeecms.common.base.domain.ThirdPartyResultDTO;
import com.jeecms.weibo.domain.vo.WeiboTokenVO;

/**
 *	 第三方用户登录
 * @author: ljw
 * @date: 2019年7月19日 下午4:08:25

 */
public interface ThirdPartyUserService {
	
	/**
	 * 获取微信用户openId(PC)
	* @Title: loginForWeChatPc 
	* @param code 用户的CODE
	* @param backUrl 回调函数
	* @param siteId 站点ID
	* @throws Exception 异常
	 */
	ThirdPartyResultDTO loginForWeChatPc(String code,  String backUrl, Integer siteId) throws Exception;
	
	/**
	 * 获取QQ用户openId
	* @Title: loginForQQ 
	* @param code 用户的CODE
	* @param  backUrl 回调函数
	* @param siteId 站点ID
	* @throws Exception 异常
	 */
	ThirdPartyResultDTO loginForQQ(String code, String backUrl, Integer siteId) throws Exception;

	/**
	 * 获取新浪用户基本信息
	* @Title: loginForSina 
	* @param vo token对象
	* @param siteId 站点ID
	* @throws Exception 异常
	 */
	ThirdPartyResultDTO loginForSina(WeiboTokenVO vo, Integer siteId) throws Exception;
}
