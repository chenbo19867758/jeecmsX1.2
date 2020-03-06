package com.jeecms.front.controller;

import static com.jeecms.common.constants.SysConstants.TPLDIR_WECHAT;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.api.open.OpenPlatformApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.message.KeyReplyRequest;
import com.jeecms.common.wechat.bean.request.mp.news.ReqMessage;
import com.jeecms.common.wechat.bean.request.open.ComponentAccessTokenRequest;
import com.jeecms.common.wechat.bean.request.open.ComponentVerifyTicketRequest;
import com.jeecms.common.wechat.bean.request.open.PreauthCodeRequest;
import com.jeecms.common.wechat.bean.request.open.VerifyTicketRequest;
import com.jeecms.common.wechat.bean.response.open.ComponentAccessTokenResponse;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;
import com.jeecms.common.wechat.util.signature.MsgCrypt;
import com.jeecms.util.FrontUtils;
import com.jeecms.wechat.domain.AbstractWeChatOpen;
import com.jeecms.wechat.domain.AbstractWeChatToken;
import com.jeecms.wechat.domain.WechatReplyKeyword;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.AbstractWeChatOpenService;
import com.jeecms.wechat.service.AbstractWeChatTokenService;
import com.jeecms.wechat.service.MiniprogramVersionService;
import com.jeecms.wechat.service.WechatReplyKeywordService;

