package com.jeecms.common.wechat.api.mp.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.BatchgetMaterialApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.BatchgetMaterialRequest;
import com.jeecms.common.wechat.bean.response.mp.material.BatchgetMaterialResponse;
import com.jeecms.common.wechat.bean.response.mp.material.BatchgetNewsResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * @Description: 获取永久素材列表
 * @author: chenming
 * @date:   2018年7月31日 下午8:44:47     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class BatchgetMaterialApiServiceImpl implements BatchgetMaterialApiService{
	
	/** 获取永久素材*/
	public final String BATCHGET_MATERIAL=Const.DoMain.API_URI.concat("/cgi-bin/material/batchget_material");

	public final String ACCESS_TOKEN="access_token";
	
	/**
	 *	获取永久素材列表（图文News除外）
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public BatchgetMaterialResponse batchgetMaterial(BatchgetMaterialRequest batchgetMaterialRequest,
			ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		BatchgetMaterialResponse batchgetMaterialResponse=HttpUtil.postJsonBean(BATCHGET_MATERIAL, params, SerializeUtil.beanToJson(batchgetMaterialRequest), BatchgetMaterialResponse.class);
		if(batchgetMaterialResponse.SUCCESS_CODE.equals(batchgetMaterialResponse.getErrcode())) {
			return batchgetMaterialResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(batchgetMaterialResponse.getErrcode(),batchgetMaterialResponse.getErrmsg()));
		}
	}
	
	/**
	 *  获取永久图文素材列表
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public BatchgetNewsResponse batchgetNews(BatchgetMaterialRequest batchgetMaterialRequest,
			ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		BatchgetNewsResponse batchgetNewsResponse=HttpUtil.postJsonBean(BATCHGET_MATERIAL, params, SerializeUtil.beanToJson(batchgetMaterialRequest), BatchgetNewsResponse.class);
		if(batchgetNewsResponse.SUCCESS_CODE.equals(batchgetNewsResponse.getErrcode())) {
			return batchgetNewsResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(batchgetNewsResponse.getErrcode(),batchgetNewsResponse.getErrmsg()));
		}
	}

}
