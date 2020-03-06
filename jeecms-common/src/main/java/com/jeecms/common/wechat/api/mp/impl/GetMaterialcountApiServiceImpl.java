package com.jeecms.common.wechat.api.mp.impl;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.GetMaterialcountApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.GetMaterialcountRequest;
import com.jeecms.common.wechat.bean.response.mp.material.GetMaterialcountResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;

/**
 * 
 * @Description: 获取素材总数
 * @author: chenming
 * @date:   2018年8月2日 下午2:29:20     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class GetMaterialcountApiServiceImpl implements GetMaterialcountApiService{
	
	/** 获取素材总数*/
	public final String GET_MATERIALCOUNT=Const.DoMain.API_URI.concat("/cgi-bin/material/get_materialcount");
	
	public final String ACCESS_TOKEN="access_token";
	
	/**
	 * 
	 * @Title: getMaterialcount  获取素材总数
	 * @param getMaterialRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetMaterialcountResponse
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public GetMaterialcountResponse getMaterialcount(GetMaterialcountRequest getMaterialcountRequest, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		GetMaterialcountResponse getMaterialcountResponse=HttpUtil.getJsonBean(GET_MATERIALCOUNT, params, GetMaterialcountResponse.class);
		if (getMaterialcountResponse.SUCCESS_CODE.equals(getMaterialcountResponse.getErrcode())) {
			return getMaterialcountResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(getMaterialcountResponse.getErrcode(),getMaterialcountResponse.getErrmsg()));
		}
	}

}
