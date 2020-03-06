/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *            仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller.member;

import static com.jeecms.common.constants.SysConstants.TPLDIR_WEIBO;
import static com.jeecms.common.exception.error.RPCErrorCodeEnum.THIRD_PARTY_INFO_UNCONFIGURATION;
import static com.jeecms.common.web.cache.CacheConstants.MEMBER;
import static com.jeecms.member.domain.dto.MemberConstants.THIRD_SCOPE_ALL;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.CoreUserExt;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.auth.service.LoginService;
import com.jeecms.auth.service.PasswdService;
import com.jeecms.common.base.domain.RequestLoginTarget;
import com.jeecms.common.base.domain.ThirdPartyResultDTO;
import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.OtherErrorCodeEnum;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.security.Digests;
import com.jeecms.common.util.DesUtil;
import com.jeecms.common.util.SnowFlake;
import com.jeecms.common.util.XssUtil;
import com.jeecms.common.web.cache.CacheConstants;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.weibo.Constants;
import com.jeecms.member.domain.SysUserThird;
import com.jeecms.member.domain.dto.PcLoginDto;
import com.jeecms.member.service.SysUserThirdService;
import com.jeecms.member.service.ThirdPartyUserService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysThird;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.SysThirdService;
import com.jeecms.util.FrontUtils;
import com.jeecms.util.SystemContextUtils;

/**
 * 第三方登录
 * @author: ljw
 * @date: 2019年7月16日 下午4:00:08
 */
