/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.weibo.domain.WeiboFans;

/**
 *微博粉丝 
* @author ljw
* @version 1.0
* @date 2019-06-18
*/
public interface WeiboFansService extends IBaseService<WeiboFans, Integer> {


	/**
	 * 同步粉丝
	* @Title: sync 
	* @param id 微博账户ID
	* @return ResponseInfo
	* @throws GlobalException 异常
	 */
	ResponseInfo sync(Integer id) throws GlobalException;
	
	/**
	 * 根据uid删除粉丝
	* @Title: deleteFans 
	* @param uid 微博账户Uid
	* @throws GlobalException 异常
	 */
	void deleteFans(Long uid) throws GlobalException;
}
