/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.sso.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.CoreUserExt;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.constants.ContentSecurityConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SSOExceptionInfo;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.DesUtil;
import com.jeecms.common.web.cache.CacheProvider;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.sso.constants.SsoContants;
import com.jeecms.sso.dto.BaseDto;
import com.jeecms.sso.dto.SsoLoginDto;
import com.jeecms.sso.dto.SyncRequestDeleteDto;
import com.jeecms.sso.dto.SyncRequestRegisterDto;
import com.jeecms.sso.dto.SyncResponseRegisterVo;
import com.jeecms.sso.dto.request.SyncDeleteUser;
import com.jeecms.sso.dto.request.SyncRequestUser;
import com.jeecms.sso.dto.request.SyncRequestUserDto;
import com.jeecms.sso.dto.response.SyncResponseBaseVo;
import com.jeecms.sso.dto.response.SyncResponseUserVo;
import com.jeecms.sso.dto.response.SyncResponseVo;
import com.jeecms.sso.service.CmsSsoService;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.system.service.CmsOrgService;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.GlobalConfigService;

/**
 * 实现类
 * 
 * @author: ljw
 * @date: 2019年10月26日 上午10:16:48
 */
@Service
@Transactional(rollbackFor = { Exception.class })
public class CmsSsoServiceImpl implements CmsSsoService {

	private static final Logger log = LoggerFactory.getLogger(CmsSsoServiceImpl.class);

	@Value("${sso.syncuser.url}")
	private String syncuserUrl;
	@Value("${sso.app.url}")
	private String appUrl;
	// SSO校验地址
	@Value("${sso.logincheck.url}")
	private String ssoLoginCheckUrl;
	// SSO登出地址
	@Value("${sso.logout.url}")
	private String ssoLogoutUrl;
	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private GlobalConfigService globalConfigService;
	@Autowired
	private CacheProvider cacheProvider;
	@Autowired
	private CmsOrgService cmsOrgService;
	@Autowired
	private CmsSiteService cmsSiteService;
	
	@Override
	public ResponseInfo saveSsoConfig(SsoLoginDto dto) throws GlobalException {
		//使用缓存实现立即刷新的效果
		cacheProvider.setCache(WebConstants.SSO_OPEN, WebConstants.SSO_OPEN,
				dto.getOpenSso() ? GlobalConfigAttr.TRUE_STRING : GlobalConfigAttr.FALSE_STRING);
		GlobalConfig config = globalConfigService.get();
		Map<String, String> attrMap = config.getAttrs();
		attrMap.put(GlobalConfigAttr.SSO_LOGIN_OPEN, dto.getOpenSso() 
				? GlobalConfigAttr.TRUE_STRING : GlobalConfigAttr.FALSE_STRING);
		if (dto.getOpenSso()) {
			attrMap.put(GlobalConfigAttr.SSO_LOGIN_APPID, dto.getAppId());
			attrMap.put(GlobalConfigAttr.SSO_LOGIN_APPSECRET, dto.getAppSecret());
			log.info("开始SSO异步任务---" + Calendar.getInstance().getTime());
			// 异步任务
			sync(dto.getAppId(), dto.getAppSecret());
		} else {
			//关闭单点登录需要删除来源于其他应用的用户
			List<CoreUser> users = coreUserService.findAll(true);
			List<CoreUser> list = users.stream()
					.filter(x -> StringUtils.isNotBlank(x.getAppId())).collect(Collectors.toList());
			coreUserService.delete(list);
			//请求SSO认证中心，将应用改成未接入
			BaseDto base = new BaseDto(dto.getAppId(), dto.getAppSecret());
			HttpUtil.postJsonBeanForJSON(appUrl.replace(" ", ""), 
					null, JSONObject.toJSONString(base),
					SyncResponseVo.class);
		}
		globalConfigService.updateAll(config);
		return new ResponseInfo();
	}

	@Override
	@Async("asyncServiceExecutor")
	public ResponseInfo sync(String appId, String appSecret) throws GlobalException {
		// 步骤1将数据同步至SSO
		List<CoreUser> users = coreUserService.findAll(true);
		// 过滤APPID为空的数据,这个数据就是原始数据
		List<CoreUser> list = users.stream()
				.filter(x -> StringUtils.isBlank(x.getAppId()))
				//审核通过
				.filter(x -> CoreUser.AUDIT_USER_STATUS_PASS.equals(x.getCheckStatus()))
				.collect(Collectors.toList());
		List<SyncRequestUser> userlist = new ArrayList<SyncRequestUser>();
		for (CoreUser coreUser : list) {
			SyncRequestUser requestUser = new SyncRequestUser(coreUser);
			userlist.add(requestUser);
		}
		SyncRequestUserDto dto = new SyncRequestUserDto();
		dto.setAppId(appId);
		dto.setAppSecret(appSecret);
		dto.setUsers(userlist);
		SyncResponseVo response = HttpUtil.postJsonBeanForJSON(syncuserUrl.replace(" ", ""), 
				null, JSONObject.toJSONString(dto),
				SyncResponseVo.class);
		if (response != null && SyncResponseBaseVo.SUCCESS_CODE.equals(response.getCode())) {
			return new ResponseInfo();
		} else {
			if (response == null) {
				throw new GlobalException(RPCErrorCodeEnum.SYNC_SSO_ERROR);
			}
			throw new GlobalException(
					new SSOExceptionInfo(String.valueOf(response.getCode()), 
							response.getMessage(), false));
		}
	}

