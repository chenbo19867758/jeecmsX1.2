package com.jeecms.front.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.wechat.service.WechatSignService;

/**
 * 微信分享controller层
 * @author: chenming
 * @date:   2019年10月31日 下午3:27:43
 */
@RequestMapping("/wechat/sign")
@RestController
public class WechatSignController {
	
	@Autowired
	private WechatSignService service;
	
	/**
	 * 微信分享-签名功能
	 * @Title: sign  
	 * @param request	request请求
	 * @throws GlobalException    全局异常  
	 * @return: ResponseInfo
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseInfo sign(HttpServletRequest request,@RequestParam String url) throws GlobalException {
		return new ResponseInfo(service.sign(request,url));
	}
}
