/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao.impl;

import com.jeecms.common.util.MyDateUtils;
import com.jeecms.system.dao.ext.StatisticsLogUserDaoExt;
import com.jeecms.system.domain.StatisticsLogUser;
import com.jeecms.system.domain.querydsl.QStatisticsLogUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 日志用户统计扩展Dao实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-19
 */
public class StatisticsLogUserDaoImpl implements StatisticsLogUserDaoExt {

	@Override
	public List statisticsList(Date startTime, Date endTime, Boolean isToday) {
		JPAQuery<StatisticsLogUser> query = new JPAQuery<StatisticsLogUser>(this.em);
		QStatisticsLogUser logUser = QStatisticsLogUser.statisticsLogUser;
		query.from(logUser);
		BooleanBuilder exp = new BooleanBuilder();
		exp.and(logUser.hasDeleted.eq(false));
		if (isToday != null && isToday) {
			Date date = Calendar.getInstance().getTime();
			endTime = MyDateUtils.getFinallyDate(date);
			startTime = MyDateUtils.getStartDate(date);
		}
		if (startTime != null) {
			exp.and(logUser.createTime.goe(startTime));
		}
		if (endTime != null) {
			exp.and(logUser.createTime.loe(endTime));
		}

		query.where(exp).select(logUser.operateUser, logUser.counts.sum())
				.groupBy(logUser.operateUser);
		return query.createQuery().getResultList();
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
