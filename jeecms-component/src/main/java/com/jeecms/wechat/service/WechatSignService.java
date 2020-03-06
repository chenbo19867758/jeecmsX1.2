package com.jeecms.wechat.service;

import javax.servlet.http.HttpServletRequest;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.domain.dto.WechatSignVo;

/**
 * 微信分享service接口
 * @author: chenming
 * @date:   2019年11月1日 上午9:35:07
 */
public interface WechatSignService {
	
	/**
	 * 微信分享-签名功能
	 * @Title: sign  
	 * @param request	request请求
	 * @throws GlobalException    全局异常  
	 * @return: WechatSignVo
	 */
	WechatSignVo sign(HttpServletRequest request,String url) throws GlobalException;
	
}
