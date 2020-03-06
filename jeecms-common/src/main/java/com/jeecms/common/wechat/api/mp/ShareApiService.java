package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.response.mp.share.GetTicketResponse;

/**
 * 微信分享api接口
 * @author: chenming
 * @date:   2019年10月31日 上午10:57:37
 */
public interface ShareApiService {
	
	/**
	 * 
	 * @Title: getTicket  
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: GetTicketResponse
	 */
	GetTicketResponse getTicket(ValidateToken validateToken) throws GlobalException;
}
