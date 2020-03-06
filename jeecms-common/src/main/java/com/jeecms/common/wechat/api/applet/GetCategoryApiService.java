package com.jeecms.common.wechat.api.applet;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.response.applet.GetCategoryResponse;

/**
 * 
 * @Description: 获取小程序账号的可选类目
 * @author: chenming
 * @date:   2018年11月12日 下午3:57:41     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface GetCategoryApiService{
	/**
	 * 获取授权小程序帐号的可选类目（授权后显示的，不能加入token注解）
	 * @Title: getCategory  
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetCategoryResponse
	 */
	GetCategoryResponse getCategory(ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 获取授权小程序的最新可选类目
	 * @Title: getNewCategory  
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetCategoryResponse
	 */
	GetCategoryResponse getNewCategory(ValidateToken validateToken) throws GlobalException;
	
}
