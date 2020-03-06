package com.jeecms.common.wechat.api.mp.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.annotations.ValidWeChatToken;
import com.jeecms.common.wechat.api.mp.AddMaterialApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.AddMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.AddNewsRequest;
import com.jeecms.common.wechat.bean.request.mp.material.AddVideoMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.UploadImgRequest;
import com.jeecms.common.wechat.bean.response.mp.material.AddMeterialResponse;
import com.jeecms.common.wechat.bean.response.mp.material.AddNewsResponse;
import com.jeecms.common.wechat.bean.response.mp.material.UploadImgResponse;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.serialize.SerializeUtil;
/**
 * 
 * @Description: 新增永久素材
 * @author: chenming
 * @date:   2018年8月2日 下午1:51:17     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class AddMaterialApiServiceImpl implements AddMaterialApiService{
	
	/** 新增永久图文素材*/
	public final String ADD_NEWS=Const.DoMain.API_URI.concat("/cgi-bin/material/add_news");
	/** 上传图文消息内的图片获取URL*/
	public final String UPLOADIMG=Const.DoMain.API_URI.concat("/cgi-bin/media/uploadimg");
	/** 新增其它类型永久素材*/
	public final String ADD_MATERIAL=Const.DoMain.API_URI.concat("/cgi-bin/material/add_material");
	
	public final String ACCESS_TOKEN="access_token";
	
	public final String TYPE="type";
	
	/**
	 * 
	 * @Title: addNews  新增永久图文素材
	 * @param addNewsRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: AddNewsResponse
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public AddNewsResponse addNews(AddNewsRequest addNewsRequest, ValidateToken validateToken) throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		AddNewsResponse addNewsResponse=HttpUtil.postJsonBean(ADD_NEWS, params, SerializeUtil.beanToJson(addNewsRequest), AddNewsResponse.class);
		if(addNewsResponse.SUCCESS_CODE.equals(addNewsResponse.getErrcode())) {
			return addNewsResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(addNewsResponse.getErrcode(),addNewsResponse.getErrmsg()));
		}
	}
	
	/**
	 * 
	 * @Title: uploadImg  上传图文消息内的图片获取URL
	 * @param uploadImgRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: UploadImgResponse
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public UploadImgResponse uploadImg(UploadImgRequest uploadImgRequest, ValidateToken validateToken,File file)
			throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		UploadImgResponse uploadImgResponse=HttpUtil.uploadJsonBean(UPLOADIMG, params, null, file, UploadImgResponse.class);
		if(uploadImgResponse.SUCCESS_CODE.equals(uploadImgResponse.getErrcode())) {
			return uploadImgResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(uploadImgResponse.getErrcode(),uploadImgResponse.getErrmsg()));
		}
	}
	
	/**
	 * 新增其它类型永久素材
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public AddMeterialResponse addMaterial(AddMaterialRequest addMaterialRequest, ValidateToken validateToken,File file)
			throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		params.put(TYPE, addMaterialRequest.getType());
		AddMeterialResponse addMeterialResponse=HttpUtil.uploadJsonBean(ADD_MATERIAL, params, null, file, AddMeterialResponse.class);

		if(addMeterialResponse.SUCCESS_CODE.equals(addMeterialResponse.getErrcode())) {
			return addMeterialResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(addMeterialResponse.getErrcode(),addMeterialResponse.getErrmsg()));
		}
	}
	
	/**
	 * 新增其Video永久素材
	 */
	@Override
	@ValidWeChatToken(value=Const.ValidTokenType.ACCESS_TOKEN)
	public AddMeterialResponse addVideoMaterial(AddVideoMaterialRequest addVideoMaterialRequest,
			ValidateToken validateToken,File file) throws GlobalException {
		Map<String, String> params=new HashMap<String,String>(16);
		params.put(ACCESS_TOKEN, validateToken.getAccessToken());
		params.put(TYPE, Const.Mssage.REQ_MESSAGE_TYPE_VIDEO);
		params.put("description", SerializeUtil.beanToJson(addVideoMaterialRequest));
		AddMeterialResponse addMeterialResponse=HttpUtil.uploadJsonBean(ADD_MATERIAL, params, null, file, AddMeterialResponse.class);
		if(addMeterialResponse.SUCCESS_CODE.equals(addMeterialResponse.getErrcode())) {
			return addMeterialResponse;
		}else {
			throw new GlobalException(new WeChatExceptionInfo(addMeterialResponse.getErrcode(),addMeterialResponse.getErrmsg()));
		}
	}
	
}
