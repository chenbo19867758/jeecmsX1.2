package com.jeecms.common.wechat.api.applet;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.applet.BindTesterRequest;
import com.jeecms.common.wechat.bean.request.applet.UnbindTesterRequest;
import com.jeecms.common.wechat.bean.response.applet.BindTesterResponse;
import com.jeecms.common.wechat.bean.response.applet.UnbindTesterResponse;

/**
 * 
 * @Description: 设置微信用户为小程序体验者service接口
 * @author: chenming
 * @date:   2018年10月31日 下午2:32:24     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface MemberTesterApiService {
	
	/**
	 * 绑定微信用户为小程序体验者
	 * @Title: bindTester  
	 * @param request
	 * @param validateToken
	 * @return
	 * @throws GlobalException      
	 * @return: BindTesterResponse
	 */
	BindTesterResponse bindTester(BindTesterRequest request,ValidateToken validateToken)throws GlobalException;
	
	/**
	 * 解绑微信用户为小程序体验者
	 * @Title: unbindTester  
	 * @param request
	 * @param validateToke
	 * @return
	 * @throws GlobalException      
	 * @return: UnbindTesterResponse
	 */
	UnbindTesterResponse unbindTester(UnbindTesterRequest request,ValidateToken validateToke) throws GlobalException;
}
