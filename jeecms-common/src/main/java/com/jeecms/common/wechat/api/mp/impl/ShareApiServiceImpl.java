package com.jeecms.common.wechat.api.mp.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.ShareApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.response.mp.share.GetTicketResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;

/**
 * 签名API的service实现类
 * @author: chenming
 * @date:   2019年10月31日 下午4:15:38
 */
@Service
public class ShareApiServiceImpl implements ShareApiService{

	public final String GET_TICKET = Const.DoMain.API_URI.concat("/cgi-bin/ticket/getticket");
	
	public final String ACCESS_TOKEN="access_token";
	
	public final String TYPE = "type";
	
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public GetTicketResponse getTicket(ValidateToken validateToken) throws GlobalException {
		Map<String,String> params = new LinkedHashMap<String,String>();
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		params.put(TYPE, "jsapi");
		GetTicketResponse response = HttpUtil.getJsonBean(GET_TICKET, params, GetTicketResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		} else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),response.getErrmsg()));
		}
	}

}
