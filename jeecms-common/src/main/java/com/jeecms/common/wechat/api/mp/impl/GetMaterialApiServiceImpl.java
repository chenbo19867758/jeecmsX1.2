package com.jeecms.common.wechat.api.mp.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.GetMaterialApiService;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.GetMaterialRequest;
import com.jeecms.common.wechat.bean.response.mp.material.GetNewsResponse;
import com.jeecms.common.wechat.bean.response.mp.material.GetVideoResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 
 * @Description: 获取永久素材
 * @author: chenming
 * @date:   2018年8月2日 下午1:52:17     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class GetMaterialApiServiceImpl implements GetMaterialApiService{
	
	/** 获取永久素材*/
	public final String GET_MATERIAL=Const.DoMain.API_URI.concat("/cgi-bin/material/get_material");
			
	public final String ACCESS_TOKEN="access_token";
	
	/**
	 * 获取图文素材
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public GetNewsResponse getNews(GetMaterialRequest getMaterialRequest, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		GetNewsResponse getNewsResponse=HttpUtil.postJsonBean(GET_MATERIAL, params, SerializeUtil.beanToJson(getMaterialRequest), GetNewsResponse.class);
		if(getNewsResponse.SUCCESS_CODE.equals(getNewsResponse.getErrcode())) {
			return getNewsResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(getNewsResponse.getErrcode(),getNewsResponse.getErrmsg()));
		}
	}
	
	/**
	 * 获取视频素材
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public GetVideoResponse getVideo(GetMaterialRequest getMaterialRequest, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		GetVideoResponse getVideoResponse=HttpUtil.postJsonBean(GET_MATERIAL, params, SerializeUtil.beanToJson(getMaterialRequest), GetVideoResponse.class);
		if(getVideoResponse.SUCCESS_CODE.equals(getVideoResponse.getErrcode())) {
			return getVideoResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(getVideoResponse.getErrcode(),getVideoResponse.getErrmsg()));
		}
	}
	
	/**
	 * 获取其它素材
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public MediaFile getMaterial(GetMaterialRequest getMaterialRequest, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		MediaFile mediaFile = HttpUtil.downloadByPost(GET_MATERIAL, params, SerializeUtil.beanToJson(getMaterialRequest));
		if (mediaFile!=null) {
			return mediaFile;
		}else {
			return null;
		}
	}

}