/**
 * 开放平台授权控制器
 * 
 * @author: qqwang
 * @date: 2018年6月20日 上午9:31:02
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RequestMapping(value = "/weChat")
@Controller
public class WechatController {
	private static final Logger LOGGER = LoggerFactory.getLogger(WechatController.class);

	@Autowired
	private AbstractWeChatOpenService openService;
	@Autowired
	private AbstractWeChatInfoService weChatInfoService;
	@Autowired
	private AbstractWeChatTokenService weChatTokenService;
	@Autowired
	private OpenPlatformApiService openApiService;
	@Autowired
	private WechatReplyKeywordService wechatReplyKeywordService;
	@Autowired
	private MiniprogramVersionService mVersionService;
	@Autowired
	private CacheProvider cacheProvider;

	/**
	 * 微信开放平台服务推送请求接口地址
	 * 
	 * @param: request
	 *             HttpServletRequest
	 * @throws GlobalException
	 *             GlobalException
	 * @throws IOException
	 *             IOException
	 * @return: String
	 */
	@RequestMapping(value = "/authCallBack")
	@ResponseBody
	public void authCallBack(HttpServletRequest request, HttpServletResponse response) throws GlobalException, IOException {
		// 获取微信开发平台服务器推送的加密xml信息
		VerifyTicketRequest verifyTicketRequest = SerializeUtil.xmlInputStreamToBean(request.getInputStream(),
				VerifyTicketRequest.class);
		// 获取微信开放平台第三方应用配置信息
		AbstractWeChatOpen openInfo = openService.findOpenConfig();
		if (openInfo == null) {
			LOGGER.info("开放平台公众号暂时未设置，无法获取对应信息");
			response.getWriter().write("success");
			response.getWriter().flush();
			//return "success";
		} else {
			// 解密ComponentVerifyTicket
			String decryptStr = MsgCrypt.decrypt(openInfo.getAppId(), openInfo.getMessageDecryptKey(),
					verifyTicketRequest.getEncrypt().trim());
			// 解析微信开发平台服务器推送的xml信息
			ComponentVerifyTicketRequest componentVerifyTicket = SerializeUtil.xmlToBean(decryptStr,
					ComponentVerifyTicketRequest.class);
			// 当微信开放平台第一次推送时才去获取compontentAccessToken
			// ,后续每次10分钟推送只更新本地的ComponentVerifyTicket
			AbstractWeChatToken weChatToken = weChatTokenService.findByAppId(openInfo.getAppId());
			// 如果在微信端进行取消授权，会推送到
			if ("unauthorized".equals(componentVerifyTicket.getInfoType())) {
				weChatInfoService.deleteAbstract(componentVerifyTicket);
			} else {
				boolean check = false;
				if (weChatToken != null && weChatToken.getComponentAccessToken() == null) {
					check = true;
				}
				if (weChatToken == null || check) {
					// 通过解密后的ComponentVerifyTicket获取component_access_token
					ComponentAccessTokenRequest componentATR = 
							new ComponentAccessTokenRequest(openInfo.getAppId(),
									openInfo.getAppSecret(), componentVerifyTicket
									.getComponentVerifyTicket());
					// 发起接口调用
					ComponentAccessTokenResponse componentTokenRsp = 
							openApiService.getComponentTokenApi(componentATR);
					weChatToken = new AbstractWeChatToken();
					weChatToken.setAppId(openInfo.getAppId());
					weChatToken.setComponentAccessToken(componentTokenRsp.getComponentAccessToken());
					weChatToken.setComponentVerifyTicket(componentVerifyTicket.getComponentVerifyTicket());
					weChatToken.setAcceccTokenCreateTime(new Date());
					weChatTokenService.save(weChatToken);
				} else {
					weChatToken.setComponentVerifyTicket(componentVerifyTicket.getComponentVerifyTicket());
					weChatTokenService.update(weChatToken);
				}
			}
			response.getWriter().write("success");
			response.getWriter().flush();
			//return "success";
		}
	}

	/**
	 * 公众号或小程序扫码授权
	 * @param: request HttpServletRequest
	 * @throws GlobalException GlobalException
	 * @return: String
	 */
	@RequestMapping(value = "/grantAuth", method = RequestMethod.GET)
	public String grantAuth(HttpServletRequest request, Integer siteId, Model model) throws GlobalException {
		AbstractWeChatOpen openInfo = openService.findOpenConfig();
		if (openInfo == null) {
			// 如数据库未找到相应数据，直接提示配置开放平台未设置或者请等待10分钟等待微信开放平台推送相关的component_verify_ticket
			throw new GlobalException(new WeChatExceptionInfo(
					SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR.getCode(),
					SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR.getDefaultMessage()));
		}
		AbstractWeChatToken weChatToken = weChatTokenService.findByAppId(openInfo.getAppId());
		if (weChatToken == null) {
			// 如数据库未找到相应数据，直接提示配置开放平台未设置或者请等待10分钟等待微信开放平台推送相关的component_verify_ticket
			throw new GlobalException(new WeChatExceptionInfo(
					SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR.getCode(),
					SystemExceptionEnum.WEIXIN_OPEN_APP_ERROR.getDefaultMessage()));
		}
		ValidateToken validToken = new ValidateToken();
		validToken.setAccessToken(weChatToken.getComponentAccessToken());
		PreauthCodeRequest preCodeReq = new PreauthCodeRequest();
		preCodeReq.setComponentAppid(openInfo.getAppId());
		// 获取附带当前绑定的公众号或小程序拥有者ID业务参数
		String redirectUri = "";
		if (siteId != null) {
			redirectUri = "site_" + siteId;
		}
		// 获取预授权码并返回授权扫码地址
		String grantAuthUrl = openApiService.getPreAuthCodeApi(preCodeReq, validToken, request, redirectUri);
		// 为了解决微信开放平台授权发起页面域名与回调域名一致问题，系统采用中继页面进行跳转
		model.addAttribute("redirectUrl", grantAuthUrl);
		return FrontUtils.getSysPagePath(request, TPLDIR_WECHAT, "wechat_auth");
	}

	/**
	 * 微信授权回调地址
	 * @param: request
	 * @throws GlobalException GlobalException
	 * @throws IOException IOException
	 * @return: QueryAuthResponse
	 */
	@RequestMapping(value = "/authBindNotify/{extra}")
	public String queryAuth(@PathVariable("extra") String extra, String auth_code, Integer expires_in,
			HttpServletRequest request,ModelMap model) throws GlobalException {
	
		
		String code = "500";
		String message = "";
		try {
			// 处理回调业务，
			// 1、使用授权码换取公众号或小程序的接口调用凭据和授权信息 并保存授权方token信息至jc_wechat_token
			// 2、获取授权方的帐号基本信息（公众号或小程序） ,并保存详细信息至 jc_wechat_info
			weChatInfoService.requestAuthorizeAccessToken(extra, auth_code);
			code = "200";
		} catch (GlobalException e) {
			if(e.getExceptionInfo() != null){
				code = e.getExceptionInfo().getCode();
				message = MessageResolver.getMessage(e.getExceptionInfo().getCode(), e.getExceptionInfo().getDefaultMessage());
			}else{
				message = e.getMessage();
			}
		}
		//回调成功或失败提示页面
		model.put("code", code);
		model.put("message", message);
		return FrontUtils.getSysPagePath(request, TPLDIR_WECHAT, "auth_success");
	}

	/**
	 * MessageReply 微信公众号消息时间回调
	 * @param: appId appId
	 * @param: request HttpServletRequest
	 * @param: response HttpServletResponse
	 * @throws GlobalException GlobalException
	 * @throws IOException IOException
	 * @throws ParseException ParseException
	 */
	@RequestMapping(value = "/keyword/reply/{appId}", method = RequestMethod.POST)
	@ResponseBody
	public String messageReply(@PathVariable("appId") String appId, HttpServletRequest request,
			HttpServletResponse response) throws GlobalException, IOException, ParseException {
		KeyReplyRequest keyReplyRequest = SerializeUtil.xmlInputStreamToBean(request.getInputStream(),
				KeyReplyRequest.class);
		
		// 获取微信开放平台第三方应用配置信息
		AbstractWeChatOpen openInfo = openService.findOpenConfig();
		// 解密 Encrypt
		String decryptStr = MsgCrypt.decrypt(openInfo.getAppId(), openInfo.getMessageDecryptKey(),
				keyReplyRequest.getEncrypt().trim());
		// 解析解密后的的xml信息
		ReqMessage reqMessage = SerializeUtil.xmlToBean(decryptStr, ReqMessage.class);
		/**
		 * 微信小程序审核回调
		 */
		if (Const.Mssage.EVENT_TYPE_AUDIT_STATE.equals(reqMessage.getEvent())
				|| Const.Mssage.EVENT_TYPE_AUDIT_FAIL.equals(reqMessage.getEvent())) {
			mVersionService.updateAuditStatus(appId, reqMessage);
			return "";
		}
		if (Const.Mssage.EVENT_TYPE_VIEW.equals(reqMessage.getEvent())) {
			return "";
		}
		if (cacheProvider.exist(WechatReplyKeyword.REPLY_KEY, reqMessage.getFromUserName()+reqMessage.getCreateTime())) {
			return "";
		}
		String messageReply = wechatReplyKeywordService.getMessageReply(reqMessage, appId);
		cacheProvider.setCache(WechatReplyKeyword.REPLY_KEY, reqMessage.getFromUserName()+reqMessage.getCreateTime(), true);
		return messageReply;
	}
}
