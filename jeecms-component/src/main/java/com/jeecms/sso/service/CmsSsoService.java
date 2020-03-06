/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.sso.service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.sso.dto.SsoLoginDto;
import com.jeecms.sso.dto.SyncRequestDeleteDto;
import com.jeecms.sso.dto.SyncRequestRegisterDto;
import com.jeecms.sso.dto.request.SyncRequestUser;
import com.jeecms.sso.dto.response.SyncResponseUserVo;

/**   
 * 单点登录Service
 * @author: ljw
 * @date:   2019年10月26日 上午10:15:34     
 */
public interface CmsSsoService {
	
	/**
	 * 保存SSO设置
	 * 
	 * @Title: saveSsoConfig
	 * @param dto 传输
	 * @return ResponseInfo 对象
	 * @throws GlobalException 异常
	 */
	ResponseInfo saveSsoConfig(SsoLoginDto dto) throws GlobalException;

	/**
	 * 同步用户到服务端
	* @Title: sync 
	* @throws GlobalException 异常
	 */
	ResponseInfo sync(String appId, String appSecret) throws GlobalException;
	
	/**
	 * 客户端新增用户，忽略权限，密码规则
	* @Title: saveUserNoAuth 
	* @throws GlobalException 异常
	 */
	ResponseInfo saveUserNoAuth(SyncRequestRegisterDto dto) throws GlobalException;
	
	/**
	 * 用户校验
	* @Title: loginVaild 
	* @param authToken 令牌
	* @return SyncResponseUserVo 用户信息对象,需要做为空判断
	* @throws GlobalException 异常
	 */
	SyncResponseUserVo userVaild(String authToken) throws GlobalException;
	
	/**
	 * 用户退出
	* @Title: loginVaild 
	* @param authToken 令牌
	* @throws GlobalException 异常
	 */
	void logout(String authToken) throws GlobalException;
	
	/**
	 * 客户端修改用户
	* @Title: update 
	* @throws GlobalException 异常
	 */
	ResponseInfo update(SyncRequestUser dto) throws GlobalException;
	
	/**
	 * 客户端提供用户删除
	* @Title: loginVaild 
	* @throws GlobalException 异常
	 */
	ResponseInfo delete(SyncRequestDeleteDto dto) throws GlobalException;
}
