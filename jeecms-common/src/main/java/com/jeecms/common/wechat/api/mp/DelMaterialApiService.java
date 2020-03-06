package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.DelMaterialRequest;
import com.jeecms.common.wechat.bean.response.mp.material.DelMateralResponse;

/**
 * 
 * @Description: 删除素材
 * @author: chenming
 * @date:   2018年8月2日 下午1:53:16     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface DelMaterialApiService {
	
	/**
	 * 删除素材
	 * @Title: delMaterial  
	 * @param delMaterialRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: DelMateralResponse
	 */
	DelMateralResponse delMaterial(DelMaterialRequest delMaterialRequest,ValidateToken validateToken)throws GlobalException;
}
