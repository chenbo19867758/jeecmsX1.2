package com.jeecms.wechat.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.domain.WechatMaterial;

/**
 * 
 * @Description: 微信素材管理
 * @author: chenming
 * @date:   2018年8月3日 下午5:53:11     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface WechatMaterialDao extends IBaseDao<WechatMaterial, Integer>{
	
	/**
	 * 以appId作为查询条件，查询出所有的微信素材
	 * @Title: findByAppId  
	 * @param appid
	 * @return      
	 * @return: List<WechatMaterial>
	 */
	List<WechatMaterial> findByAppId(String appid);
	
	/**
	 * 以mediaId作为单一查询条件
	 * @Title: findByMediaId  
	 * @param mediaId
	 * @return      
	 * @return: WechatMaterial
	 */
	List<WechatMaterial> findByMediaIdAndHasDeleted(String mediaId,Boolean hasDeleted);
	
	/**
	 * 以appId、mediaType、hasDeleted作为查询条件，查询出符合条件的微信素材
	 * @Title: findByAppIdAndMediaTypeAndHasDeleted  
	 * @param appid
	 * @param mediaType
	 * @param hasDeleted
	 * @return      
	 * @return: List<WechatMaterial>
	 */
	List<WechatMaterial> findByAppIdAndMediaTypeAndHasDeleted(String appid,String mediaType,boolean hasDeleted);
    
	/**
	 * 根据ID获取微信素材
	 * @Title: findByIdAndHasDeleted  
	 * @param id 主键ID
	 * @param hasDeleted 是否删除
	 * @return      
	 * @return: WechatMaterial
	 */
	WechatMaterial findByIdAndHasDeleted(Integer id,boolean hasDeleted);
	
}
