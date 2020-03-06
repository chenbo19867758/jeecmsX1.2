package com.jeecms.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.api.applet.GetCategoryApiService;
import com.jeecms.common.wechat.api.applet.ModifyDomainApiService;
import com.jeecms.common.wechat.api.open.OpenPlatformApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.applet.ModifyDomainRequest;
import com.jeecms.common.wechat.bean.request.open.AuthorizerInfoRequest;
import com.jeecms.common.wechat.bean.request.open.ComponentVerifyTicketRequest;
import com.jeecms.common.wechat.bean.request.open.QueryAuthRequest;
import com.jeecms.common.wechat.bean.response.applet.GetCategoryResponse;
import com.jeecms.common.wechat.bean.response.applet.ModifyDomainData;
import com.jeecms.common.wechat.bean.response.open.AuthorizerInfoResponse;
import com.jeecms.common.wechat.bean.response.open.AuthorizerInfoResponse.AuthorizationInfo.FuncInfo;
import com.jeecms.common.wechat.bean.response.open.AuthorizerInfoResponse.AuthorizerInfo.MiniProgramInfo;
import com.jeecms.common.wechat.bean.response.open.QueryAuthResponse;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.wechat.dao.AbstractWeChatInfoDao;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.AbstractWeChatOpen;
import com.jeecms.wechat.domain.AbstractWeChatToken;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.AbstractWeChatOpenService;
import com.jeecms.wechat.service.AbstractWeChatTokenService;

