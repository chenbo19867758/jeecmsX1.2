/**
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.wechat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.wechat.bean.request.mp.message.MessageRequest;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.WechatFans;
import com.jeecms.wechat.domain.WechatFansExt;
import com.jeecms.wechat.domain.dto.WechatFansDto;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatFansService;

/**
 * 粉丝列表控制器
 * 
 * @author: ljw
 * @version: 1.0
 * @date 2018-08-02
 */
@RequestMapping(value = "/wechatfans")
@RestController
public class WechatFansController extends BaseController<WechatFans, Integer> {

	@Autowired
	private WechatFansService wechatFansService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;

	/**
	 * 粉丝列表分页
	* @Title: page 
	* @param request 请求
	* @param pageable 分页对象
	* @param nickname 昵称
	* @return ResponseInfo 返回对象
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/page")
	@MoreSerializeField({ @SerializeField(clazz = WechatFans.class, includes = { "id", "username", "subscribeTime",
			"subscribeTimes", "headimgurl", "nickname", "memberId", "openid", "tagidList", "sex", 
			"wechatName", "appId", "fansExt" }),
		@SerializeField(clazz = WechatFansExt.class, includes = { "commentCount", "topCommentCount",
		"mesCount" }),})
	public ResponseInfo page(HttpServletRequest request,
			@PageableDefault(sort = "subscribeTime", direction = Direction.DESC) Pageable pageable, 
			String nickname, String appId, String tagid) throws GlobalException {
		//APPID为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(),false);
		}
		return wechatFansService.getFansPage(Arrays.asList(appId), pageable, nickname, tagid, null);
	}
	
	/**
	 * 粉丝汇总
	* @Title: collect 
	* @param request 请求
	* @param pageable 分页对象
	* @param nickname 昵称
	* @return ResponseInfo 返回对象
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/collect")
	@MoreSerializeField({ 
		@SerializeField(clazz = WechatFans.class, includes = { "id", "username", "subscribeTime",
				"subscribeTimes",
			"headimgurl", "nickname", "memberId", "openid", "tagidList", "wechatName", "appId"}), })
	public ResponseInfo collect(HttpServletRequest request,
			@PageableDefault(sort = "subscribeTime", direction = Direction.DESC) Pageable pageable, 
			String nickname, String appId, String tagid) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		List<String> appids = new ArrayList<String>(10);
		if (StringUtils.isNotBlank(appId)) {
			appids.add(appId);
		} else {
			Map<String, String[]> param = new HashMap<String, String[]>(10);
			param.put("EQ_siteId_Integer", new String[] { siteId.toString() });
			List<AbstractWeChatInfo> abs = abstractWeChatInfoService.getList(param, null, true);
			// 站点没有APPID，直接返回
			if (abs.isEmpty()) {
				return new ResponseInfo();
			}
			for (AbstractWeChatInfo ab : abs) {
				appids.add(ab.getAppId());
			} 
		}
		return wechatFansService.getFansPage(appids, pageable, nickname, tagid, null);
	}

	/**
	 * 同步全部粉丝
	 * @param request 请求
	 * @param response 响应
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping(value = "/sync")
	public ResponseInfo sync(HttpServletRequest request, HttpServletResponse response) throws GlobalException {
		String appId = request.getParameter("appId");
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, null,appId);
		return wechatFansService.sync(appId);
	}

	/**
	 * 同步多个粉丝
	* @Title: syncFans 
	* @param request 请求
	* @param dto 传送DTO
	* @return ResponseInfo 返回对象
	* @throws GlobalException 异常
	 */
	@PostMapping(value = "/syncFans")
	public ResponseInfo syncFans(HttpServletRequest request, @RequestBody WechatFansDto dto) 
			throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, null,dto.getAppId());
		return wechatFansService.syncFans(dto.getAppId(), dto.getIds());
	}
	
	/**
	 * 同步黑名单
	* @Title: syncblack 
	* @param request 请求
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/syncblack")
	public ResponseInfo syncblack(HttpServletRequest request)
			throws GlobalException {
		String appId = request.getParameter("appId");
		//APPID为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(),false);
		}
		return wechatFansService.syncblack(appId);
	}
	
	/**
	 * 黑名单
	* @Title: blackList 
	* @param request 请求
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/black")
	@MoreSerializeField({ @SerializeField(clazz = WechatFans.class, includes = { "id", "username", "subscribeTime",
			"subscribeTimes", "headimgurl", "nickname", "memberId", "openid", 
			"tagidList", "wechatName", "appId"}), })
	public ResponseInfo blackList(HttpServletRequest request, Pageable pageable)
			throws GlobalException {
		String appId = request.getParameter("appId");
		//APPID为空直接返回
		if (!StringUtils.isNotBlank(appId)) {
			return new ResponseInfo(SettingErrorCodeEnum.APP_ID_NOT_NULL.getCode(),
					SettingErrorCodeEnum.APP_ID_NOT_NULL.getDefaultMessage(),false);
		}
		String nickname = request.getParameter("nickname");
		return wechatFansService.blackList(appId, nickname, pageable);
	}

	/**
	 * 拉黑粉丝
	* @Title: black 
	* @param request 请求
	* @param dto 传输
	* @param result 检验
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	@PostMapping(value = "/black")
	public ResponseInfo black(HttpServletRequest request, @RequestBody @Valid WechatFansDto dto, 
			BindingResult result)
			throws GlobalException {
		super.validateBindingResult(result);
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, null, dto.getAppId());
		return wechatFansService.black(dto.getAppId(), dto.getIds());
	}

	/**
	 * 取消拉黑粉丝
	* @Title: cancelblack 
	* @param request 请求
	* @param dto 传输
	* @param result 检验
	* @return ResponseInfo 返回对象
	* @throws GlobalException 异常
	 */
	@PostMapping(value = "/cancelblack")
	public ResponseInfo cancelblack(HttpServletRequest request, @RequestBody @Valid WechatFansDto dto,
			BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		return wechatFansService.cancelblack(dto.getAppId(), dto.getIds());
	}

	/**
	 * 获取粉丝详情
	 * @Title: get 
	 * @param openId 粉丝OPENID
     * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	@GetMapping
	@MoreSerializeField({
			@SerializeField(clazz = WechatFans.class, includes = { "id", "username", "subscribeTime",
				"subscribeTimes", "headimgurl", "nickname", "openid", "fansExt", "tagidList", "sex",}),
			@SerializeField(clazz = WechatFansExt.class, includes = 
			{ "commentCount", "topCommentCount", "mesCount" }), })
	public ResponseInfo getFans(@NotNull String openId) throws GlobalException {
		return wechatFansService.getFans(openId);
	}

	/**
	 * 发送信息
	* @Title: send 
	* @param request 请求
	* @param messageRequest 信息对象
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	@PostMapping(value = "/send")
	public ResponseInfo send(HttpServletRequest request, @RequestBody MessageRequest messageRequest)
			throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, null, messageRequest.getAppId());
		return wechatFansService.send(messageRequest.getAppId(), messageRequest);
	}

}
