package com.jeecms.wechat.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.domain.WechatMenuGroup;

/**
 * 
 * @Description: 微信菜单组service
 * @author: chenming
 * @date:   2018年8月11日 下午5:15:59     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface WechatMenuGroupService extends IBaseService<WechatMenuGroup, Integer>{
	/**
	 * 删除一个菜单组
	 * @Title: deleteMenuGroup  
	 * @param id
	 * @return
	 * @throws GlobalException      
	 * @return: Boolean
	 */
	Boolean deleteMenuGroup(Integer id)throws GlobalException;
	
	/**
	 * 修改该appId的所有的默认菜单组为未开启
	 * @Title: updateStatic  
	 * @param appId
	 * @throws GlobalException      
	 * @return: void
	 */
	void updateStatic(String appId)throws GlobalException;
	
	/**
	 * 获取一个菜单组
	 * @Title: getMenuGroup  
	 * @param id
	 * @return
	 * @throws GlobalException      
	 * @return: WechatMenuGroup
	 */
	WechatMenuGroup getMenuGroup(Integer id)throws GlobalException;
	
	/**
	 * 修改一个菜单组状态
	 * @Title: updateStatus  
	 * @param id
	 * @return
	 * @throws GlobalException      
	 * @return: WechatMenuGroup
	 */
	WechatMenuGroup updateStatus(Integer id)throws GlobalException;
}
