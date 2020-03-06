/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.wechat.service;

import com.jeecms.wechat.domain.WechatFansExt;

import java.util.List;

import com.jeecms.common.base.service.IBaseService;

/**
 * 粉丝扩展Service
* @author ljw
* @version 1.0
* @date 2019-05-29
*/
public interface WechatFansExtService extends IBaseService<WechatFansExt, Integer> {

	/**
	 * 根据粉丝openIds删除
	* @Title: deleteAllFansExt 
	* @param openids 粉丝openIDs
	 */
	void deleteAllFansExt(List<String> openids);
}
