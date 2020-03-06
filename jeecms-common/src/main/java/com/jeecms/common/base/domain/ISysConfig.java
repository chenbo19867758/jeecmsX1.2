package com.jeecms.common.base.domain;

import java.util.Map;

/**
 * 系统配置接口
 * 
 * @author: tom
 * @date: 2019年1月11日 下午6:00:32
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface ISysConfig {
	
	/**
	 * 获取站点域名
	 * @Title: getSiteDomain  
	 * @return      
	 * @return: String
	 */
	String getSiteDomain();

	/**
	 * 获取站点名称
	 * @Title: getSiteName  
	 * @return      
	 * @return: String
	 */
	String getSiteName();

	/**
	 * 获取认证信息
	 * @Title: getAuthCode  
	 * @return      
	 * @return: String
	 */
	String getAuthCode();

	/**
	 * 获取
	 * @Title: getSysState  
	 * @return      
	 * @return: Integer
	 */
	Integer getSysState();

	/**
	 * 是否禁止
	 * @Title: getInBlack  
	 * @return      
	 * @return: Boolean
	 */
	Boolean getInBlack();

	/**
	 * 是否限制
	 * @Title: getIsLimit  
	 * @return      
	 * @return: Boolean
	 */
	Boolean getIsLimit();
	
	Map<String,String> getConfigMap();
}
