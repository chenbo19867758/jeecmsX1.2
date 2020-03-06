package com.jeecms.wechat.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.domain.WechatMenu;


/**
 * 
 * @Description: 微信菜单dao层
 * @author: chenming
 * @date:   2018年8月11日 下午5:13:10     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface WechatMenuDao extends IBaseDao<WechatMenu, Integer> {
	
	/**
	 * 删除所有的menuGroupId为符合条件的菜单对象
	 * @Title: deleteMenu  
	 * @param menuGroupId
	 * @return      
	 * @return: Integer
	 */
	@Query("delete from WechatMenu bean where bean.menuGroupId=?1")
	@Modifying
	Integer deleteMenu(Integer menuGroupId);
	
	
	List<WechatMenu> findByMenuGroupId(Integer menuGroupId);
}
