package com.jeecms.common.wechat.api.open;

import javax.servlet.http.HttpServletRequest;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.open.AuthorizerAccessTokenRequest;
import com.jeecms.common.wechat.bean.request.open.AuthorizerInfoRequest;
import com.jeecms.common.wechat.bean.request.open.ComponentAccessTokenRequest;
import com.jeecms.common.wechat.bean.request.open.PreauthCodeRequest;
import com.jeecms.common.wechat.bean.request.open.QueryAuthRequest;
import com.jeecms.common.wechat.bean.response.open.AuthorizerAccessTokenResponse;
import com.jeecms.common.wechat.bean.response.open.AuthorizerInfoResponse;
import com.jeecms.common.wechat.bean.response.open.ComponentAccessTokenResponse;
import com.jeecms.common.wechat.bean.response.open.QueryAuthResponse;

/**
 * 
 * @Description:微信第三方开放平台应用授权流程接口集
 * @author: wangqq
 * @date:   2018年7月25日 下午4:15:46     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface OpenPlatformApiService {
	/**授权扫码确认回调地址额外参数默认*/
	public static final String EXTERA  = "extera";
	
	/**
	 * 获取开放平台应用component_access_token
	 * @Title: getComponentTokenApi  
	 * @param comAccessTokenATR form-data请求参数对象
	 * @return
	 * @throws GlobalException      
	 * @return: ComponentAccessTokenResponse
	 */
	ComponentAccessTokenResponse getComponentTokenApi(ComponentAccessTokenRequest comAccessTokenATR)throws GlobalException;
	
	/**
	 * 获取开放平台应用代为调用微信公众号或小程序授权的token，需要进行验证component_access_token
	 * @Title: getAuthorizerTokenApi  
	 * @param accessTokenATR form-data请求参数对象
	 * @param validToken
	 * @return
	 * @throws GlobalException      
	 * @return: AuthorizerAccessTokenResponse
	 */
	AuthorizerAccessTokenResponse getAuthorizerTokenApi(AuthorizerAccessTokenRequest  accessTokenATR,ValidateToken validToken)throws GlobalException;
    
    /**
     * 获取微信公众号及小程序 网页预授权码并返回授权扫码地址，需要进行验证component_access_token
     * @Title: getPreAuthCodeApi  
     * @Description: 回调地址默认格式为：  https(http)://域名/weChat/$extra/authBindNotify  ,$extra 为动态业务参数(即本接口的redirectUri参数值)，默认为  "extra"，
     * redirectUri 为回调地址中的额外参数，如实际业务需要回调地址带上多余的业务参数，需要在调用本次接口的同时，通过此参数带上扩展业务参数，参数规则需要自行约定，
     * 如传递storeid_12 ,则最终回调地址为 https(http)://域名/weChat/storeid_12/authBindNotify
     * @param preCodeReq
     * @param accessTokenATR  form-data请求参数对象
     * @param validToken
     * @param request  HttpServletRequest对象
     * @param redirectUri  扫描授权地址中添加的额外参数，支持下划线、英文字母，不填则默认为extra
     * @param: @return      
     * @throws GlobalException
     * @return BaseResponse
     */
    String getPreAuthCodeApi(PreauthCodeRequest  preCodeReq,ValidateToken validToken,HttpServletRequest request,String redirectUri)throws GlobalException;
	
    /**
     * 使用授权码换取公众号或小程序的接口调用凭据和授权信息，需要进行验证component_access_token
     * @Title: queryAuthApi  
     * @param authRequest form-data请求参数对象
     * @param validToken
     * @return
     * @throws GlobalException      
     * @return: QueryAuthResponse
     */
    QueryAuthResponse  queryAuthApi(QueryAuthRequest  authRequest,ValidateToken validToken)throws GlobalException;


    /**
     * 获取授权方的帐号基本信息，需要进行验证component_access_token
     * @Title: getAuthorizerInfo  
     * @param authorizerInfoRequest form-data请求参数对象
     * @param validToken
     * @return
     * @throws GlobalException      
     * @return: AuthorizerInfoResponse
     */
    AuthorizerInfoResponse getAuthorizerInfo(AuthorizerInfoRequest authorizerInfoRequest,ValidateToken validToken)throws GlobalException;
}

 