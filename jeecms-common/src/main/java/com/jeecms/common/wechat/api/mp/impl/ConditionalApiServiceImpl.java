package com.jeecms.common.wechat.api.mp.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.ConditionalApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.menu.AddConditionalRequest;
import com.jeecms.common.wechat.bean.request.mp.menu.DelConditionalRequest;
import com.jeecms.common.wechat.bean.request.mp.menu.TrymatchRequest;
import com.jeecms.common.wechat.bean.response.mp.menu.AddConditionalResponse;
import com.jeecms.common.wechat.bean.response.mp.menu.DeleteMenuResponse;
import com.jeecms.common.wechat.bean.response.mp.menu.GetConditionalResponse;
import com.jeecms.common.wechat.bean.response.mp.menu.TrymatchResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 
 * @Description: 个性化菜单管理
 * @author: chenming
 * @date:   2018年8月9日 上午8:50:15     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class ConditionalApiServiceImpl implements ConditionalApiService{
	
	/** 增加一个个性化菜单*/
	public final String ADD_CONDITIONAL=Const.DoMain.API_URI.concat("/cgi-bin/menu/addconditional");
	/** 删除一个个性化菜单*/
	public final String DEL_CONDITIONAL=Const.DoMain.API_URI.concat("/cgi-bin/menu/delconditional");
	/** 测试个性化菜单匹配结果*/
	public final String TRYMATCH=Const.DoMain.API_URI.concat("/cgi-bin/menu/trymatch");
	/** 查询个性化菜单*/
	public final String GET_CONDITIONAL=Const.DoMain.API_URI.concat("/cgi-bin/menu/get");
	
	public final String ACCESS_TOKEN="access_token";
	
	
	
	/**
	 * 增加一个个性化菜单
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public AddConditionalResponse addConditional(AddConditionalRequest aConditionalRequest, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		AddConditionalResponse aConditionalResponse=HttpUtil.postJsonBean(ADD_CONDITIONAL, params, SerializeUtil.beanToJson(aConditionalRequest), AddConditionalResponse.class);
		if(aConditionalResponse.SUCCESS_CODE.equals(aConditionalResponse.getErrcode())) {
			return aConditionalResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(aConditionalResponse.getErrcode(),aConditionalResponse.getErrmsg()));
		}
			
	}
	
	public static void main(String[] args) {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put("access_token","22_gySkOeMChBOy5cRPAk_5UsE6nyuX42AJU0fmXIL4-7xmbTTnNTZ5MFgNQD8BiGwhH8XBERVA9IR3MOHOOzFJKFTMsVfvnusPuIXKGCZYFqHKe7y27Rltu9edWHOqMhaXw3VyWGrNd60zxvrLHLIeAEDOSP");
		HttpUtil.get("https://api.weixin.qq.com/cgi-bin/menu/delete", params);
	}
	
	/**
	 * 删除一个个性化菜单
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public DeleteMenuResponse delConditional(DelConditionalRequest dRequest, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN,validateToken.getAccessToken());
		DeleteMenuResponse dMenuResponse=HttpUtil.postJsonBean(DEL_CONDITIONAL, params, SerializeUtil.beanToJson(dRequest),DeleteMenuResponse.class);
		if(dMenuResponse.SUCCESS_CODE.equals(dMenuResponse.getErrcode())) {
			return dMenuResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(dMenuResponse.getErrcode(),dMenuResponse.getErrmsg()));
		}
	}
	
	/**
	 * 测试个性化菜单匹配结果
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public TrymatchResponse trymatch(TrymatchRequest trymatchRequest, ValidateToken validateToken)
			throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN,validateToken.getAccessToken());
		TrymatchResponse tResponse=HttpUtil.postJsonBean(TRYMATCH, params, SerializeUtil.beanToJson(trymatchRequest),TrymatchResponse.class);
		if(tResponse.SUCCESS_CODE.equals(tResponse.getErrcode())) {
			return tResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(tResponse.getErrcode(),tResponse.getErrmsg()));
		}
	}
	
	/**
	 * 查询个性化菜单(拥有个性化菜单)
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public GetConditionalResponse getConditional(ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN,validateToken.getAccessToken());
		GetConditionalResponse getConditionalResponse=HttpUtil.getJsonBean(GET_CONDITIONAL, params, GetConditionalResponse.class);
		if (getConditionalResponse.SUCCESS_CODE.equals(getConditionalResponse.getErrcode())) {
			return getConditionalResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(getConditionalResponse.getErrcode(),getConditionalResponse.getErrmsg()));
		}
	}

}
