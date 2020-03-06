package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.BatchgetMaterialRequest;
import com.jeecms.common.wechat.bean.response.mp.material.BatchgetMaterialResponse;
import com.jeecms.common.wechat.bean.response.mp.material.BatchgetNewsResponse;

/**
 * 
 * @Description: 获取素材列表
 * @author: chenming
 * @date:   2018年8月2日 下午2:52:00     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface BatchgetMaterialApiService {
	
	/**
	 * 获取永久素材列表
	 * @Title: batchgetMaterial  
	 * @param batchgetMaterialRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: BatchgetMaterialResponse
	 */
	BatchgetMaterialResponse batchgetMaterial(BatchgetMaterialRequest batchgetMaterialRequest,ValidateToken validateToken)throws GlobalException;

	/**
	 * 获取永久图文素材列表
	 * @Title: batchgetNews  
	 * @param batchgetMaterialRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: BatchgetNewsResponse
	 */
	BatchgetNewsResponse batchgetNews(BatchgetMaterialRequest batchgetMaterialRequest,ValidateToken validateToken)throws GlobalException;

}