@RequestMapping(value = "/thirdParty")
@Controller
public class ThirdPartyLoginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyLoginController.class);

	/**
	 * 新浪微博登录
	* @Title: sina 
	* @param response 响应
	* @param request 请求
	* @throws Exception 异常
	 */
	@GetMapping(value = "/sina/pc")
	public void sina(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		//获得站点
		CmsSite site = SystemContextUtils.getSite(request);
		// 生成14位的SKU编号,用于缓存里面
		String state = String.valueOf(new SnowFlake(SnowFlake.SHORT_STR_CODE).nextId());
		if (StringUtils.isBlank(state)) {
			throw new GlobalException(OtherErrorCodeEnum.GENERATE_CODE_ERROR);
		}
		SysThird thirdInfo = thirdService.getCode(SysThird.WEIBO);
		if (thirdInfo == null || !thirdInfo.getIsEnable()) {
			throw new GlobalException(THIRD_PARTY_INFO_UNCONFIGURATION);
		}
		String appId = thirdInfo.getAppId();
		//得到站点域名
		String uri = site.getDomain();
		uri = XssUtil.cleanXSS(uri);
		//回调地址
		String redirectUri = uri + "/weibo/authCallBack";
		JSONObject jsonStr = new JSONObject();
		jsonStr.put("scene", CacheConstants.LOGIN_SCENE);
		jsonStr.put("backUrl", redirectUri);
		jsonStr.put("siteId", site.getId());
		cacheProvider.setCache(MEMBER, state, jsonStr.toJSONString());
		appId = XssUtil.cleanXSS(appId);
		state = XssUtil.cleanXSS(state);
		StringBuilder builder = new StringBuilder();
		builder.append(Constants.AUTHORIZE_URL).append("?")
		.append("client_id=").append(appId)
		.append("&redirect_uri=").append(redirectUri)
		.append("&state=").append(state)
		.append("&response_type=code");
		response.sendRedirect(builder.toString());
	}
	
	/**
	 * qq登录
	* @Title: qq 
	* @param backUrl 回调地址
	* @param request 请求
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/qq/pc")
	public void qq(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获得站点
		CmsSite site = SystemContextUtils.getSite(request);
		// 生成14位的SKU编号
		String state = String.valueOf(new SnowFlake(SnowFlake.SHORT_STR_CODE).nextId());
		if (StringUtils.isBlank(state)) {
			throw new GlobalException(OtherErrorCodeEnum.GENERATE_CODE_ERROR);
		}
		SysThird thirdInfo = thirdService.getCode(SysThird.QQ);
		if (thirdInfo == null || !thirdInfo.getIsEnable()) {
			throw new GlobalException(THIRD_PARTY_INFO_UNCONFIGURATION);
		}
		String appId = thirdInfo.getAppId();
		//得到站点域名
		String uri = site.getDomain();
		uri = XssUtil.cleanXSS(uri);
		//回调地址
		String redirectUri = uri + "/thirdParty/qq/authCallBack";
		redirectUri = XssUtil.cleanXSS(redirectUri);
		JSONObject jsonStr = new JSONObject();
		jsonStr.put("backUrl", redirectUri);
		jsonStr.put("siteId", site.getId());
		cacheProvider.setCache(MEMBER, state, jsonStr.toJSONString());
		appId = XssUtil.cleanXSS(appId);
		state = XssUtil.cleanXSS(state);
		StringBuilder builder = new StringBuilder();
		builder.append(Constants.QQ_CODE_URL).append("?")
		.append("response_type=code")
		.append("&client_id=").append(appId)
		.append("&redirect_uri=").append(redirectUri)
		.append("&state=").append(state)
		.append("&scope=").append(THIRD_SCOPE_ALL);
		response.sendRedirect(builder.toString());
	}

	/**
	 * 微信登录(PC端扫码)
	* @Title: wechatPc 
	* @param backUrl 回调地址
	* @param request 请求
	* @param response 响应
	* @throws GlobalException 异常
	* @throws ServletException 异常
	* @throws IOException 异常
	 */
	@GetMapping(value = "/wechat/pc")
	public void wechatPc(HttpServletRequest request, HttpServletResponse response)
			throws GlobalException, ServletException, IOException {
		//获得站点
		CmsSite site = SystemContextUtils.getSite(request);
		// 生成14位的SKU编号
		String state = String.valueOf(new SnowFlake(SnowFlake.SHORT_STR_CODE).nextId());
		if (StringUtils.isBlank(state)) {
			throw new GlobalException(OtherErrorCodeEnum.GENERATE_CODE_ERROR);
		}
		SysThird thirdInfo = thirdService.getCode(SysThird.WECHAT);
		if (thirdInfo == null || !thirdInfo.getIsEnable()) {
			throw new GlobalException(THIRD_PARTY_INFO_UNCONFIGURATION);
		}
		//得到协议 + 站点域名
		String uri = site.getProtocol() + site.getDomain();
		uri = XssUtil.cleanXSS(uri);
		//回调地址
		String redirectUri = uri + "/thirdParty/wechat/authCallBack";
		redirectUri = XssUtil.cleanXSS(redirectUri);
		String appId = thirdInfo.getAppId();
		JSONObject jsonStr = new JSONObject();
		jsonStr.put("backUrl", redirectUri);
		jsonStr.put("siteId", site.getId());
		cacheProvider.setCache(MEMBER, state, jsonStr.toJSONString());
		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		StringBuilder builder = new StringBuilder();
		builder.append(Constants.WECHAT_PC_CODE_URL).append("?")
		.append("appid=").append(appId)
		//转码
		.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"))
		.append("&response_type=code")
		.append("&scope=snsapi_login")
		.append("&state=").append(state);
		response.sendRedirect(builder.toString());
	}

	/**
	 * 微信回调（PC端回调）
	* @Title: wechatBackPC 
	* @param code 用户Code
	* @param state 传值
	* @param request 请求
	* @param model 模型
	* @throws Exception
	 */
	@GetMapping(value = "/wechat/authCallBack")
	public String wechatBackPC(String code, String state, HttpServletRequest request,
			Model model) throws Exception {
		LOGGER.info(java.text.Normalizer.normalize(String.format("code:%s --> state%s", code, state),
				java.text.Normalizer.Form.NFKD));
		if (StringUtils.isEmpty(code)) {
			return FrontUtils.getSysPagePath(request, TPLDIR_WEIBO, "weibo_auth_quit");
		}
		//得到缓存key
		Object reqState = cacheProvider.getCache(MEMBER, state);
		JSONObject obj = JSONObject.parseObject(reqState.toString());
		//得到回调函数
		String redirectUri = obj.getString("backUrl");
		//得到站点ID
		Integer siteId = obj.getInteger("siteId");
		ThirdPartyResultDTO dto = thirdPartyUserService.loginForWeChatPc(code, redirectUri, siteId);
		model.addAttribute("info", dto);
		cacheProvider.clearCache(MEMBER, state);
		return FrontUtils.getTplPath(request, null, "callback");
	}

	/**
	 * qq回调
	* @Title: qqBack 
	* @param code 用户CODE
	* @param state 参数
	* @param userCancel WAP网站用户在授权过程中取消授权则会带上该参数,该参数值非0.
	* @param request 请求
	* @param model 模型
	* @throws Exception 异常
	 */
	@GetMapping(value = "/qq/authCallBack")
	public String qqBack(String code, String state, String userCancel, HttpServletRequest request,
			Model model) throws Exception {
		LOGGER.info(java.text.Normalizer.normalize(String.format("code:%s --> state%s", code, state),
				java.text.Normalizer.Form.NFKD));
		if (StringUtils.isEmpty(code)) {
			return FrontUtils.getSysPagePath(request, TPLDIR_WEIBO, "weibo_auth_quit");
		}
		//得到缓存key
		Object reqState = cacheProvider.getCache(MEMBER, state);
		JSONObject obj = JSONObject.parseObject(reqState.toString());
		//得到回调函数
		String redirectUri = obj.getString("backUrl");
		// 得到站点ID
		Integer siteId = obj.getInteger("siteId");
		ThirdPartyResultDTO dto = thirdPartyUserService.loginForQQ(code, redirectUri, siteId);
		model.addAttribute("info", dto);
		// 得到场景
		String scene = obj.getString("scene");
		if (StringUtils.isNotBlank(scene) && scene.equals(CacheConstants.QQ_LOGIN_SCENE_MOBILE)) {
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
		cacheProvider.clearCache(MEMBER, state);
		return FrontUtils.getTplPath(request, null, "callback");
	}

	/**
	 * 绑定会员或直接登录
	* @Title: bindMember 
	* @param request 请求
	* @param dto 传输
	* @return
	* @throws GlobalException
	 */
	@PostMapping("/bind")
	@ResponseBody
	private ResponseInfo bindMember(HttpServletRequest request, @RequestBody PcLoginDto dto) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		CoreUser user = new CoreUser();
		CoreUserExt ext = new CoreUserExt();
		ext.setRealname(dto.getUsername());
		user.setUsername(dto.getUsername());
		user.setSourceSiteId(siteId);
		user.setEnabled(true);
		// 默认登录失败次数为0
		user.setLoginErrorCount(0);
		user.setLoginCount(0);
		user.addExt(ext);
		// 设置为会员
		user.setAdmin(false);
		// 默认审核通过
		user.setCheckStatus(CoreUser.AUDIT_USER_STATUS_PASS);
		user.setIsResetPassword(true);
		user.setPassMsgHasSend(false);
		//判断登录方式
		if (PcLoginDto.TYPE_LOGIN.equals(dto.getLoginWay())) {
			Boolean validName = memberService.validName(dto.getUsername());
			if (!validName) {
				return new ResponseInfo(UserErrorCodeEnum.USERNAME_ALREADY_EXIST.getCode(),
						UserErrorCodeEnum.USERNAME_ALREADY_EXIST.getDefaultMessage(), false);
			}
			//如果是直接登录,则默认创建会员，密码随机
			user.setPassword(String.valueOf(new SnowFlake(SnowFlake.SHORT_STR_CODE).nextId()));
			// 密码加密
			byte[] salt = Digests.generateSaltFix();
			user.setSalt(Digests.getSaltStr(salt));
			user.setThird(true);
			//新建会员用户
			user = memberService.save(user);
			this.bind(dto, user.getId());
		} else {
			//绑定登录
			// 解密密码
			String password = DesUtil.decrypt(dto.getPsw(), ContentSecurityConstants.DES_KEY,
					ContentSecurityConstants.DES_IV);
			CoreUser member = memberService.findByUsername(dto.getUsername());
			//用户不存在
			if (member == null) {
				return new ResponseInfo(RPCErrorCodeEnum.USER_INFO_NOT_OBTAINED.getCode(),
						RPCErrorCodeEnum.USER_INFO_NOT_OBTAINED.getDefaultMessage(), false);
			}
			// 判断输入的旧密码是否匹配
			if (!passwdService.isPasswordValid(password, member.getSalt(), member.getPassword())) {
				return new ResponseInfo(UserErrorCodeEnum.ACCOUNT_CREDENTIAL_ERROR.getCode(),
						UserErrorCodeEnum.ACCOUNT_CREDENTIAL_ERROR.getDefaultMessage(), false);
			}
			this.bind(dto, member.getId());
		}
		Map<String, Object> authToken = loginService
				.login(RequestLoginTarget.member, dto.getUsername(), null);
		//默认绑定
		JSONObject object = new JSONObject();
		object.putAll(authToken);
		//跳转地址
		object.put("redirectUrl", "");
		if (siteId != null) {
			CmsSite site = cmsSiteService.findById(siteId);
			String redirectUrl = site.getMemberRedirectUrl();
			object.put("redirectUrl", redirectUrl);
		}
		return new ResponseInfo(object);
	}
	
	/**
	 * 绑定第三方用户
	* @Title: bind 
	* @param dto 传输
	* @param memberId 会员ID
	* @throws GlobalException 异常
	 */
	public void bind(PcLoginDto dto, Integer memberId) throws GlobalException {
		//查询第三方配置信息
		SysThird thirdInfo = thirdService.getCode(dto.getLoginType());
		SysUserThird third = new SysUserThird();
		third.setAppId(thirdInfo.getAppId());
		third.setThirdId(dto.getThirdId());
		third.setThirdUsername(dto.getNickname());
		third.setMemberId(memberId);
		third.setUsername(dto.getUsername());
		third.setThirdTypeCode(dto.getLoginType());
		sysUserThirdService.save(third);
	}
	
	/**
	 * 新浪微博手机登录
	* @Title: sina 
	* @param response 响应
	* @param request 请求
	* @throws Exception 异常
	 */
	@GetMapping(value = "/sina/mobile")
	public void sinaMobile(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		//获得站点
		CmsSite site = SystemContextUtils.getSite(request);
		// 生成14位的SKU编号,用于缓存里面
		String state = String.valueOf(new SnowFlake(SnowFlake.SHORT_STR_CODE).nextId());
		if (StringUtils.isBlank(state)) {
			throw new GlobalException(OtherErrorCodeEnum.GENERATE_CODE_ERROR);
		}
		SysThird thirdInfo = thirdService.getCode(SysThird.WEIBO);
		if (thirdInfo == null || !thirdInfo.getIsEnable()) {
			throw new GlobalException(THIRD_PARTY_INFO_UNCONFIGURATION);
		}
		String appId = thirdInfo.getAppId();
		//得到站点域名
		String uri = site.getDomain();
		uri = XssUtil.cleanXSS(uri);
		//回调地址
		String redirectUri = uri + "/weibo/authCallBack";
		JSONObject jsonStr = new JSONObject();
		jsonStr.put("scene", CacheConstants.LOGIN_SCENE_MOBILE);
		jsonStr.put("backUrl", redirectUri);
		jsonStr.put("siteId", site.getId());
		cacheProvider.setCache(MEMBER, state, jsonStr.toJSONString());
		appId = XssUtil.cleanXSS(appId);
		state = XssUtil.cleanXSS(state);
		StringBuilder builder = new StringBuilder();
		builder.append(Constants.AUTHORIZE_URL).append("?")
		.append("client_id=").append(appId)
		.append("&redirect_uri=").append(redirectUri)
		.append("&state=").append(state)
		.append("&response_type=code");
		response.sendRedirect(builder.toString());
	}
	
	/**
	 * qq手机登录
	* @Title: qq 
	* @param backUrl 回调地址
	* @param request 请求
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/qq/mobile")
	public void qqMobile(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获得站点
		CmsSite site = SystemContextUtils.getSite(request);
		// 生成14位的SKU编号
		String state = String.valueOf(new SnowFlake(SnowFlake.SHORT_STR_CODE).nextId());
		if (StringUtils.isBlank(state)) {
			throw new GlobalException(OtherErrorCodeEnum.GENERATE_CODE_ERROR);
		}
		SysThird thirdInfo = thirdService.getCode(SysThird.QQ);
		if (thirdInfo == null || !thirdInfo.getIsEnable()) {
			throw new GlobalException(THIRD_PARTY_INFO_UNCONFIGURATION);
		}
		String appId = thirdInfo.getAppId();
		//得到站点域名
		String uri = site.getDomain();
		uri = XssUtil.cleanXSS(uri);
		//回调地址
		String redirectUri = uri + "/thirdParty/qq/authCallBack";
		redirectUri = XssUtil.cleanXSS(redirectUri);
		JSONObject jsonStr = new JSONObject();
		jsonStr.put("scene", CacheConstants.QQ_LOGIN_SCENE_MOBILE);
		jsonStr.put("backUrl", redirectUri);
		jsonStr.put("siteId", site.getId());
		cacheProvider.setCache(MEMBER, state, jsonStr.toJSONString());
		appId = XssUtil.cleanXSS(appId);
		state = XssUtil.cleanXSS(state);
		StringBuilder builder = new StringBuilder();
		builder.append(Constants.QQ_CODE_URL).append("?")
		.append("response_type=code")
		.append("&client_id=").append(appId)
		.append("&redirect_uri=").append(redirectUri)
		.append("&state=").append(state)
		.append("&scope=").append(THIRD_SCOPE_ALL);
		response.sendRedirect(builder.toString());
	}
	
	/**
	 * 得到数据信息
	* @Title: getInfo 
	* @throws Exception 异常
	 */
	@GetMapping(value = "/info")
	@ResponseBody
	public ThirdPartyResultDTO getInfo(String thidId) throws Exception {
		ThirdPartyResultDTO dto = new ThirdPartyResultDTO();
		Object reqState = cacheProvider.getCache(MEMBER, thidId);
		if (reqState != null) {
			dto = (ThirdPartyResultDTO) reqState;
			cacheProvider.clearCache(MEMBER, thidId);
		}
		return dto;
	}
	
	
	@Autowired
	private SysThirdService thirdService;
	@Autowired
	private CacheProvider cacheProvider;
	@Autowired
	private CoreUserService memberService;
	@Autowired
	private ThirdPartyUserService thirdPartyUserService;
	@Autowired
	private SysUserThirdService sysUserThirdService;
	@Autowired
	private PasswdService passwdService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private CmsSiteService cmsSiteService;
}
