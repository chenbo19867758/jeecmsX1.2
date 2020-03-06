/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.util.ResponseUtils;
import com.jeecms.voice.service.BaiduVoiceService;

/**   
 * 百度语音控制器
 * @author: ljw
 * @date:   2019年6月14日 上午9:51:27     
 */
@RestController
public class YuyinActController {

	@RequestMapping(value = "/yuyin/ajax.jspx")
	public ResponseInfo getMp3Url(HttpServletRequest request, String text,
			HttpServletResponse response) 
			throws Exception {
		return new ResponseInfo(baiduVoiceService.text2audio(text));
	}

	@Autowired
	private BaiduVoiceService baiduVoiceService;
}
