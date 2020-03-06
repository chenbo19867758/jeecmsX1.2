package com.jeecms.common.wechat.api.applet.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.applet.GetTemplateDraftApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.applet.TemplateDraftRequest;
import com.jeecms.common.wechat.bean.response.applet.BaseAppletResponse;
import com.jeecms.common.wechat.bean.response.applet.GetTemplateDraftListResponse;
import com.jeecms.common.wechat.bean.response.applet.GetTemplateListResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 
 * TODO
 * @author: tom
 * @date:   2019年3月8日 下午4:37:13
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class GetTemplateDraftApiServiceImpl implements GetTemplateDraftApiService{
	public final String GET_DRAFT=Const.DoMain.API_URI.concat("/wxa/gettemplatedraftlist");
	public final String ADD_TO_TEMPLATE=Const.DoMain.API_URI.concat("/wxa/addtotemplate");
	public final String GET_TEMPLATE_LIST=Const.DoMain.API_URI.concat("/wxa/gettemplatelist");
	public final String DELETE_TEMPLATE=Const.DoMain.API_URI.concat("/wxa/deletetemplate");
	
	public final String ACCESS_TOKEN="access_token";
	/**
	 * 获取草稿箱内的所有临时代码草稿
	 */
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.COMONTENT_ACCESS_TOKEN)
	public GetTemplateDraftListResponse getTemplateDraftList(ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<>(500);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		GetTemplateDraftListResponse response = HttpUtil.getJsonBean(GET_DRAFT, params,GetTemplateDraftListResponse.class);
		if(response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),response.getErrmsg()));
		}
	}
	
	
	/**
	 * 将草稿箱的草稿选为小程序代码模版
	 */
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.COMONTENT_ACCESS_TOKEN)
	public BaseAppletResponse addToTemplate(ValidateToken validateToken,Integer draftId) throws GlobalException {
		Map<String, String> params=new HashMap<>(500);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		TemplateDraftRequest postData=new TemplateDraftRequest();
		postData.setDraftId(draftId);
		BaseAppletResponse response = HttpUtil.postJsonBean(ADD_TO_TEMPLATE, params, SerializeUtil.beanToJson(postData), BaseAppletResponse.class);
		if(response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),response.getErrmsg()));
		}
	}


	/**
	 * 获取代码模版库中的所有小程序代码模版
	 */
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.COMONTENT_ACCESS_TOKEN)
	public GetTemplateListResponse getTemplateList(ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<>(500);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		GetTemplateListResponse response = HttpUtil.getJsonBean(GET_TEMPLATE_LIST, params, GetTemplateListResponse.class);
		if(response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),response.getErrmsg()));
		}
	}

    /**
     * 删除指定小程序代码模版
     */
	@Override
	@ValidWeChatToken(value = Const.ValidTokenType.COMONTENT_ACCESS_TOKEN)
	public BaseAppletResponse deletetemplate(ValidateToken validateToken, Integer templateId)
			throws GlobalException {
		Map<String, String> params=new HashMap<>(500);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		TemplateDraftRequest postData=new TemplateDraftRequest();
		postData.setTemplateId(templateId);
		BaseAppletResponse response = HttpUtil.postJsonBean(DELETE_TEMPLATE, params, SerializeUtil.beanToJson(postData), BaseAppletResponse.class);
		if(response.SUCCESS_CODE.equals(response.getErrcode())) {
			return response;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(response.getErrcode(),response.getErrmsg()));
		}
	}

	
}
