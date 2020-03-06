package com.jeecms.common.wechat.api.mp.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.UpdateNewsApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.UpdateNewsRequest;
import com.jeecms.common.wechat.bean.response.mp.material.UpdateNewsResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 
 * @Description:	修改永久图文素材
 * @author: chenming
 * @date:   2018年8月2日 下午1:57:28     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class UpdateNewsApiServiceImpl implements UpdateNewsApiService{
	
	/** 修改永久图文素材*/
	public final String UPDATE_MATERIAL=Const.DoMain.API_URI.concat("/cgi-bin/material/update_news");
			
	public final String ACCESS_TOKEN="access_token";
	
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public UpdateNewsResponse updateNews(UpdateNewsRequest updateNewsRequest, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		UpdateNewsResponse updateNewsResponse=HttpUtil.postJsonBean(UPDATE_MATERIAL, params, SerializeUtil.beanToJson(updateNewsRequest), UpdateNewsResponse.class);
		if(updateNewsResponse.SUCCESS_CODE.equals(updateNewsResponse.getErrcode())) {
			return updateNewsResponse;
		}else {
			String errmsg = null;
			if ("40114".equals(updateNewsResponse.getErrcode().substring(1))) {
				errmsg = "发送的数据格式出错，请检查数据索引是否正确？";
			} else {
				errmsg = updateNewsResponse.getErrmsg();
			}
			throw new GlobalException(new WeChatExceptionInfo(updateNewsResponse.getErrcode(),errmsg));
		}
				
	}

}
