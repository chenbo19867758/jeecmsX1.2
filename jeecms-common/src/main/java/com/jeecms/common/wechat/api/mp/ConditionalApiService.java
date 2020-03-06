package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.menu.AddConditionalRequest;
import com.jeecms.common.wechat.bean.request.mp.menu.DelConditionalRequest;
import com.jeecms.common.wechat.bean.request.mp.menu.TrymatchRequest;
import com.jeecms.common.wechat.bean.response.mp.menu.AddConditionalResponse;
import com.jeecms.common.wechat.bean.response.mp.menu.DeleteMenuResponse;
import com.jeecms.common.wechat.bean.response.mp.menu.GetConditionalResponse;
import com.jeecms.common.wechat.bean.response.mp.menu.TrymatchResponse;

/**
 * 
 * @Description: 个性化菜单管理
 * @author: chenming
 * @date:   2018年8月8日 下午7:59:28     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface ConditionalApiService {
	
	/**
	 * 增加一个个性化菜单
	 * @Title: addConditional  
	 * @param aConditionalRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: AddConditionalResponse
	 */
	AddConditionalResponse addConditional(AddConditionalRequest aConditionalRequest,ValidateToken validateToken)throws GlobalException;
	
	/**
	 * 删除一个个性化菜单
	 * @Title: delConditional  
	 * @param dRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: DeleteMenuResponse
	 */
	DeleteMenuResponse delConditional(DelConditionalRequest dRequest,ValidateToken validateToken)throws GlobalException;
	
	/**
	 * 测试个性化菜单匹配结果
	 * @Title: trymatch  
	 * @param trymatchRequest
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: TrymatchResponse
	 */
	TrymatchResponse trymatch(TrymatchRequest trymatchRequest,ValidateToken validateToken)throws GlobalException;
	
	/**
	 * 查询自定义菜单(拥有个性化菜单)
	 * @Title: getConditional  
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetConditionalResponse
	 */
	GetConditionalResponse getConditional(ValidateToken validateToken)throws GlobalException;
}
