package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.UpdateNewsRequest;
import com.jeecms.common.wechat.bean.response.mp.material.UpdateNewsResponse;

/**
 * 
 * @Description: 修改永久图文素材
 * @author: chenming
 * @date:   2018年8月2日 下午1:50:32     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface UpdateNewsApiService {
	
	/**
	 * 修改永久图文素材
	 * @Title: updateNews  
	 * @param updateNewsRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: UpdateNewsResponse
	 */
	UpdateNewsResponse updateNews(UpdateNewsRequest updateNewsRequest,ValidateToken validateToken)throws GlobalException;
}
