package com.jeecms.common.wechat.api.applet;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.ValidateToken;

public interface GetQrcodeApiService {
	
	MediaFile getQrcode(ValidateToken validateToken) throws GlobalException;
}
