package com.jeecms.front.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.system.service.GlobalConfigService;

/**
 * "后台管理"登录页面获取到图片
 * @author: chenming
 * @date:   2019年11月26日 上午9:17:05
 */
@RequestMapping(value = "/register")
@RestController
public class LoginImageController {
	
	@Autowired
	private GlobalConfigService configService;
	
	/**
	 * 获取图片
	 * @Title: getImage  
	 * @throws GlobalException      
	 * @return: ResponseInfo
	 */
	@RequestMapping(value = "/getImage",method = RequestMethod.GET)
	public ResponseInfo getImage() throws GlobalException {
		Map<String,String> attr = configService.get().getConfigMap();
		String imageUrl = attr.get(GlobalConfigAttr.LOGIN_SYS_IMG_URL);
		return new ResponseInfo(imageUrl);
	}
}