	/** 得到其他应用的数据，将关联关系返回给SSO **/
	@Override
	public ResponseInfo saveUserNoAuth(SyncRequestRegisterDto dto) throws GlobalException {
		List<CoreUser> list = new ArrayList<CoreUser>(16);
		List<SyncResponseRegisterVo> dtoList = new ArrayList<SyncResponseRegisterVo>(16);
		List<String> usernames = coreUserService.findAll(true).stream().map(CoreUser::getUsername)
				.collect(Collectors.toList());
		for (SyncRequestUser responseUser : dto.getUsers()) {
			CoreUser user = new CoreUser();
			user.setUsername(responseUser.getUsername());
			// 判断用户名是否存在，存在就同步失败
			Boolean flag = usernames.contains(responseUser.getUsername());
			if (flag) {
				SyncResponseRegisterVo vo = new SyncResponseRegisterVo(responseUser.getUserId(), false);
				dtoList.add(vo);
				continue;
			}
			user.setPassword(DesUtil.decrypt(responseUser.getPassword(), ContentSecurityConstants.DES_KEY,
					ContentSecurityConstants.DES_IV));
			user.setSalt(responseUser.getSalt());
			//暂时不启用手机以及邮箱
			//user.setEmail(responseUser.getUserEmail());
			//user.setTelephone(responseUser.getUserPhone());
			user.setAppId(dto.getAppId());
			// 根据用户类型分别用户，会员
			if (responseUser.getUserType().equals(SsoContants.SSO_USER_TYPE_1)) {
				user.setAdmin(true);
				// 默认组织为1金磊科技
				user.setOrgId(1);
				user.setOrg(cmsOrgService.findById(1));
			} else {
				user.setAdmin(false);
				//默认来源站点为金磊科技
				user.setSourceSiteId(1);
				user.setSourceSite(cmsSiteService.findById(1));
			}
			CoreUserExt ext = new CoreUserExt();
			ext.setGender(CoreUserExt.GENDER_SECRET);
			user.addExt(ext);
			user.setLoginErrorCount(0);
			user.setLoginCount(0);
			user.setLastLoginTime(new Date());
			user.setCheckStatus(CoreUser.AUDIT_USER_STATUS_PASS);
			user.setIsResetPassword(true);
			user.setPassMsgHasSend(false);
			user.setEnabled(true);
			list.add(user);
		}
		// 保存其他应用在客户端的数据
		coreUserService.saveAll(list);
		coreUserService.flush();
		return new ResponseInfo(dtoList);
	}

	@Override
	public SyncResponseUserVo userVaild(String authToken) throws GlobalException {
		GlobalConfigAttr configAttr = globalConfigService.get().getConfigAttr();
		Map<String, String> params = new HashMap<>(16);
		params.put(SsoContants.SSO_AUTHTOKEN, authToken);
		params.put(SsoContants.SSO_APPID, configAttr.getSsoLoginAppId());
		SyncResponseVo response = HttpUtil.getJsonBean(ssoLoginCheckUrl.replace(" ", ""), 
				params, SyncResponseVo.class);
		if (response != null 
				&& SyncResponseBaseVo.SUCCESS_CODE.equals(response.getCode())
				&& response.getData() != null) {
			String data = response.getData().toString();
			SyncResponseUserVo vo = JSONObject.parseObject(data, SyncResponseUserVo.class);
			return vo;
		} else {
			return null;
		}
	}

	@Override
	public void logout(String authToken) throws GlobalException {
		Map<String, String> params = new HashMap<>(16);
		params.put(SsoContants.SSO_AUTHTOKEN, authToken);
		HttpUtil.get(ssoLogoutUrl.replace(" ", ""), params);
	}
	
	@Override
	public ResponseInfo delete(SyncRequestDeleteDto dto) throws GlobalException {
		// 得到全部用户信息
		List<CoreUser> users = coreUserService.findAll(true);
		if (!users.isEmpty() && dto.getUsers() != null && !dto.getUsers().isEmpty()) {
			//得到要删除的用户
			List<String> stringList = dto.getUsers().stream()
					.map(SyncDeleteUser::getUsername).collect(Collectors.toList());
			users = users.stream().filter(x -> stringList.contains(x.getUsername()))
					.collect(Collectors.toList());
			coreUserService.delete(users);
		}
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo update(SyncRequestUser dto) throws GlobalException {
		// 得到全部用户信息
		List<CoreUser> list = coreUserService.findAll(true);
		if (!list.isEmpty() && dto != null) {
			Optional<CoreUser> optional = list.stream().filter(x -> dto.getAppId().equals(x.getAppId()))
					.filter(x -> x.getUsername().equals(dto.getUsername())).findFirst();
			if (optional.isPresent()) {
				// 只修改以下字段
				CoreUser user = optional.get();
				user.setPassword(DesUtil.decrypt(dto.getPassword(), ContentSecurityConstants.DES_KEY,
						ContentSecurityConstants.DES_IV));
				user.setSalt(dto.getSalt());
				//暂时只修改密码
				//user.setEmail(dto.getUserEmail());
				//user.setTelephone(dto.getUserPhone());
				coreUserService.update(user);
				coreUserService.flush();
			}
		}
		return new ResponseInfo();
	}

}