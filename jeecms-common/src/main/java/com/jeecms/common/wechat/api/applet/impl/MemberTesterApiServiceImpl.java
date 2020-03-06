package com.jeecms.common.wechat.api.applet.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.applet.MemberTesterApiService;
import com.jeecms.common.wechat.bean.OpenReturnCode;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.applet.BindTesterRequest;
import com.jeecms.common.wechat.bean.request.applet.UnbindTesterRequest;
import com.jeecms.common.wechat.bean.response.applet.BindTesterResponse;
import com.jeecms.common.wechat.bean.response.applet.UnbindTesterResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 
 * @Description: 小程序体验者操作
 * @author: chenming
 * @date:   2018年11月12日 下午3:54:58     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class MemberTesterApiServiceImpl implements MemberTesterApiService{
	/** 解除绑定小程序的体验者*/
	public final String UNBIND_TESTER=Const.DoMain.API_URI.concat("/wxa/unbind_tester");
	/** 绑定微信用户为小程序体验者*/
	public final String BIND_TESTER=Const.DoMain.API_URI.concat("/wxa/bind_tester");
	
	public final String ACCESS_TOKEN="access_token";
	
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public UnbindTesterResponse unbindTester(UnbindTesterRequest request, ValidateToken validateToke)
			throws GlobalException {
		Map<String, String> params=new HashMap<>(16);
		params.put(ACCESS_TOKEN, validateToke.getAccessToken());
		UnbindTesterResponse response=HttpUtil.postJsonBean(UNBIND_TESTER, params, SerializeUtil.beanToJson(request), UnbindTesterResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			/** 需要将此处的英文提示修改成为中文提示*/
			Integer errcode=Integer.valueOf(response.getErrcode());
			if (errcode!=-1) {
				errcode=Math.abs(errcode);
			}
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),OpenReturnCode.get(errcode)));
		}
	}
	
	
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public BindTesterResponse bindTester(BindTesterRequest request, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		BindTesterResponse response=HttpUtil.postJsonBean(BIND_TESTER, params, SerializeUtil.beanToJson(request), BindTesterResponse.class);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			/** 需要将此处的英文提示修改成为中文提示*/
			Integer errcode=Integer.valueOf(response.getErrcode());
			if (errcode!=-1) {
				errcode=Math.abs(errcode);
			}
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),OpenReturnCode.get(errcode)));
		}
	}
}
