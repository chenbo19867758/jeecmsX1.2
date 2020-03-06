/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao.impl;

import com.jeecms.system.dao.ext.StatisticsLogResultDaoExt;
import com.jeecms.system.domain.StatisticsLogResult;
import com.jeecms.system.domain.querydsl.QStatisticsLogResult;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;


/**
 * 日志请求结果统计扩展Dao实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-19
 */
public class StatisticsLogResultDaoImpl implements StatisticsLogResultDaoExt {

	@Override
	public List<StatisticsLogResult> getList(Date startTime, Date endTime) {
		JPAQuery<StatisticsLogResult> query = new JPAQuery<StatisticsLogResult>(this.em);
		QStatisticsLogResult logResult = QStatisticsLogResult.statisticsLogResult;
		query.from(logResult);
		BooleanBuilder exp = new BooleanBuilder();
		exp.and(logResult.hasDeleted.eq(false));
		if (startTime != null && endTime != null) {
			exp.and(logResult.createTime.between(startTime, endTime));
		}
		return query.fetch();
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

}
