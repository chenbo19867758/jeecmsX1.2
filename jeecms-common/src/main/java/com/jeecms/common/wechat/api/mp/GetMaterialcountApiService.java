package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.GetMaterialcountRequest;
import com.jeecms.common.wechat.bean.response.mp.material.GetMaterialcountResponse;

/**
 * 
 * @Description: 获取素材总数
 * @author: chenming
 * @date:   2018年8月2日 下午2:13:29     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface GetMaterialcountApiService {
	
	/**
	 * 获取素材总数
	 * @Title: getMaterialcount  
	 * @param getMaterialcountRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetMaterialcountResponse
	 */
	GetMaterialcountResponse getMaterialcount(GetMaterialcountRequest getMaterialcountRequest,ValidateToken validateToken)throws GlobalException;
}