/**
 * AbstractWeChatInfo
 * @author: qqwang
 * @date: 2018年4月16日 上午11:05:40
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */
@Service
public class AbstractWeChatInfoServiceImpl extends BaseServiceImpl<AbstractWeChatInfo, AbstractWeChatInfoDao, Integer>
		implements AbstractWeChatInfoService {

	@Autowired
	private AbstractWeChatOpenService openService;
	@Autowired
	private AbstractWeChatTokenService weChatTokenService;
	@Autowired
	private OpenPlatformApiService openApiService;
	@Autowired
	private GetCategoryApiService getCategoryApiService;
	@Autowired
	private GetCategoryApiService categoryApiService;
	@Autowired
	private ModifyDomainApiService modifyDomainApiService;
	@Autowired
	private CmsSiteService siteService;

	/**
	 * 系统默认一个公众号小程序只能同时授权同一个站点，如果授权多个
	 */
	@Override
	public AbstractWeChatInfo requestAuthorizeInfo(String componentAppid, String authorizerAppId,
			ValidateToken validToken, Integer siteId, String authorizerToken) throws GlobalException {
		AuthorizerInfoRequest authorizerReq = new AuthorizerInfoRequest(componentAppid, authorizerAppId);
		AuthorizerInfoResponse authorizerRsp = openApiService.getAuthorizerInfo(authorizerReq, validToken);
		// 授权账户类型（公众号或小程序）
		Short weChatType = null; 
		// 小程序服务器域名清单
		String netWork = ""; 
		// 小程序服务类目
		String categories = "";
		// 返回参数中MiniProgramInfo不为空则识别为小程序，反之为公众号
		MiniProgramInfo propramInfos = authorizerRsp.getAuthorizerInfo().getMiniProgramInfo();
		if (propramInfos != null) {
			weChatType = Const.WeCahtType.SMALL_PROGRAM;
			netWork = SerializeUtil.beanToJson(propramInfos.getNetwork());
			// 通过api获取小程序的服务类目
			validToken.setAccessToken(authorizerToken);
			GetCategoryResponse response = getCategoryApiService.getCategory(validToken);
			categories = SerializeUtil.beanToJson(response.getCategoryList());
			// 此处校验是因为有一个限制：一个站点只能最多绑定一个小程序
			List<AbstractWeChatInfo> infos = this.findWeChatInfo(Const.WeCahtType.SMALL_PROGRAM, siteId);
			if (infos != null && infos.size() > 0) {
				if (!infos.get(0).getAppId().equals(authorizerAppId)) {
					throw new GlobalException(new WeChatExceptionInfo(
							RPCErrorCodeEnum.THE_SITE_HAS_BEEN_BOUND_TO_THE_APPLET.getCode(),
							RPCErrorCodeEnum.THE_SITE_HAS_BEEN_BOUND_TO_THE_APPLET.getDefaultMessage()));
				}
			}
		} else {
			weChatType = Const.WeCahtType.PUBLIC_ACCOUNT;
		}
		AbstractWeChatInfo weChatInfo = this.findWeChatInfo(authorizerAppId);
		if (weChatInfo != null) {
			if (!siteId.equals(weChatInfo.getSiteId())) {
				CmsSite site = siteService.findById(weChatInfo.getSiteId());
				throw new GlobalException(new WeChatExceptionInfo(
						RPCErrorCodeEnum.THE_WECHAT_OR_APPLET_IS_ALREADY_BOUND.getCode(),
						"该公众号/小程序已绑定在了站点:" + site.getName() + ",请解绑后重试或更换公众号重试"));
			}
		} else {
			weChatInfo = new AbstractWeChatInfo();
		}
		weChatInfo.setHasDeleted(false);
		// 设置头像
		weChatInfo.setHeadImg(authorizerRsp.getAuthorizerInfo().getHeadImg());
		// 设置授权账户类型（公众号或小程序）
		weChatInfo.setType(weChatType);
		// 设置小程序服务器域名清单
		weChatInfo.setNetwork(netWork);
		// 设置小程序服务类目
		weChatInfo.setCategories(categories);
		// 设置账号拥有者
		weChatInfo = setweChatInfo(weChatInfo,authorizerRsp);
		weChatInfo.setSiteId(siteId);
		AbstractWeChatInfo defaultAuth = this.findDefault(weChatType, siteId);
		if (defaultAuth != null) {
			weChatInfo.setIsDefaultAuth(false);
		} else {
			weChatInfo.setIsDefaultAuth(true);
		}
		// 设置权限集
		List<FuncInfo> funcs = authorizerRsp.getAuthorizationInfo().getFuncInfo();
		if (funcs != null && funcs.size() > 0) {
			StringBuffer auths = new StringBuffer();
			for (FuncInfo fun : funcs) {
				auths.append(fun.getFuncscopeCategory().getId() + ",");
			}
			weChatInfo.setFuncInfo(auths.toString());
		}
		// 为防止公众号重复授权、修改权限等操作，此处可能执行 更新或保存wechatinfo信息
		super.dao.save(weChatInfo);
		validToken.setAccessToken(authorizerToken);
		return weChatInfo;
	}
	
	/**
	 * 初始化WeChatInfo
	 */
	public AbstractWeChatInfo setweChatInfo(AbstractWeChatInfo weChatInfo,
			AuthorizerInfoResponse authorizerRsp) throws GlobalException {
		// 设置原始ID
		weChatInfo.setGlobalId(authorizerRsp.getAuthorizerInfo().getUserName());
		// 设置授权类型（暂时只有授权接入 1）
		weChatInfo.setGrantType((short) 1);
		// 设置公众号或小程序appid
		weChatInfo.setAppId(authorizerRsp.getAuthorizationInfo().getAuthorizerAppid());
		// 设置名称
		weChatInfo.setWechatName(authorizerRsp.getAuthorizerInfo().getNickName());
		// 设置主体账号信息
		weChatInfo.setPrincipalName(authorizerRsp.getAuthorizerInfo().getPrincipalName());
		// 设置账号类型
		weChatInfo.setWechatType(authorizerRsp.getAuthorizerInfo().getServiceTypeInfo().getId());
		// 设置认证信息
		weChatInfo.setVerifyStatus(
				authorizerRsp.getAuthorizerInfo().getVerifyTypeInfo().getId() > -1 
				? 
				(short) 2 : (short) 1);
		if (AbstractWeChatInfo.TYPE_WECHAT.equals(weChatInfo.getType())) {
			// 设置二维码
			weChatInfo.setQrcodeUrl(authorizerRsp.getAuthorizerInfo().getQrcodeUrl());
		}
		weChatInfo.setIsSetAdmin(false);
		weChatInfo.setIsDefaultAuth(true);
		List<AbstractWeChatInfo> infos = this.findWeChatInfo(weChatInfo.getType(), weChatInfo.getSiteId());
		if (infos != null && infos.size() > 0) {
			weChatInfo.setIsDefaultAuth(false);
		}
		return weChatInfo;
	}

	@Override
	public AbstractWeChatToken requestAuthorizeAccessToken(String extra, String authCode) throws GlobalException {
		// 获取开放平台应用配置信息
		AbstractWeChatOpen openInfo = openService.findOpenConfig();
		if (openInfo == null) {
			// 如数据库未找到相应数据，直接提示配置开放平台未设置或者请等待10分钟等待微信开放平台推送相关的component_verify_ticket
			throw new GlobalException(new WeChatExceptionInfo(
					SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR.getCode(),
					SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR.getDefaultMessage()));
		}
		// 获取开放平台应用token信息
		AbstractWeChatToken weChatToken = weChatTokenService.findByAppId(openInfo.getAppId());
		if (weChatToken == null) {
			// 如数据库未找到相应数据，直接提示配置开放平台未设置或者请等待10分钟等待微信开放平台推送相关的component_verify_ticket
			throw new GlobalException(new WeChatExceptionInfo(
					SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR.getCode(),
					SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR.getDefaultMessage()));
		}
		ValidateToken validToken = new ValidateToken();
		validToken.setAccessToken(weChatToken.getComponentAccessToken());
		QueryAuthRequest authRequest = new QueryAuthRequest();
		// 第三方平台appid
		authRequest.setComponentAppid(openInfo.getAppId());
		// 授权code,当前授权回调URL获取
		authRequest.setAuthorizationCode(authCode);
		Integer siteId = null;
		if (extra != null && !extra.equals(OpenPlatformApiService.EXTERA)) {
			// 获取站点id
			String[] params = extra.split("_");
			if (params.length > 1) {
				siteId = Integer.parseInt(params[1]);
			}
		}
		if (siteId == null) {
			throw new GlobalException(new WeChatExceptionInfo(
					RPCErrorCodeEnum.SITE_CANNOT_BE_EMPTY.getCode(),
					RPCErrorCodeEnum.SITE_CANNOT_BE_EMPTY.getDefaultMessage()));
		}
		QueryAuthResponse authResponse = openApiService.queryAuthApi(authRequest, validToken);
		// 考虑当前公众号或小程序存在重复授权、更改授权权限等操作，对jc_wechat_token进行修改或新增判断
		AbstractWeChatToken token = weChatTokenService
				.findByAppId(authResponse.getAuthorizationInfo().getAuthorizerAppid());
		if (token == null) {
			token = new AbstractWeChatToken();
		}
		token.setAppId(authResponse.getAuthorizationInfo().getAuthorizerAppid());
		token.setAuthorizerAccessToken(authResponse.getAuthorizationInfo().getAuthorizerAccessToken());
		token.setAuthorizerRefreshToken(authResponse.getAuthorizationInfo().getAuthorizerRefreshToken());
		// 新增或修改jc_wechat_token表数据
		weChatTokenService.save(token); 

		// 获取授权方的帐号基本信息(含小程序或公众号)
		this.requestAuthorizeInfo(openInfo.getAppId(), token.getAppId(), validToken, siteId,
				token.getAuthorizerAccessToken());
		return token;
	}

	/**
	 * 获取站点下小程序或公众号集合
	 */
	@Override
	public List<AbstractWeChatInfo> findWeChatInfo(Short type, Integer siteId) {
		return dao.findByTypeAndSiteIdAndHasDeleted(type, siteId,false);
	}

	/**
	 * 检测该站点下该小程序、公众号是否已绑定
	 */
	private AbstractWeChatInfo findWeChatInfo(String appId) {
		return dao.findByAppIdAndHasDeleted(appId, false);
	}
	
	/**
	 * 通过AppId查询
	 */
	@Override
	public AbstractWeChatInfo findAppId(String appId) {
		return dao.findByAppIdAndHasDeleted(appId, false);
	}
	

	@Override
	public AbstractWeChatInfo findDefault(Short type, Integer siteId) {
		return dao.findByTypeAndSiteIdAndIsDefaultAuthAndHasDeleted(type, siteId, true, false);
	}

	@Override
	public ResponseInfo getCategory(Integer id) throws GlobalException {
		AbstractWeChatInfo abs = super.findById(id);
		String appid = abs.getAppId();
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appid);
		GetCategoryResponse category = categoryApiService.getNewCategory(validToken);
		/**
		 * 将最新的数据修改入数据库
		 */
		abs.setCategories(JSONObject.toJSON(category.getCategoryList()).toString());
		super.update(abs);
		return new ResponseInfo(JSONObject.toJSON(category.getCategoryList()));
	}

	@Override
	public ResponseInfo getModifyDomain(Integer id) throws GlobalException {
		AbstractWeChatInfo abs = super.findById(id);
		String appid = abs.getAppId();
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appid);
		ModifyDomainData domainResponse = modifyDomainApiService.getModifyDomain(validToken);
		return new ResponseInfo(domainResponse);
	}

	@Override
	public ResponseInfo setModifyDomain(HttpServletRequest request,Integer id) throws GlobalException {
		AbstractWeChatInfo abs = super.findById(id);
		String appid = abs.getAppId();
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appid);
		// 获取后台服务器域名
		String url = RequestUtils.getServerUrl(request);
		List<String> reqUrl = new ArrayList<>();
		reqUrl.add(url.substring(7));
		ModifyDomainRequest domain = new ModifyDomainRequest();
		domain.setDownloaddomain(reqUrl);
		domain.setRequestdomain(reqUrl);
		domain.setUploaddomain(reqUrl);
		domain.setWsrequestdomain(reqUrl);
		ModifyDomainData domainResponse = modifyDomainApiService.setModifyDomain(validToken, domain);
		abs.setNetwork(JSONObject.toJSONString(domainResponse));
		super.update(abs);
		return new ResponseInfo(domainResponse);
	}

	@Override
	public void deleteAbstract(ComponentVerifyTicketRequest componentVerifyTicket) throws GlobalException {
		// 查询该appId是否存在数据库中
		AbstractWeChatInfo wechatInfo = dao.findByAppIdAndHasDeleted(
				componentVerifyTicket.getAuthorizerAppid(), false);
		if (wechatInfo != null) {
			super.delete(wechatInfo);
		}
	}
	
	@Override
	public void checkWeChatAuth(Integer userId, Integer wechatId,String appId) throws GlobalException {
		AbstractWeChatInfo weChatInfo = null;
		if (wechatId != null) {
			weChatInfo = super.findById(wechatId);
		}
		if (weChatInfo == null && StringUtils.isNotBlank(appId)) {
			weChatInfo = dao.findByAppIdAndHasDeleted(appId, false);
		}
		String code, message = null;
		if (weChatInfo == null) {
			code = UserErrorCodeEnum.NO_OPERATE_WECHAT_PERMISSION.getCode();
			message = MessageResolver.getMessage(code,
					UserErrorCodeEnum.NO_OPERATE_WECHAT_PERMISSION.getDefaultMessage());
			throw new GlobalException(new WeChatExceptionInfo(code, message));
		}
		List<Integer> userIds = weChatInfo.getUsers().stream().map(CoreUser::getId)
				.collect(Collectors.toList());
		// 判断公众号当前设置管理员及用户是否为管理员中一员
		if (weChatInfo.getIsSetAdmin() && !userIds.contains(userId)) {
			code = UserErrorCodeEnum.NO_OPERATE_WECHAT_PERMISSION.getCode();
			message = MessageResolver.getMessage(code,
					UserErrorCodeEnum.NO_OPERATE_WECHAT_PERMISSION.getDefaultMessage());
			throw new GlobalException(new WeChatExceptionInfo(code, message));
		}
	}
	

}
