/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.wechat.dao.ext.WechatFansStatisticsDaoExt;
import com.jeecms.wechat.domain.WechatFansStatistics;

/**
 * 用户数据统计dao层
 * @author ljw
 * @date 2019年05月30日
 */
public interface WechatFansStatisticsDao extends IBaseDao<WechatFansStatistics, Integer>,
				WechatFansStatisticsDaoExt {

	/**
	 * 根据appid、时间范围查询微信统计粉丝信息
	 * @param appIds 公众号IDs
	 * @param startTime 开始时间
	 * @param endTime  结束时间
	 * @return
	 */
	@Query("select bean from WechatFansStatistics bean where 1 = 1 and bean.appId in?1 "
			+ "and bean.hasDeleted=false and "
			+ " statisticsDate BETWEEN ?2 AND ?3 order by statisticsDate")
	List<WechatFansStatistics> selectFans(List<String> appIds, Date startTime, Date endTime);
	
	/**
	 * 根据时间范围查询微信统计粉丝信息
	 * @param startTime 开始时间
	 * @param endTime  结束时间
	 * @return
	 */
	@Query("select bean from WechatFansStatistics bean where 1 = 1 and bean.hasDeleted=false and "
			+ " statisticsDate BETWEEN ?1 AND ?2 order by statisticsDate")
	List<WechatFansStatistics> selectFansDate(Date startTime, Date endTime);
}
