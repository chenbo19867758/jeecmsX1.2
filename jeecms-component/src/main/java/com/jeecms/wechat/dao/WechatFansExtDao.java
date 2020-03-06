/**
 ** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 
 */

package com.jeecms.wechat.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.domain.WechatFansExt;


/**
 * 微信粉丝扩展Dao
* @author ljw
* @version 1.0
* @date 2019-05-29
*/
public interface WechatFansExtDao extends IBaseDao<WechatFansExt, Integer> {

	/**
	 * 根据openId删除微信粉丝扩展列表
	 * 
	 * @param openids 粉丝OpenIDs
	 */
	@Query("delete from WechatFansExt bean where 1 = 1 and bean.openid in?1")
	@Modifying
	void deleteAllFansExt(List<String> openids);
}
