package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.GetInterimMaterialRequest;

/**
 * 获取临时素材api的service接口
 * @author: chenming
 * @date:   2019年6月4日 下午5:26:38
 */
public interface GetInterimMaterialApiService {
	
	/**
	 * 获取视频素材
	 * @Title: getVideo  
	 * @param request	获取新的request对象
	 * @param validateToken		token对象
	 * @throws GlobalException     全局异常 
	 * @return: GetInterimVideoResponse
	 */
	MediaFile getVideo(GetInterimMaterialRequest request,ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 获取其它临时素材
	 * @Title: getMaterial  
	 * @param request	获取的request对象
	 * @param validateToken	token对象
	 * @throws GlobalException      全局异常
	 * @return: MediaFile
	 */
	MediaFile getMaterial(GetInterimMaterialRequest request,ValidateToken validateToken)throws GlobalException;
}
