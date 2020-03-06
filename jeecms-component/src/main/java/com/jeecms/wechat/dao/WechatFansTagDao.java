/**
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.domain.WechatFansTag;


/**
 * 微信粉丝标签Dao
* @author ljw
* @version 1.0
* @date 2018-08-03
*/
public interface WechatFansTagDao extends IBaseDao<WechatFansTag, Integer> {

	/**
	 * 根据appid删除全部标签
	 * @param appid 公众号ID
	 */
	@Query("delete from WechatFansTag bean where 1 = 1 and bean.appId=?1")
	@Modifying
	void deleteTags(String appid);
	
	/**
	 * 根据appid和tagid删除标签
	 * @param appid 公众号ID
	 * @param id 标签ID
	 */
	@Query("delete from WechatFansTag bean where 1 = 1 and bean.appId=?1 and bean.id=?2")
	@Modifying
	void deleteTags(String appid,Integer id);
	
	/**
	 * 根据appid查询粉丝标签列表
	 * @param appid 公众号ID
	 * @return
	 */
	@Query("select bean from WechatFansTag bean where 1 = 1 and bean.appId=?1")
	List<WechatFansTag> selectTags(String appid);
	
	/**
	 * 根据appid以及tagid查询粉丝标签列表
	 * @param appid 公众号ID
	 * @param id 标签ID
	 * @return 
	 */
	@Query("select bean from WechatFansTag bean where 1 = 1 and bean.appId=?1 and bean.id=?2")
	WechatFansTag selectTags(String appid,Integer id);
	
}
