/**
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.dao.ext.WechatFansSendLogDaoExt;
import com.jeecms.wechat.domain.WechatFansSendLog;

/**
 * 粉丝推送Dao
* @author ljw
* @version 1.0
* @date 2018-08-09
*/
public interface WechatFansSendLogDao extends IBaseDao<WechatFansSendLog, Integer>, WechatFansSendLogDaoExt {

	/**
	 * 根据留言获取留言数
	* @Title: findByOpenid 
	* @param openid 粉丝OPENID
	* @return
	 */
	List<WechatFansSendLog> findByOpenIdAndHasDeletedFalse(String openid);
}
