/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.wechat.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.dao.ext.WechatCommentDaoExt;
import com.jeecms.wechat.domain.WechatComment;

/**
 * 微信留言Dao
* @author ljw
* @version 1.0
* @date 2019-05-31
*/
public interface WechatCommentDao extends IBaseDao<WechatComment, Integer>, WechatCommentDaoExt {

	/**
	 * 根据群发ID删除评论数据
	 * 
	 * @Title: deleteModelItems
	 * @param msgDataIds 群发ids
	 * @return: void
	 */
	@Query("delete from WechatComment bean where 1 = 1 and bean.msgDataId in?1")
	@Modifying
	void deleteComments(List<String> msgDataIds);
	
	/**
	 * 根据留言获取留言数
	* @Title: findByOpenid 
	* @param openid 粉丝OPENID
	* @return
	 */
	List<WechatComment> findByOpenidAndHasDeletedFalse(String openid);
}
