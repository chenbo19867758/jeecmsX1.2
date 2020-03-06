/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.dao.ext.WechatFansDaoExt;
import com.jeecms.wechat.domain.WechatFans;

/**
 * 微信粉丝Dao
 * @author ljw
 * @version 1.0
 * @date 2018-08-02
 */
public interface WechatFansDao extends IBaseDao<WechatFans, Integer>, WechatFansDaoExt {

	/**
	 * 根据appid删除微信粉丝列表
	 * 
	 * @param appid 粉丝ID
	 */
	@Query("delete from WechatFans bean where 1 = 1 and bean.appId=?1")
	@Modifying
	void deleteAllFans(String appid);

	/**
	 * 根据openid查询微信粉丝
	 * 
	 * @param openid 粉丝ID
	 * @return
	 */
	List<WechatFans> findByOpenidIn(List<String> openid);
	
	/**
	 * 根据openid查询微信粉丝
	 * 
	 * @param openid 粉丝ID
	 * @return
	 */
	WechatFans findByOpenid(String openid);

	/**
	 * 根据appid、时间范围查询微信粉丝数量
	 * 
	 * @param appId     公众号appid
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return
	 */
	@Query("select count(*) from WechatFans bean where 1 = 1 and bean.appId=?1 and "
			+ "bean.subscribeTime>=?2 and bean.subscribeTime<=?3 and bean.hasDeleted=false")
	Integer selectFans(String appId, Long startTime, Long endTime);

	/**
	 * 查询用户性别比例
	 * 
	 * @Title: selectFansSex
	 * @param appId 公众号ID
	 * @author: wulongwei
	 * @date: 2018年8月24日 下午3:50:46
	 * @return: List 性别数据
	 */
	@Query("select sex,count(1) from WechatFans bean where 1 = 1 and bean.appId=?1 "
			+ "and bean.hasDeleted=false group by sex order by count(1) desc")
	List<String> selectFansSex(String appId);

	/**
	 * 查询用户关注渠道的数量
	 * 
	 * @Title: selectFansSubscribe
	 * @param appId 公众号ID
	 * @author: wulongwei
	 * @date: 2018年8月24日 下午3:51:07
	 * @return: List 渠道的数量
	 */
	@Query("select subscribeScene,count(1) from WechatFans bean where 1 = 1 and "
			+ "bean.appId=?1 and bean.hasDeleted=false group by subscribeScene order by count(1) desc")
	List<String> selectFansSubscribe(String appId);

	/**
	 * 查询用户所在国家的数量
	 * 
	 * @Title: selectFansCountry
	 * @param appId 公众号ID
	 * @author: wulongwei
	 * @date: 2018年8月24日 下午4:22:36
	 * @return: List 渠道的数量
	 */
	@Query("select country,count(1) from WechatFans bean where 1 = 1 and bean.appId=?1 "
			+ "and bean.hasDeleted=false group by country order by count(1) desc")
	List<String> selectFansCountry(String appId);

	/**
	 * 查询中国地区 各省 用户分布的数量
	 * 
	 * @Title: selectFansProvince
	 * @param appId 公众号ID
	 * @author: wulongwei
	 * @date: 2018年8月24日 下午4:23:46
	 * @return: List 渠道的数量
	 */
	@Query("select province,count(1) from WechatFans bean where 1 = 1 and bean.appId=?1 "
			+ "and country ='中国' and bean.hasDeleted=false group by province order by count(1) desc")
	List<String> selectFansProvince(String appId);

	/**
	 * 查询开通了会员的用户的数量
	 * 
	 * @Title: selectFansCount
	 * @param appId 公众号ID
	 * @author: wulongwei
	 * @date: 2018年8月27日 上午10:29:57
	 * @return: Integer 数量
	 */
	@Query("select count(*) from WechatFans bean where 1 = 1 and bean.appId=?1 and "
			+ "bean.hasDeleted=false and member_id is not null")
	Integer selectFansCount(String appId);

	/**
	 * 查询全部的用户的数量
	 * 
	 * @Title: selectFansSumCount
	 * @param appId 公众号ID
	 * @author: wulongwei
	 * @date: 2018年8月27日 上午10:30:31
	 * @return: Integer 数量
	 */
	@Query("select count(*) from WechatFans bean where 1 = 1 and bean.appId=?1 and bean.hasDeleted=false")
	Integer selectFansSumCount(String appId);
	
	/**
	 * 根据多个APPID 查询粉丝
	* @Title: findByAppIdIn 
	* @param appids 公众号IDs
	* @return
	 */
	List<WechatFans> findByAppIdIn(List<String> appids);
	
}
