package com.jeecms.common.wechat.api.applet.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.applet.GetQrcodeApiService;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.applet.GetQrcodeRequest;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

@Service
public class GetQrcodeApiServiceImpl implements GetQrcodeApiService{
	
	public final String GET_QRCODE = Const.DoMain.API_URI.concat("/wxa/getwxacodeunlimit");

	public final String ACCESS_TOKEN="access_token";
	
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public MediaFile getQrcode(ValidateToken validateToken) throws GlobalException {
		Map<String,String> params = new HashMap<String, String>();
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		GetQrcodeRequest request = new GetQrcodeRequest("jeecms.jpg", "", 430, false, null, false);
		MediaFile mediaFile = HttpUtil.downloadByPost(GET_QRCODE, params, SerializeUtil.beanToJson(request));
		if (mediaFile!=null) {
			return mediaFile;
		}else {
			return null;
		}
	}
}
