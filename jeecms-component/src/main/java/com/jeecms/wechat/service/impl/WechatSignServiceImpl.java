package com.jeecms.wechat.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.wechat.api.mp.ShareApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.response.mp.share.GetTicketResponse;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.dto.WechatSignVo;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatSignService;

/**
 * 微信分享service实现类
 * @author: chenming
 * @date:   2019年11月1日 上午9:36:24
 */
@Service
public class WechatSignServiceImpl implements WechatSignService {
	/** 微信分享缓存块*/
	public static final String WECHAT_SIGN = "wechatSign";
	/** 微信分享token名称*/
	public static final String JSAPI_TICKET = "jsapiTicket";
	/** 微信分享token截止时间名称*/
	public static final String END_TIME = "endTime";
	
	@Override
	public WechatSignVo sign(HttpServletRequest request,String url) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		AbstractWeChatInfo wechat = wechatInfoService.findDefault(AbstractWeChatInfo.TYPE_WECHAT, siteId);
		if (wechat == null) {
			throw new GlobalException(RPCErrorCodeEnum.NOT_CONFIG_WECHAT);
		}
		String ticket = this.getJsapiTicket(request,wechat.getAppId());
		String nonceStr = createNonceStr();
		String timestamp = createTimestamp();
		String signParameter;
		String signature = "";
		// 注意这里参数名必须全部小写，且必须有序
		signParameter = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(signParameter.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		WechatSignVo dto = new WechatSignVo(wechat.getAppId(), timestamp, nonceStr, signature);
		return dto;
	}

	/**
	 * 签名
	 */
	private String byteToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * 获取组装ValidateToken
	 */
	private ValidateToken getToken(String appId) {
		ValidateToken validateToken = new ValidateToken();
		validateToken.setAppId(appId);
		return validateToken;
	}
	
	/**
	 * 获取签名jsapiTicket，有从缓存中取，没有则查询
	 */
	private String getJsapiTicket(HttpServletRequest request,String appId) throws GlobalException{
		Long newTime = System.currentTimeMillis();
		String ticket = null;
		if (cacheProvider.exist(WechatSignServiceImpl.WECHAT_SIGN, WechatSignServiceImpl.END_TIME)) {
			Long endTime = Long.valueOf(cacheProvider.getCache(WechatSignServiceImpl.WECHAT_SIGN, WechatSignServiceImpl.END_TIME).toString());
			if (newTime >= endTime) {
				ticket =  this.getJsapiTicket(request, appId, newTime);
			} else {
				ticket =  cacheProvider.getCache(WechatSignServiceImpl.WECHAT_SIGN, WechatSignServiceImpl.JSAPI_TICKET).toString();
			}
		} else {
			ticket = this.getJsapiTicket(request, appId, newTime);
		}
		return ticket;
	}
	
	/**
	 * 处理具体的获取ticket业务
	 */
	private String getJsapiTicket(HttpServletRequest request,String appId,Long time) throws GlobalException{
		ValidateToken validateToken = this.getToken(appId);
		GetTicketResponse response = shareApiService.getTicket(validateToken);
		if (response != null) {
			String ticket = response.getTicket();
			cacheProvider.setCache(WechatSignServiceImpl.WECHAT_SIGN, WechatSignServiceImpl.JSAPI_TICKET, ticket);
			// ticket的有效时长为7200秒，未了防止出现前后端加载慢导致时间反应过去的缘故，所以此处给与的时间为 7150秒
			cacheProvider.setCache(WechatSignServiceImpl.WECHAT_SIGN, WechatSignServiceImpl.END_TIME, time + (7150 * 1000));
			return response.getTicket();
		} 
		return null;
	}
	
	/**
	 * 创建签名随机串
	 */
	private String createNonceStr() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 创建签名时间戳
	 */
	private static String createTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	
	@Autowired
	private ShareApiService shareApiService;
	@Autowired
	private AbstractWeChatInfoService wechatInfoService;
	@Autowired
	private CacheProvider cacheProvider;
}
