package com.jeecms.common.wechat.api.mp.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.GetInterimMaterialApiService;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.GetInterimMaterialRequest;
import com.jeecms.common.wechat.util.client.HttpUtil;

/**
 * 获取其它临时素材service实现类
 * @author: chenming
 * @date:   2019年6月4日 下午5:32:51
 */
@Service
public class GetInterimMaterialApiServiceImpl implements GetInterimMaterialApiService{
	/** 获取素材*/
	public final String GET_VIDEO_MATERIAL = Const.DoMain.API_URI_HTTP.concat("/cgi-bin/media/get");
	/** 获取其它素材*/
	public final String GET_OTHER_MATERIAL = Const.DoMain.API_URI.concat("/cgi-bin/media/get");
	/** token*/
	public final String ACCESS_TOKEN = "access_token";
	
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public MediaFile getVideo(GetInterimMaterialRequest request, ValidateToken validateToken)
			throws GlobalException {
		Map<String,String> params = new LinkedHashMap<String, String>();
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		params.put("media_id", request.getMediaId());
		MediaFile mediaFile = HttpUtil.downloadByGet(GET_VIDEO_MATERIAL, params);
		if (mediaFile != null) {
			return mediaFile;
		}
		return null;
	}

	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public MediaFile getMaterial(GetInterimMaterialRequest request, ValidateToken validateToken)
			throws GlobalException {
		Map<String,String> params = new LinkedHashMap<String, String>();
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		params.put("media_id", request.getMediaId());
		MediaFile mediaFile = HttpUtil.downloadByGet(GET_OTHER_MATERIAL, params);
		if (mediaFile != null) {
			return mediaFile;
		}
		return null;
	}

}
