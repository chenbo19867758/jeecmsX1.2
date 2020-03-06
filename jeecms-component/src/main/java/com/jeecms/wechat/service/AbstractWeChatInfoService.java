package com.jeecms.wechat.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.open.ComponentVerifyTicketRequest;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.AbstractWeChatToken;

/**
 * @Description:AbstractWeChatInfo
 * @author: qqwang
 * @date: 2018年4月16日 上午11:05:40
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */
public interface AbstractWeChatInfoService extends IBaseService<AbstractWeChatInfo, Integer> {
	
	/**
	 * 授权信息
	 * @Title: requestAuthorizeInfo  
	 * @param componentAppid 开放平台第三方应用appid
	 * @param authorizerAppId 授权应用appid
	 * @param validToken 验证token对象
	 * @param extra 额外参数
	 * @param authorizerToken 授权token
	 * @throws GlobalException 全局异常     
	 * @return: AbstractWeChatInfo
	 */
	public AbstractWeChatInfo requestAuthorizeInfo(String componentAppid,String authorizerAppId,
			ValidateToken validToken,Integer siteId, String authorizerToken)throws GlobalException;
	
	/**
	 * 授权token
	 * @Title: requestAuthorizeAccessToken  
	 * @param extra 额外参数
	 * @param authCode 授权回调授权码
	 * @throws GlobalException 全局异常     
	 * @return: AbstractWeChatToken
	 */
	AbstractWeChatToken requestAuthorizeAccessToken(String extra,String authCode)throws GlobalException;
	
	/**
	 * 获取站点下的小程序/公众号
	 * @Title: findWeChatInfo   
	 * @param: @param type 1-公众号  2-小程序
	 * @param: @param siteId  站点ID，为空，则默认查询平台
	 * @return: AbstractWeChatInfo
	 */
	List<AbstractWeChatInfo> findWeChatInfo(Short type,Integer siteId);
	
	/**
	 * 通过AppId查询
	 * @Title: findAppId  
	 * @param appId
	 * @return: AbstractWeChatInfo
	 */
	AbstractWeChatInfo findAppId(String appId);
	
	/**
	 * 查询站点下默认快捷登录的公众号、小程序
	 * @Title: findDefault  
	 * @param type	状态：1-公众号，2-小程序
	 * @param siteId	站点id
	 * @return: AbstractWeChatInfo
	 */
	AbstractWeChatInfo findDefault(Short type, Integer siteId);
	
	/**
	 * 获取当前小程序的最新服务类目
	 * @Title: getCategory  
	 * @param request
	 * @throws GlobalException 全局异常     
	 * @return: ResponseInfo
	 */
	public ResponseInfo getCategory(Integer id)throws GlobalException;
	
	/**
	 * 获取服务器域名
	 * @Title: getModifyDomain  
	 * @param request
	 * @throws GlobalException 全局异常      
	 * @return: ResponseInfo
	 */
	public ResponseInfo getModifyDomain(Integer id)throws GlobalException;
	
	/**
	 * 设置服务器域名
	 * @Title: setModifyDomain  
	 * @param request	request请求
	 * @param id		小程序ID
	 * @throws GlobalException      
	 * @return: ResponseInfo
	 */
	public ResponseInfo setModifyDomain(HttpServletRequest request,Integer id)throws GlobalException;
	
	/**
	 * 取消授权
	 * @Title: deleteAbstract  
	 * @param componentVerifyTicket
	 * @throws GlobalException      
	 * @return: void
	 */
	void deleteAbstract(ComponentVerifyTicketRequest componentVerifyTicket) throws GlobalException;
	
	/**
	 * 检查管理员是否有权限操作该公众号数据，校验逻辑如下（菜单权限在此处不做处理，全局拦截已处理）
	 * 1、在公众号设置管理员情况下，管理员是否为公众号的管理员且是否有菜权限；
	 * 2、在未设置管理员情况下,是否有菜单权限
	 * @param userId  操作用户id
	 * @param wechatId 操作授权公众号、小程序对象id
	 * @throws GlobalException
	 */
	void checkWeChatAuth(Integer userId,Integer wechatId,String appId) throws GlobalException;
	
}
