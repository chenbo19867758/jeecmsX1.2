/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.front.controller;

import static com.jeecms.common.constants.SysConstants.TPLDIR_WEIBO;
import static com.jeecms.common.constants.TplConstants.TPLDIR_COMMON;
import static com.jeecms.common.web.cache.CacheConstants.MEMBER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.domain.ThirdPartyResultDTO;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.SnowFlake;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.weibo.Constants;
import com.jeecms.member.service.ThirdPartyUserService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysThird;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.SysThirdService;
import com.jeecms.util.FrontUtils;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.weibo.domain.WeiboAppConfig;
import com.jeecms.weibo.domain.vo.WeiboTokenVO;
import com.jeecms.weibo.service.WeiboAppConfigService;
import com.jeecms.weibo.service.WeiboInfoService;


/**   
 * 微博控制器
 * @author: ljw
 * @date:   2019年6月14日 上午9:51:27     
 */
@RequestMapping(value = "/weibo")
@Controller
public class WeiboController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeiboController.class);
	
	@Autowired
	private WeiboAppConfigService weiboAppConfigService;
	@Autowired
	private WeiboInfoService weiboInfoService;
	@Autowired
	private CacheProvider cacheProvider;
	@Autowired
	private ThirdPartyUserService thirdPartyUserService;
	@Autowired
	private SysThirdService thirdService;
	@Autowired
	private CmsSiteService cmsSiteService;
	
	/**
	 * 点击授权
	* @Title: auth 
	* @param request 请求
	* @param model 
	* @return
	* @throws GlobalException
	* @throws IOException
	 */
	@GetMapping()
	@ResponseBody
	public ResponseInfo auth(HttpServletRequest request, HttpServletResponse response) 
			throws GlobalException, IOException {
		//项目地址
		String uri = RequestUtils.getServerUrl(request);
		//回调地址
		String redirectUri = uri + "/weibo/authCallBack";
		//获得站点ID
		Integer siteId = SystemContextUtils.getSiteId(request);
		//根据站点去查询应用
		WeiboAppConfig weibo = weiboAppConfigService.getBySiteId(siteId);
		if (weibo == null) {
			throw new GlobalException(new WeChatExceptionInfo(RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getCode(),
					RPCErrorCodeEnum.THIRD_PARTY_CALL_ERROR.getDefaultMessage()));
		}
		// 生成14位的SKU编号,用于缓存里面
		String state = String.valueOf(new SnowFlake(SnowFlake.SHORT_STR_CODE).nextId());
		JSONObject jsonStr = new JSONObject();
		jsonStr.put("scene", CacheConstants.AUTH_SCENE);
		jsonStr.put("backUrl", redirectUri);
		jsonStr.put("siteId", siteId);
		cacheProvider.setCache(MEMBER, state, jsonStr.toJSONString());
		StringBuilder builder = new StringBuilder();
		builder.append(Constants.AUTHORIZE_URL).append("?")
		.append("client_id=").append(weibo.getAppId())
		.append("&redirect_uri=").append(redirectUri)
		.append("&state=").append(state)
		.append("&response_type=code");
		return new ResponseInfo(builder.toString());
	}
	
	
	/**
	 * 微博回调事件
	* @Title: authCallBack 
	* @param request 请求
	* @param response 返回
	* @throws GlobalException
	* @throws IOException
	 */
	@GetMapping(value = "/authCallBack")
	public String authCallBack(HttpServletRequest request, HttpServletResponse response, Model model) 
			throws Exception {
		//得到code
		String code = request.getParameter("code");
		if (StringUtils.isEmpty(code)) {
			return FrontUtils.getSysPagePath(request, TPLDIR_WEIBO, "weibo_auth_quit");
		}
		//得到缓存key
		String state = request.getParameter("state");
		Object reqState = cacheProvider.getCache(MEMBER, state);
		JSONObject obj = JSONObject.parseObject(reqState.toString());
		//得到场景值
		String scene = obj.getString("scene");
		String redirectUri = obj.getString("backUrl");
		Integer siteId = obj.getInteger("siteId");
		cacheProvider.clearCache(MEMBER, state);
		if (scene.equals(CacheConstants.AUTH_SCENE)) {
			//根据站点去查询应用
			WeiboAppConfig weibo = weiboAppConfigService.getBySiteId(siteId);
			WeiboTokenVO vo = token(code,weibo.getAppId(),weibo.getAppSecret(),redirectUri);
			//保存微博账户信息
			ResponseInfo info = weiboInfoService.saveWeiboInfo(vo, siteId);
			model.addAttribute("info", info);
			return FrontUtils.getSysPagePath(request, TPLDIR_WEIBO, "weibo_auth_success");
		} else if (scene.equals(CacheConstants.LOGIN_SCENE)) {
			// 获取授权过的authToken
			SysThird third = thirdService.getCode(SysThird.WEIBO);
			WeiboTokenVO vo = token(code,third.getAppId(),third.getAppKey(),redirectUri);
			ThirdPartyResultDTO dto = thirdPartyUserService.loginForSina(vo, siteId);
			model.addAttribute("info", dto);
			return FrontUtils.getTplPath(request, null, "callback");
		} else if (scene.equals(CacheConstants.LOGIN_SCENE_MOBILE)) {
			// 获取授权过的authToken
			SysThird third = thirdService.getCode(SysThird.WEIBO);
			WeiboTokenVO vo = token(code,third.getAppId(),third.getAppKey(),redirectUri);
			ThirdPartyResultDTO dto = thirdPartyUserService.loginForSina(vo, siteId);
			String thirdId = String.valueOf(new SnowFlake(SnowFlake.SHORT_STR_CODE).nextId());
			cacheProvider.setCache(MEMBER, thirdId, dto);
			CmsSite site = cmsSiteService.findById(siteId);
			//得到站点域名
			String uri = site.getDomain();
			StringBuilder builder = new StringBuilder();
			String url = builder.append("redirect:http://")
			.append(uri)
			.append("/h5center/index.html#/pages/login/thirdParty?thirdId=")
			.append(thirdId).toString();
			LOGGER.info(url);
			return url;
		}
		return "";
	} 
	
	/**
	 * 取消授权微博回调事件
	* @Title: authCallBack 
	* @param request 请求
	* @param response 返回
	* @throws GlobalException
	* @throws IOException
	 */
	@GetMapping(value = "/cancel")
	public String cancel(HttpServletRequest request, HttpServletResponse response) throws GlobalException, IOException {
		return FrontUtils.getSysPagePath(request, TPLDIR_WEIBO, "weibo_auth_quit");
	} 
	
	/**
	 * 获取token
	* @Title: token 
	* @param appid 应用ID
	* @param secret 秘钥
	* @return
	 */
	public WeiboTokenVO token(String code, String appid, String secret, String redirectUri) {
		Map<String, String> params = new HashMap<String, String>(16);
		params.put("client_id", appid);
		params.put("client_secret", secret);
		params.put("grant_type", "authorization_code");
		params.put("code", code);
		params.put("redirect_uri", redirectUri);
		WeiboTokenVO vo = HttpUtil.postJsonBean(Constants.ACCESS_TOKEN_URL, params, null, WeiboTokenVO.class);
		LOGGER.info(vo.toString());
		return vo;
	}
}
