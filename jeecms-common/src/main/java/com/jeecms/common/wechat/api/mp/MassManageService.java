package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.mass.MassRequest;
import com.jeecms.common.wechat.bean.response.mp.user.MassResponse;

/**
 * @Description:微信群发接口
 * @author: ljw
 * @date: 2018年7月30日 下午4:15:46
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface MassManageService {

	/**
	 * 发送信息
	 * 
	 * @param massRequest
	 * @param validToken
	 * @return
	 * @throws GlobalException
	 */
	MassResponse sendMass(MassRequest massRequest, ValidateToken validToken) throws GlobalException;
}
