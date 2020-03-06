package com.jeecms.common.wechat.api.mp.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.DelMaterialApiService;
import com.jeecms.common.wechat.bean.OpenReturnCode;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.DelMaterialRequest;
import com.jeecms.common.wechat.bean.response.mp.material.DelMateralResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 
 * @Description: 删除永久素材
 * @author: chenming
 * @date:   2018年8月2日 下午1:50:51     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class DelMaterialApiServiceImpl implements DelMaterialApiService{
	
	/** 删除永久素材*/
	public final String DELETE_MATERIAL=Const.DoMain.API_URI.concat("/cgi-bin/material/del_material");
	
	public final String ACCESS_TOKEN="access_token";
	
	/**
	 * 删除永久素材
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public DelMateralResponse delMaterial(DelMaterialRequest delMaterialRequest, ValidateToken validateToken)
			throws GlobalException {
		delMaterialRequest.setId(null);
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		DelMateralResponse delMateralResponse=HttpUtil.postJsonBean(DELETE_MATERIAL, params, SerializeUtil.beanToJson(delMaterialRequest), DelMateralResponse.class);
		if(delMateralResponse.SUCCESS_CODE.equals(delMateralResponse.getErrcode())) {
			return delMateralResponse;
		}else {
			String errcode = delMateralResponse.getErrcode();
			String errmsg = delMateralResponse.getErrmsg();
			if (errcode != null) {
				Integer errcodeInt = Integer.valueOf(errcode.substring(1));
				if (OpenReturnCode.get(errcodeInt) != null) {
					errmsg = OpenReturnCode.get(errcodeInt);
				}
			}
			throw new GlobalException(new WeChatExceptionInfo(delMateralResponse.getErrcode(),errmsg));
		}
	}

}
