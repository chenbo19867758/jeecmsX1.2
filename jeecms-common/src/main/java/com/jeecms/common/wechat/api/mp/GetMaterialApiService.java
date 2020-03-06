package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.GetMaterialRequest;
import com.jeecms.common.wechat.bean.response.mp.material.GetNewsResponse;
import com.jeecms.common.wechat.bean.response.mp.material.GetVideoResponse;

/**
 * 
 * @Description: 获取素材
 * @author: chenming
 * @date:   2018年8月2日 下午1:53:27     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface GetMaterialApiService {
	
	/**
	 * 获取图文素材
	 * @Title: getNews  
	 * @param getMaterialRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetNewsResponse
	 */
	GetNewsResponse getNews(GetMaterialRequest getMaterialRequest,ValidateToken validateToken)throws GlobalException;

	/**
	 * 获取视频素材
	 * @Title: getVideo  
	 * @param getMaterialRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetVideoResponse
	 */
	GetVideoResponse getVideo(GetMaterialRequest getMaterialRequest,ValidateToken validateToken)throws GlobalException;
	
	/**
	 * 获取其它素材
	 * @Title: getMaterial  
	 * @param getMaterialRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: byte[]
	 */
	MediaFile getMaterial(GetMaterialRequest getMaterialRequest,ValidateToken validateToken)throws GlobalException;
}
