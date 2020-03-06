package com.jeecms.component.aspect;

import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.open.OpenPlatformApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.open.AuthorizerAccessTokenRequest;
import com.jeecms.common.wechat.bean.request.open.ComponentAccessTokenRequest;
import com.jeecms.common.wechat.bean.response.open.AuthorizerAccessTokenResponse;
import com.jeecms.common.wechat.bean.response.open.ComponentAccessTokenResponse;
import com.jeecms.wechat.dao.AbstractWeChatInfoDao;
import com.jeecms.wechat.dao.AbstractWeChatOpenDao;
import com.jeecms.wechat.dao.AbstractWeChatTokenDao;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.AbstractWeChatOpen;
import com.jeecms.wechat.domain.AbstractWeChatToken;

/**
 * 
 * @Description:处理添加@ValidWeChatToken注解的方法拦截业务，会判断是否存在或者过期，然后自动调用相关接口刷新
 * 注解类型为componentAccessToken(开放平台第三方应用使用的componentAccessToken)，authorizerAccessToken(开放平台第三方应用代公众号或小程序调用凭证authorizerAccessToken)
 * @author: wangqq
 * @date:   2018年7月26日 下午1:42:35     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Component
@Aspect
public class ValidWeChatTokenAspect {
	Logger logger = LoggerFactory.getLogger(ValidWeChatTokenAspect.class);

	@Autowired
	private AbstractWeChatInfoDao weChatDao;
	@Autowired
	private AbstractWeChatOpenDao weChatOpenDao;
	@Autowired
	private AbstractWeChatTokenDao weChatTokenDao;
	@Autowired
	private OpenPlatformApiService openApiService;
	
	
	@Before(value = "@annotation(validWeChatToken)")
	public Object validToken(JoinPoint joinPoint,ValidWeChatToken validWeChatToken)
			throws Throwable {
		//获取注解函数上的所有参数
		Object[] params = joinPoint.getArgs();
		String annonationParam = validWeChatToken.value();
		ValidateToken validaToken = null ;
		AbstractWeChatToken token = null;
		if(params != null && params.length > 0){
			for (Object object : params) {
				if(ValidateToken.class.isAssignableFrom(object.getClass())){
					validaToken = (ValidateToken) object;
					break;
				}
			}
			if(validaToken != null ){
				//根据@ValidWeChatToken注解所传递验证类型，进行检验authorizerAccessToken 还是comonentAccessToken是否存在或过期问题
				if( annonationParam.equals(Const.ValidTokenType.ACCESS_TOKEN)){
					//验证authorizerAccessToken
					token =  validateAuthorizerAccessToken(validaToken);
					validaToken.setAccessToken(token.getAuthorizerAccessToken());
				} else{
					//验证componentAccessToken
					token = validateComponentAccessToken();
					validaToken.setAccessToken(token.getComponentAccessToken());
				}
				return params;
			}else{
				//抛出缺少验证参数
				throw new GlobalException(new WeChatExceptionInfo(
						SystemExceptionEnum.LACK_WECHAT_VALIDTOKEN_PARAMETERS.getCode(),
						SystemExceptionEnum.LACK_WECHAT_VALIDTOKEN_PARAMETERS.getDefaultMessage()));
			}
		}else{
			//抛出缺少验证参数
			throw new GlobalException(new WeChatExceptionInfo(
					SystemExceptionEnum.LACK_WECHAT_VALIDTOKEN_PARAMETERS.getCode(),
					SystemExceptionEnum.LACK_WECHAT_VALIDTOKEN_PARAMETERS.getDefaultMessage()));
		}
		
	}
	
	 /**
     * 检测公众号的authorizerAccessToken 是否失效或不存在,在失效或不存在的情况下，系统会自动刷新
     * @Title: validateAccessToken   
     * @param:  ownerType
     * @param:  validateAppType
     * @param:  storeId
     * @param: @throws GlobalException      
     * @return: void
     */
    protected AbstractWeChatToken  validateAuthorizerAccessToken(ValidateToken  validateToken) throws GlobalException {
    	//获取公众号或小程序信息
    	AbstractWeChatInfo weCahtInfo = weChatDao.findByAppIdAndHasDeleted(validateToken.getAppId(),false);
		if ( weCahtInfo != null && weCahtInfo.getAppId() != null ){
				// 获取开发平台应用配置信息
		    	AbstractWeChatOpen open = weChatOpenDao.findByHasDeleted(false);
				if(open != null  && open.getAppId() != null) {
					// 获取当前appId对应的token
					AbstractWeChatToken token = weChatTokenDao.findByAppId(weCahtInfo.getAppId());
					if (token != null && token.getAuthorizerRefreshToken() != null ) {
						// 当前毫秒数
						Long currentMillisecond = System.currentTimeMillis();
						// token 生成时的毫秒数
						Long createMillisecond = token.getAcceccTokenCreateTime() == null ? 0L :token.getAcceccTokenCreateTime().getTime();
						// 判断authorizerAccessToken生成时间与当前时间相差超过5400000毫秒（一个半小时）,系统再次通过authorizer_refresh_token
						// 进行刷新并修改
						if (currentMillisecond - createMillisecond > Const.Expire.VALUE  || token.getAuthorizerAccessToken() == null) {
							AuthorizerAccessTokenRequest componentATR = new AuthorizerAccessTokenRequest(open.getAppId(), weCahtInfo.getAppId(), token.getAuthorizerRefreshToken());
							AuthorizerAccessTokenResponse  authTokenRsp=  referAuthorizerAccessToken(componentATR, token,validateToken);
							token.setAuthorizerAccessToken(authTokenRsp.getAuthorizerAccessToken());
						}
						return token;
					} else {
						//微信公众号或小程序未授权
						throw new GlobalException(new WeChatExceptionInfo(
								SystemExceptionEnum.WEIXIN_APP_UNAUTHORIZED_ERROR.getCode(),
								SystemExceptionEnum.WEIXIN_APP_UNAUTHORIZED_ERROR.getDefaultMessage()));
					}
				}else{
					// 如数据库未找到相应数据，直接提示配置开放平台未设置或者请等待10分钟等待微信开放平台推送相关的component_verify_ticket
					throw new GlobalException(new WeChatExceptionInfo(
							SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR.getCode(),
							SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR
									.getDefaultMessage()));
				}
		}else{
			//微信公众号或小程序未授权
			throw new GlobalException(new WeChatExceptionInfo(
					SystemExceptionEnum.WEIXIN_APP_UNAUTHORIZED_ERROR.getCode(),
					SystemExceptionEnum.WEIXIN_APP_UNAUTHORIZED_ERROR.getDefaultMessage()));
		}
	}
    
    /**
     * 重新刷新authorizer_access_token
     * @throws GlobalException 
     */
    protected AuthorizerAccessTokenResponse referAuthorizerAccessToken(AuthorizerAccessTokenRequest accessTokenATR,AbstractWeChatToken token,ValidateToken  validateToken) throws GlobalException{
    	//使用authorizer_refresh_token刷新authorizerAccessToken
    	AuthorizerAccessTokenResponse authAccessTOkenRsp =  openApiService.getAuthorizerTokenApi(accessTokenATR,validateToken);
    	if(authAccessTOkenRsp.SUCCESS_CODE.equals(authAccessTOkenRsp.getErrcode()) ){
    		token.setAcceccTokenCreateTime(new Date());
    		token.setAuthorizerAccessToken(authAccessTOkenRsp.getAuthorizerAccessToken());
    		token.setAuthorizerRefreshToken(authAccessTOkenRsp.getAuthorizerRefreshToken());
    		/**重新更新数据库对应数据*/
    		weChatTokenDao.save(token); 
    		return authAccessTOkenRsp;
    	}else{
    		throw new GlobalException(new WeChatExceptionInfo(authAccessTOkenRsp.getErrcode(), authAccessTOkenRsp.getErrmsg())); 
    	}
    }
    
    /**
     * 检测开放平台ComponentAccessToken 是否存在或过期,在失效或不存在的情况下，系统会自动刷新
     */
    protected AbstractWeChatToken validateComponentAccessToken() throws GlobalException {
		// 获取开发平台应用配置信息
		AbstractWeChatOpen open = weChatOpenDao.findByHasDeleted(false);
		if (open != null && open.getAppId() != null) {
			// 获取当前appId对应的token
			AbstractWeChatToken token = weChatTokenDao.findByAppId(open.getAppId());
			if (token != null && token.getComponentVerifyTicket() != null) {
				// 当前毫秒数
				Long currentMillisecond = System.currentTimeMillis();
				// token 生成时的毫秒数
				Long createMillisecond = token.getAcceccTokenCreateTime() == null ? 0L : token.getAcceccTokenCreateTime().getTime();
				// 判断componentAccessToken生成时间与当前时间相差超过5400000毫秒（一个半小时）,系统再次通过component_verify_ticket
				// 进行刷新并修改
				if (currentMillisecond - createMillisecond > Const.Expire.VALUE || token.getComponentAccessToken() == null) {
					ComponentAccessTokenRequest componentATR = new ComponentAccessTokenRequest(open.getAppId(), open.getAppSecret(), token.getComponentVerifyTicket());
					ComponentAccessTokenResponse response = (ComponentAccessTokenResponse) referComponentAccessToken(componentATR, token);
					  token.setComponentAccessToken(response.getComponentAccessToken());
				}
				return token;
			} else {
				// 如数据库未找到相应数据，直接提示配置开放平台未设置或者请等待10分钟等待微信开放平台推送相关的component_verify_ticket
				throw new GlobalException(new WeChatExceptionInfo(
						SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR.getCode(),
						SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR
								.getDefaultMessage()));
			}
		}else{
			// 如数据库未找到相应数据，直接提示配置开放平台未设置或者请等待10分钟等待微信开放平台推送相关的component_verify_ticket
			throw new GlobalException(new WeChatExceptionInfo(
					SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR.getCode(),
					SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR
							.getDefaultMessage()));
		}
	}
    
    /**
     * 重新刷新componentAccessToken
     * @throws GlobalException 
     */
    protected ComponentAccessTokenResponse referComponentAccessToken(ComponentAccessTokenRequest componentATR,AbstractWeChatToken token) throws GlobalException{
    	//使用component_verify_ticket刷新componentAccessToken
    	ComponentAccessTokenResponse  componentAT =  openApiService.getComponentTokenApi(componentATR);
    	if(componentAT.SUCCESS_CODE.equals(componentAT.getErrcode()) ){
    		token.setAcceccTokenCreateTime(new Date());
    		token.setComponentAccessToken(componentAT.getComponentAccessToken());
    		/**重新更新数据库对应数据*/
    		weChatTokenDao.save(token); 
    		return componentAT;
    	}else{
    		throw new GlobalException(new WeChatExceptionInfo(componentAT.getErrcode(), componentAT.getErrmsg())); 
    	}
    }

}
