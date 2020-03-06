/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */

package com.jeecms.wechat.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.wechat.dao.ext.WechatFansStatisticsDaoExt;
import com.jeecms.wechat.domain.WechatFansStatistics;
import com.jeecms.wechat.domain.querydsl.QWechatFansStatistics;
import com.jeecms.wechat.domain.vo.WechatStatisticsVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * 粉丝统计实现类
 * 
 * @author: ljw
 * @date: 2018年8月21日 上午11:05:40
 */
public class WechatFansStatisticsDaoImpl extends BaseDao<WechatFansStatistics> implements WechatFansStatisticsDaoExt {

	@Override
	public List<WechatStatisticsVO> groupFans(List<String> appIds, Date startTime, Date endTime) {
		List<WechatStatisticsVO> vos = new ArrayList<WechatStatisticsVO>(10);
		QWechatFansStatistics statistics = QWechatFansStatistics.wechatFansStatistics;
		BooleanBuilder builder = new BooleanBuilder();
		if (appIds.isEmpty()) {
			return vos;
		}
		builder.and(statistics.appId.in(appIds));
		if (startTime != null && endTime != null) {
			builder.and(statistics.statisticsDate.between(startTime,endTime));
		}
		JPAQuery<WechatStatisticsVO> query = 
				super.getJpaQueryFactory().select(
						Projections.bean(WechatStatisticsVO.class,
						statistics.cancelUser.sum().as("cancelUser"), 
						statistics.countUser.sum().as("countUser"),
						statistics.newUser.sum().as("newUser"),
						statistics.netGrowthUser.sum().as("netGrowthUser"),
						statistics.statisticsDate.as("statisticsDate")))
				.from(statistics);
		query.where(builder).groupBy(statistics.statisticsDate);;
		return query.fetch();
	}

	
}
