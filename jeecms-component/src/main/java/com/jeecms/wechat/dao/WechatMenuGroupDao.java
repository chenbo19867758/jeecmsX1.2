package com.jeecms.wechat.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.domain.WechatMenuGroup;


/**
 * 
 * @Description: 微信菜单组dao层
 * @author: chenming
 * @date:   2018年8月11日 下午5:13:43     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface WechatMenuGroupDao extends IBaseDao<WechatMenuGroup, Integer> {
	
	/**
	 * 修改该appId的所有的默认菜单组为未开启
	 * @Title: updateStatus  
	 * @param appId
	 * @return      
	 * @return: Integer
	 */
	@Query("update WechatMenuGroup bean set bean.status=2 where bean.appId=?1 and bean.menuGroupType=1")
	@Modifying
	Integer updateStatus(String appId);
}
