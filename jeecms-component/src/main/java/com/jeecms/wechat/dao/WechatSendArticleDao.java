/**
** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.wechat.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.domain.WechatSendArticle;


/**
 * Dao层
* @author ljw
* @version 1.0
* @date 2019-06-04
*/
public interface WechatSendArticleDao extends IBaseDao<WechatSendArticle, Integer> {
	
	/**
	 * 根据appid集合、时间范围查询文章数量
	 * @param appId     公众号appid
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return
	 */
	@Query("select bean from WechatSendArticle bean where 1 = 1 and bean.appId in ?1 and "
			+ "bean.createTime >=?2 and bean.createTime <=?3 and bean.hasDeleted = false")
	List<WechatSendArticle> selectArts(List<String> appId, Date startTime, Date endTime);
	
	/**
	 * 根据appid集合、时间范围查询文章数量
	 * @param msgDataId 群发ID
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return
	 */
	@Query("select bean from WechatSendArticle bean where 1 = 1 and bean.msgDataId in ?1 and "
			+ "bean.createTime >=?2 and bean.createTime <=?3 and bean.hasDeleted = false")
	List<WechatSendArticle> selectArtsBySend(List<String> msgDataId, Date startTime, Date endTime);
	
	/**
	 * 查询单个文章
	* @Title: findArticle 
	* @param msgDataId 群发ID
	* @param index 序号
	* @return
	 */
	WechatSendArticle findByMsgDataIdAndMsgDataIndex(String msgDataId, Integer index);
}
