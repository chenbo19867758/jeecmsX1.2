package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.menu.CreateMenuRequest;
import com.jeecms.common.wechat.bean.response.mp.menu.CreateMenuResponse;
import com.jeecms.common.wechat.bean.response.mp.menu.DeleteMenuResponse;
import com.jeecms.common.wechat.bean.response.mp.menu.GetMenuResponse;

/**
 * 
 * @Description: 菜单配置接口
 * @author: chenming
 * @date:   2018年8月8日 下午2:31:11     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface MenuApiService {
	
	/**
	 * 创建菜单
	 * @Title: createMenu  
	 * @param cMenuRequest
	 * @param vToken
	 * @return
	 * @throws GlobalException      
	 * @return: CreateMenuResponse
	 */
	CreateMenuResponse createMenu(CreateMenuRequest cMenuRequest,ValidateToken vToken)throws GlobalException;
	
	/**
	 * 查询菜单
	 * @Title: getMenu  
	 * @param vToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetMenuResponse
	 */
	GetMenuResponse getMenu(ValidateToken vToken) throws GlobalException;
	
	/**
	 * 删除菜单
	 * @Title: deleteMenu  
	 * @param vToken
	 * @return
	 * @throws GlobalException      
	 * @return: DeleteMenuResponse
	 */
	DeleteMenuResponse deleteMenu(ValidateToken vToken)throws GlobalException;
}
