package com.jeecms.common.wechat.api.mp;

import java.io.File;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.AddMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.AddNewsRequest;
import com.jeecms.common.wechat.bean.request.mp.material.AddVideoMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.UploadImgRequest;
import com.jeecms.common.wechat.bean.response.mp.material.AddMeterialResponse;
import com.jeecms.common.wechat.bean.response.mp.material.AddNewsResponse;
import com.jeecms.common.wechat.bean.response.mp.material.UploadImgResponse;

/**
 * @Description: 新增永久素材
 * @author: chenming
 * @date:   2018年7月31日 下午8:44:47     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface AddMaterialApiService {
	
	/**
	 * 新增永久图文素材
	 * @Title: addNews  
	 * @param addNewsRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: AddNewsResponse
	 */
	AddNewsResponse addNews(AddNewsRequest addNewsRequest,ValidateToken validateToken)throws GlobalException;
	
	/**
	 * 上传图文消息内的图片获取URL
	 * @Title: uploadImg  
	 * @param uploadImgRequest
	 * @param validateToken
	 * @param file
	 * @return
	 * @throws GlobalException      
	 * @return: UploadImgResponse
	 */
	UploadImgResponse uploadImg(UploadImgRequest uploadImgRequest,ValidateToken validateToken,File file)throws GlobalException;
	
	/**
	 * 新增其它永久素材，如图片（image）、语音（voice）、和缩略图（thumb）
	 * @Title: addMaterial  
	 * @param addMaterialRequest
	 * @param validateToken
	 * @param file
	 * @return
	 * @throws GlobalException      
	 * @return: AddMeterialResponse
	 */
	AddMeterialResponse addMaterial(AddMaterialRequest addMaterialRequest,ValidateToken validateToken,File file)throws GlobalException;
	
	/**
	 * 新增永久视频素材
	 * @Title: addVideoMaterial  
	 * @param addVideoMaterialRequest
	 * @param validateToken
	 * @param file
	 * @return
	 * @throws GlobalException      
	 * @return: AddMeterialResponse
	 */
	AddMeterialResponse addVideoMaterial(AddVideoMaterialRequest addVideoMaterialRequest,ValidateToken validateToken,File file)throws GlobalException;
}	
