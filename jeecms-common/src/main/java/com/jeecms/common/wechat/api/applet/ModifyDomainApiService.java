package com.jeecms.common.wechat.api.applet;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.applet.ModifyDomainRequest;
import com.jeecms.common.wechat.bean.response.applet.ModifyDomainData;

/**
 * 
 * @author: tom
 * @date:   2019年3月8日 下午4:36:54
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface ModifyDomainApiService {
    
	/**
	 * 获取服务器域名
	 * @Title: getModifyDomain  
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: ModifyDomainData
	 * @author: wulongwei
     * @date: 2018年10月31日 下午5:29:17
	 */
	ModifyDomainData getModifyDomain(ValidateToken validateToken) throws GlobalException;
	
	/**
	 * 设置服务器域名
	 * @Title: setModifyDomain  
	 * @param validateToken
	 * @param request
	 * @return
	 * @throws GlobalException      
	 * @return: ModifyDomainData
	 * @author: wulongwei
	 * @date: 2018年10月31日 下午5:29:55
	 */
	ModifyDomainData setModifyDomain(ValidateToken validateToken , ModifyDomainRequest request) throws GlobalException;
}
