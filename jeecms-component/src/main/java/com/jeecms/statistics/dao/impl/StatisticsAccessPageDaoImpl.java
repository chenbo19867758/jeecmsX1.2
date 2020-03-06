/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.dao.impl;

import com.jeecms.common.util.MyDateUtils;
import com.jeecms.statistics.dao.ext.StatisticsAccessPageDaoExt;
import com.jeecms.statistics.domain.StatisticsAccessPage;
import com.jeecms.statistics.domain.querydsl.QStatisticsAccessPage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/5 9:39
 */

public class StatisticsAccessPageDaoImpl implements StatisticsAccessPageDaoExt {

	@Override
	public List<StatisticsAccessPage> getList(Date beginTime, Date endTime, Integer siteId, Integer sorceUrlType,
											  Boolean newVisitor, int urlType) {
		JPAQuery<StatisticsAccessPage> jpaQuery = new JPAQuery<StatisticsAccessPage>(this.em);
		QStatisticsAccessPage accessPage = QStatisticsAccessPage.statisticsAccessPage;
		BooleanBuilder exp = new BooleanBuilder();
		jpaQuery.from(accessPage);
		if (siteId != null) {
			exp.and(accessPage.siteId.eq(siteId));
		}
		if (beginTime != null) {
			exp.and(accessPage.statisticsDay.goe(MyDateUtils.formatDate(beginTime)));
		}
		if (endTime != null) {
			exp.and(accessPage.statisticsDay.loe(MyDateUtils.formatDate(endTime)));
		}
		exp.and(accessPage.urlType.eq(urlType));
		if (sorceUrlType != null) {
			exp.and(accessPage.sourceType.eq(sorceUrlType));
		}
		if (newVisitor != null) {
			exp.and(accessPage.newVisitor.eq(newVisitor));
		}
		return jpaQuery.where(exp).fetch();
	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
