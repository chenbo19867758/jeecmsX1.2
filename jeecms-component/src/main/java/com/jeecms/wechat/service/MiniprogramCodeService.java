package com.jeecms.wechat.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.domain.MiniprogramCode;

/**
 * 小程序模版草稿箱service
 * 
 * @Description:
 * @author wulongwei
 * @date 2018年11月1日 上午10:12:44
 */
public interface MiniprogramCodeService extends IBaseService<MiniprogramCode, Integer> {

	/**
	 * 获取最新的MiniprogramCode
	 * @Title: getNew  
	 * @throws GlobalException  全局异常    
	 * @return: MiniprogramCode
	 */
	MiniprogramCode getNew() throws GlobalException;

	/**
	 * 同步草稿箱模板
	 * @Title: synchronousDraft  
	 * @param openAppId	第三方开放平台的appId
	 * @throws GlobalException  全局异常         
	 * @return: void
	 */
	void synchronousDraft(String openAppId) throws GlobalException;

	/**
	 * 同步模板库
	 * @Title: synchronousTemplate  
	 * @param openAppId	第三方开放平台的appId
	 * @throws GlobalException  全局异常      
	 * @return: void
	 */
	void synchronousTemplate(String openAppId) throws GlobalException;

	/**
	 * 添加到模板库
	 * @Title: addTemplate  
	 * @param openAppId	第三方开放平台的appId
	 * @param id	模板/草稿箱id值
	 * @throws GlobalException  全局异常    
	 * @return: void
	 */
	void addTemplate(String openAppId, Integer id) throws GlobalException;

	/**
	 * 删除模板库的模版
	 * @Title: deleteTemplate  
	 * @param openAppId	第三方开放平台的appId
	 * @param id	模板/草稿箱id值
	 * @throws GlobalException   全局异常    
	 * @return: void
	 */
	void deleteTemplate(String openAppId, Integer id) throws GlobalException;

	/**
	 * 设为最新版本
	 * @Title: updateTemplate  
	 * @param id	模板/草稿箱id值	
	 * @throws GlobalException   全局异常   
	 * @return: void
	 */
	void updateTemplate(Integer id) throws GlobalException;
	
	/**
	 * 获取开放平台的appid
	 * @Title: getOpenAppId  
	 * @return: String
	 */
	String getOpenAppId();
	
	/**
	 * 清空模板、草稿箱
	 * @Title: clear  
	 * @throws GlobalException    全局异常
	 * @return: void
	 */
	void clear() throws GlobalException;
}
