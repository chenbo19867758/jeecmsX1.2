/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.dao.impl;

import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.questionnaire.constants.QuestionnaireConstant;
import com.jeecms.questionnaire.dao.ext.SysQuestionnaireDaoExt;
import com.jeecms.questionnaire.domain.SysQuestionnaire;
import com.jeecms.questionnaire.domain.querydsl.QSysQuestionnaire;
import com.jeecms.questionnaire.domain.vo.QuestionnaireFrontListVo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/23 10:11
 */
public class SysQuestionnaireDaoImpl implements SysQuestionnaireDaoExt {
	@Override
	public Page<SysQuestionnaire> page(String title, Integer status, Date beginTime, Date endTime, Integer siteId, Pageable pageable) {
		JPAQuery<SysQuestionnaire> query = new JPAQuery<SysQuestionnaire>(this.em);
		QSysQuestionnaire data = QSysQuestionnaire.sysQuestionnaire;
		BooleanBuilder builder = new BooleanBuilder();
		if (StringUtils.isNotBlank(title)) {
			builder.and(data.title.like("%" + title + "%"));
		}
		if (status != null) {
			builder.and(data.status.eq(status));
		}
		if (beginTime != null) {
			builder.and(data.questionnaireConfig.createTime.goe(beginTime));
		}
		if (endTime != null) {
			builder.and(data.questionnaireConfig.createTime.loe(endTime));
		}
		if (siteId != null) {
			builder.and(data.siteId.eq(siteId));
		}
		builder.and(data.hasDeleted.isFalse());
		query.from(data).where(builder);
		return QuerydslUtils.page(query, pageable, data);
	}

	private EntityManager em;

	@Override
	public Page<QuestionnaireFrontListVo> page(Integer siteId, Integer orderBy, Pageable pageable) {
		QSysQuestionnaire data = QSysQuestionnaire.sysQuestionnaire;
		BooleanBuilder builder = new BooleanBuilder();
		JPAQuery<QuestionnaireFrontListVo> query = new JPAQuery<QuestionnaireFrontListVo>(this.em);
		//进行中或已结束
		builder.and(data.status.eq(QuestionnaireConstant.STATUS_PROCESSING))
			.or(data.status.eq(QuestionnaireConstant.STATUS_OVER));
		if (siteId != null) {
			builder.and(data.siteId.eq(siteId));
		}
		builder.and(data.hasDeleted.isFalse());
		query.from(data).where(builder);
		orderBy(query, builder, data, orderBy);
		return QuerydslUtils.page(query, pageable, null);
	}

	@Override
	public List<QuestionnaireFrontListVo> list(Integer siteId, Integer orderBy, int count) {
		JPAQuery<QuestionnaireFrontListVo> query = new JPAQuery<QuestionnaireFrontListVo>(this.em);
		QSysQuestionnaire data = QSysQuestionnaire.sysQuestionnaire;
		BooleanBuilder builder = new BooleanBuilder();
		//进行中或已结束
		builder.and(data.status.eq(QuestionnaireConstant.STATUS_PROCESSING))
			.or(data.status.eq(QuestionnaireConstant.STATUS_OVER));
		if (siteId != null) {
			builder.and(data.siteId.eq(siteId));
		}
		builder.and(data.hasDeleted.isFalse());
		query.from(data).where(builder).limit(count);
		orderBy(query, builder, data, orderBy);
		return query.fetch();
	}

	public void orderBy (JPAQuery<QuestionnaireFrontListVo> query, BooleanBuilder builder,	QSysQuestionnaire data, Integer orderBy) {
		if (orderBy == null) {
			query.orderBy(data.questionnaireConfig.beginTime.desc());
		} else if (QuestionnaireConstant.PUBLISH_DESC.equals(orderBy)) {
			query.orderBy(data.questionnaireConfig.beginTime.desc());
		} else if (QuestionnaireConstant.PUBLISH_ASC.equals(orderBy)) {
			query.orderBy(data.questionnaireConfig.beginTime.asc());
		} else if (QuestionnaireConstant.CREATE_TIME_DESC.equals(orderBy)) {
			query.orderBy(data.createTime.desc());
		} else if (QuestionnaireConstant.CREATE_TIME_ASC.equals(orderBy)) {
			query.orderBy(data.createTime.asc());
		}else if (QuestionnaireConstant.END_TIME_DESC.equals(orderBy)) {
			query.orderBy(data.questionnaireConfig.endTime.desc());
		}else if (QuestionnaireConstant.END_TIME_ASC.equals(orderBy)) {
			query.orderBy(data.questionnaireConfig.endTime.asc());
		}
	}

	@Override
	public List<SysQuestionnaire> findByWorkflowId(Integer workfolwId, Integer siteId) {
		JPAQuery<SysQuestionnaire> query = new JPAQuery<SysQuestionnaire>(this.em);
		QSysQuestionnaire data = QSysQuestionnaire.sysQuestionnaire;
		BooleanBuilder builder = new BooleanBuilder();
		//进行中或已结束
		if (siteId != null) {
			builder.and(data.siteId.eq(siteId));
		}
		if (workfolwId != null) {
			builder.and(data.questionnaireConfig.workflowId.eq(workfolwId));
		}
		builder.and(data.hasDeleted.isFalse());
		query.from(data).where(builder);
		return query.fetch();
	}

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
