package com.jeecms.common.base.domain;

import java.util.List;

/**   
用户类数据domain接口
 * @author: tom
 * @date:   2018年4月3日 下午1:44:20     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface IBaseUser {
	/**
	 * 获取用户类ID
	 * @Title: getUserId  
	 * @return: Integer
	 */
	Integer getUserId();
	
	/**
	 * 获取用户名
	 * @Title: getUsername
	 * @return: String
	 */
	String getUsername();
	
	/**
	 * 用户拥有的站群权限ID
	 * @Title: getOwnerSiteIds
	 * @return List
	 */
	List<Integer> getOwnerSiteIds();
}
