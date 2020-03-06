package com.jeecms.common.wechat.api.applet.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.applet.GetCategoryApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.response.applet.GetCategoryResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;

/**
 * 
 * @Description: 获取小程序账号的可选类目
 * @author: chenming
 * @date:   2018年11月12日 下午3:53:43     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class GetCategoryApiServiceImpl implements GetCategoryApiService{
	
	public final String GET_CATEGORY=Const.DoMain.API_URI.concat("/wxa/get_category");
	
	public final String ACCESS_TOKEN="access_token";
	
	@Override
	public GetCategoryResponse getCategory(ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		GetCategoryResponse response=HttpUtil.getJsonBean(GET_CATEGORY, params, GetCategoryResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),response.getErrmsg()));
		}
	}
	
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.ACCESS_TOKEN)
	public GetCategoryResponse getNewCategory(ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		GetCategoryResponse response=HttpUtil.getJsonBean(GET_CATEGORY, params, GetCategoryResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),response.getErrmsg()));
		}
	}

}
