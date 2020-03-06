package com.jeecms.common.ueditor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.Zipper;

/**
 * 对客户端检测授权 controller v1版
 * 
 * @author: tom
 * @date: 2019年1月5日 下午6:08:52
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RestController
@RequestMapping(value = "/v1/auth")
public class UeditorCodeController {

	
	/**
	 * 检查code
	 * 
	 * @Title: check
	 * @param authCode
	 *            code
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws GlobalException
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	@RequestMapping(value = "/checkCode")
	public ResponseInfo check(String authCode, HttpServletRequest request, HttpServletResponse response)
			throws GlobalException {
		String url = "http://192.168.0.173:8081/v1/notify/check";
		Map<String, String> params = new HashMap<String, String>(200);
		params.put("authCode", authCode);
		ResponseInfo obj = new ResponseInfo(false);
		String res;
		try {
			res = Zipper.post(url, params);
			obj = JSONObject.parseObject(res, ResponseInfo.class);
		} catch (Exception e) {
			
		}
		return obj;
	}

}
