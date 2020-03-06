/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.admin.controller.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.WechatFansTag;
import com.jeecms.wechat.domain.dto.WechatTagDto;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatFansTagService;

/**   
 * 微信粉丝标签控制器
 * @author: ljw
 * @date:   2019年5月28日 上午11:51:04     
 */
@RequestMapping(value = "/wechattags")
@RestController
public class WechatFansTagController extends BaseController<WechatFansTag, Integer> {

	@Autowired
	private WechatFansTagService wechatFansTagService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;
	
	/**
	 * 新建标签
	* @Title: addTags 
	* @param request 请求
	* @param dto 标签传输
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	@PostMapping()
	public ResponseInfo addTags(HttpServletRequest request, @RequestBody WechatTagDto dto) 
			throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, null, dto.getAppId());
		return wechatFansTagService.addTags(dto);
	}

	/**
	 *  删除标签
	* @Title: deleteTags 
	* @param request 请求
	* @param dto 标签传输 
	* @return ResponseInfo 响应
	* @throws GlobalException 异常
	 */
	@DeleteMapping()
	public ResponseInfo deleteTags(HttpServletRequest request, @RequestBody WechatTagDto dto) 
			throws GlobalException {
		AbstractWeChatInfo info = abstractWeChatInfoService.findAppId(dto.getAppId());
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, info.getId(), null);
		return wechatFansTagService.deleteTags(dto.getAppId(), dto.getTagid());
	}

	/**
	 * 编辑标签
	* @Title: updateTags 
	* @param request 请求
	* @param dto 标签传输 
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	@PutMapping()
	public ResponseInfo updateTags(HttpServletRequest request, @RequestBody WechatTagDto dto)
			throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, null, dto.getAppId());
		return wechatFansTagService.updateTags(dto);
	}

	/**
	 * 查询便签名是否存在
	* @Title: check 
	* @param request 请求
	* @param tagName 标签名称
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/check")
	public ResponseInfo check(HttpServletRequest request, String appId, String tagName) throws GlobalException {
		Boolean flag = wechatFansTagService.check(appId,tagName);
		if (!flag) {
			return new ResponseInfo(false);
		} else {
			return new ResponseInfo(true);
		}
	}

	/**
	 * 设置标签
	* @Title: setTags 
	* @param request 请求
	* @param dto 传输Dto
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	@PostMapping(value = "/set")
	public ResponseInfo setTags(HttpServletRequest request, @RequestBody WechatTagDto dto)
			throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, null, dto.getAppId());
		return wechatFansTagService.setTags(dto);
	}

	/**
	 *  同步标签
	* @Title: syncTags 
	* @param request 请求
	* @param response 响应
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/syncTags")
	public ResponseInfo syncTags(HttpServletRequest request, HttpServletResponse response) throws GlobalException {
		String appId = request.getParameter("appId");
		Integer userId = SystemContextUtils.getUserId(request);
		//检查权限
		abstractWeChatInfoService.checkWeChatAuth(userId, null, appId);
		return wechatFansTagService.syncTags(appId);
	}

	/**
	 *  获取该公众号下的全部标签
	* @Title: app 
	* @param request 请求
	* @param response 响应
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	@GetMapping(value = "/app")
	@MoreSerializeField({ 
		@SerializeField(clazz = WechatFansTag.class, includes = { "id", "tagName", "userCount" }), })
	public ResponseInfo app(HttpServletRequest request, HttpServletResponse response) throws GlobalException {
		String appId = request.getParameter("appId");
		return wechatFansTagService.tags(appId);
	}
}
