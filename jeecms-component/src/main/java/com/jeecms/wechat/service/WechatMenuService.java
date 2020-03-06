package com.jeecms.wechat.service;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.domain.WechatMenu;
import com.jeecms.wechat.domain.WechatMenuGroup;
import com.jeecms.wechat.domain.dto.UpdateMenuStatusDto;

/**
 * 微信菜单service接口
 * @author: chenming
 * @date:   2019年5月31日 上午11:57:49
 */
public interface WechatMenuService extends IBaseService<WechatMenu, Integer> {
	
	/**
	 * 新增一组菜单
	 * @Title: saveMenu  
	 * @param wGroup	菜单组
	 * @throws GlobalException     异常 
	 * @return: WechatMenu
	 */
	WechatMenu saveMenu(WechatMenuGroup wGroup)throws GlobalException;
	
	/**
	 * 删除该menuGroup符合的所有的菜单集合
	 * @Title: deleteMenu  
	 * @param menuGroup	菜单组
	 * @throws GlobalException    异常  
	 * @return: Integer
	 */
	Integer deleteMenu(WechatMenuGroup menuGroup)throws GlobalException;
	
	/**
	 * 修改菜单组状态
	 * @Title: updateStatus  
	 * @param dto	修改菜单状态dto
	 * @throws GlobalException 异常     
	 * @return: WechatMenu
	 */
	WechatMenu updateStatus(UpdateMenuStatusDto dto)throws GlobalException;
	
	/**
	 * 修改该wGroup下的所有的菜单组
	 * @Title: updateMenu  
	 * @param wGroup	菜单组对象
	 * @throws GlobalException 异常     
	 * @return: WechatMenu
	 */
	WechatMenu updateMenu(WechatMenuGroup wGroup)throws GlobalException;
	
	/**
	 * 根据菜单组ID进行查询菜单集合
	 * @Title: findByMenuGroupId  
	 * @param menuGroupId	菜单组ID值
	 * @throws GlobalException   全局异常   
	 * @return: List
	 */
	List<WechatMenu> findByMenuGroupId(Integer menuGroupId) throws GlobalException;
	
}
