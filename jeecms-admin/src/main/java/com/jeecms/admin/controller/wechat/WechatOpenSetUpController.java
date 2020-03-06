/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.wechat.domain.AbstractWeChatOpen;
import com.jeecms.wechat.service.AbstractWeChatOpenService;

/**
 * 微信开放平台设置控制层
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年5月7日 下午3:20:52
 */
@RequestMapping(value = "/weChatSetUp")
@RestController
public class WechatOpenSetUpController extends BaseController<AbstractWeChatOpen, Integer> {

	/**
	 * 保存微信开放平台信息
	 * 
	 * @Title: save
	 * @param request 请求
	 * @param response 响应
	 * @param chatOpen 开放平台对象
	 * @param result 检测
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@PostMapping
	public ResponseInfo save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody @Valid AbstractWeChatOpen chatOpen, BindingResult result) throws GlobalException {
		return abstractWeChatOpenService.saveAbstractWeChatOpen(chatOpen);
	}

	/**
	 * 显示微信开放平台信息
	 * 
	 * @Title: get
	 * @param request 请求
	 * @param paginable 分页
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@GetMapping
	@SerializeField(clazz = AbstractWeChatOpen.class, includes = { "id", "name", "appId", "appSecret",
			"messageDecryptKey", "messageValidateToken" })
	public ResponseInfo get(HttpServletRequest request, Paginable paginable) throws GlobalException {
		AbstractWeChatOpen info = abstractWeChatOpenService.findOpenConfig();
		AbstractWeChatOpen weChatOpen = new AbstractWeChatOpen();
		if (info != null) {
			weChatOpen = info;
		}
		return new ResponseInfo(weChatOpen);
	}

	/**
	 * 修改微信开放平台信息
	 * 
	 * @Title: update
	 * @param request 请求
	 * @param response 响应
	 * @param chatOpen 开放平台对象
	 * @param result 检测
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@PutMapping
	public ResponseInfo update(HttpServletRequest request, HttpServletResponse response,
			@RequestBody @Valid AbstractWeChatOpen chatOpen, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		return abstractWeChatOpenService.updateAbstractWeChatOpen(chatOpen);
	}

	@Autowired
	private AbstractWeChatOpenService abstractWeChatOpenService;
}
