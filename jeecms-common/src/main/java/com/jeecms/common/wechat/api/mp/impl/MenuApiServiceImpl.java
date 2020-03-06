package com.jeecms.common.wechat.api.mp.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.MenuApiService;
import com.jeecms.common.wechat.bean.OpenReturnCode;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.menu.CreateMenuRequest;
import com.jeecms.common.wechat.bean.response.mp.menu.CreateMenuResponse;
import com.jeecms.common.wechat.bean.response.mp.menu.DeleteMenuResponse;
import com.jeecms.common.wechat.bean.response.mp.menu.GetMenuResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;

/**
 * 
 * @Description: 菜单配置
 * @author: chenming
 * @date:   2018年8月8日 下午2:38:32     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class MenuApiServiceImpl implements MenuApiService{
	
	/** 新增菜单*/
	public final String CREATE_MENU=Const.DoMain.API_URI.concat("/cgi-bin/menu/create");
	/** 查询菜单*/
	public final String GET_MENU=Const.DoMain.API_URI.concat("/cgi-bin/menu/get");
	/** 删除菜单*/
	public final String DELETE_MENU=Const.DoMain.API_URI.concat("/cgi-bin/menu/delete");
	
	public final String ACCESS_TOKEN="access_token";
	
	/**
	 * 创建菜单
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public CreateMenuResponse createMenu(CreateMenuRequest cMenuRequest, ValidateToken vToken) throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, vToken.getAccessToken());
		CreateMenuResponse cMenuResponse=HttpUtil.postJsonBean(CREATE_MENU, params, SerializeUtil.beanToJson(cMenuRequest), CreateMenuResponse.class);
		if(cMenuResponse.SUCCESS_CODE.equals(cMenuResponse.getErrcode())) {
			return cMenuResponse;
		}else {
			String errcode = cMenuResponse.getErrcode();
			String errmsg = cMenuResponse.getErrmsg();
			if (errcode != null) {
				Integer errcodeInt = Integer.valueOf(errcode.substring(1));
				if (OpenReturnCode.get(errcodeInt) != null) {
					errmsg = OpenReturnCode.get(errcodeInt);
				}
			}
			throw new GlobalException(new WeChatExceptionInfo(cMenuResponse.getErrcode(),errmsg));
		}
	}

	/**
	 * 查询菜单
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public GetMenuResponse getMenu(ValidateToken vToken) throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, vToken.getAccessToken());
		GetMenuResponse gMenuResponse=HttpUtil.getJsonBean(GET_MENU, params, GetMenuResponse.class);
		if(gMenuResponse.SUCCESS_CODE.equals(gMenuResponse.getErrcode())) {
			return gMenuResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(gMenuResponse.getErrcode(),gMenuResponse.getErrmsg()));
		}
	}
	
	/**
	 * 删除菜单
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public DeleteMenuResponse deleteMenu(ValidateToken vToken) throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, vToken.getAccessToken());
		DeleteMenuResponse dMenuResponse=HttpUtil.getJsonBean(DELETE_MENU, params, DeleteMenuResponse.class);
		if (dMenuResponse.SUCCESS_CODE.equals(dMenuResponse.getErrcode())) {
			return dMenuResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(dMenuResponse.getErrcode(),dMenuResponse.getErrmsg()));
		}
	}

}
