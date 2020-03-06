/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.dao.impl;

import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.questionnaire.constants.QuestionnaireConstant;
import com.jeecms.questionnaire.dao.ext.SysQuestionnaireAnswerDaoExt;
import com.jeecms.questionnaire.domain.SysQuestionnaireAnswer;
import com.jeecms.questionnaire.domain.querydsl.QSysQuestionnaireAnswer;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

import static com.jeecms.questionnaire.domain.SysQuestionnaireAnswer.COMPUTER;
import static com.jeecms.questionnaire.domain.SysQuestionnaireAnswer.MOBILE;

/**
 * 问卷结果Dao实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/10/30 14:21
 */
public class SysQuestionnaireAnswerDaoImpl implements SysQuestionnaireAnswerDaoExt {
	@Override
	public Page<SysQuestionnaireAnswer> getPage(Boolean isEffective, String province, String city,
												String device, Integer deviceType, String ip,
												Integer subjectId, String replayName,
												Date beginTime, Date endTime, Integer siteId,
												Integer questionnaireId, Pageable pageable) {
		JPAQuery<SysQuestionnaireAnswer> query = new JPAQuery<SysQuestionnaireAnswer>(this.em);
		QSysQuestionnaireAnswer data = QSysQuestionnaireAnswer.sysQuestionnaireAnswer;
		query = queryAppend(query, data, isEffective, province, city, device, deviceType, ip, subjectId, replayName,
			beginTime, endTime, true, siteId, questionnaireId);
		return QuerydslUtils.page(query, pageable, data);
	}

	@Override
	public List<SysQuestionnaireAnswer> getList(Boolean isEffective, String province, String city, String device,
												Integer deviceType, String ip, Integer subjectId, String replayName,
												Date beginTime, Date endTime,
												Integer orderBy, Integer siteId, Integer questionnaireId) {
		JPAQuery<SysQuestionnaireAnswer> query = new JPAQuery<SysQuestionnaireAnswer>(this.em);
		QSysQuestionnaireAnswer data = QSysQuestionnaireAnswer.sysQuestionnaireAnswer;
		query = queryAppend(query, data, isEffective, province, city, device, deviceType, ip, subjectId, replayName,
			beginTime, endTime, true, siteId, questionnaireId);
		return query.orderBy(data.createTime.desc()).fetch();
	}

	@Override
	public List<SysQuestionnaireAnswer> find(List<Date> dates, List<String> ips, List<String> devices) {
		JPAQuery<SysQuestionnaireAnswer> query = new JPAQuery<SysQuestionnaireAnswer>(this.em);
		QSysQuestionnaireAnswer data = QSysQuestionnaireAnswer.sysQuestionnaireAnswer;
		BooleanBuilder builder = new BooleanBuilder();
		if (dates != null && !dates.isEmpty()) {
			builder.and(data.createTime.in(dates));
		}
		if (ips != null && !ips.isEmpty()) {
			builder.and(data.ip.in(ips));
		}
		if (devices != null && !devices.isEmpty()) {
			builder.and(data.device.in(devices));
		}
		query.from(data).where(builder);
		query.orderBy(data.subject.index.asc());
		return query.orderBy(data.createTime.desc()).fetch();
	}

	@Override
	public List<SysQuestionnaireAnswer> getList(Integer subjectId, Integer siteId, Integer questionnaireId) {
		JPAQuery<SysQuestionnaireAnswer> query = new JPAQuery<SysQuestionnaireAnswer>(this.em);
		QSysQuestionnaireAnswer data = QSysQuestionnaireAnswer.sysQuestionnaireAnswer;
		query = queryAppend(query, data, true, null, null, null, null, null,
			subjectId, null, null, null, true, siteId, questionnaireId);
		return query.orderBy(data.createTime.desc()).fetch();
	}

	@Override
	public List<SysQuestionnaireAnswer> findByQuestionnaireId(Integer questionnaireId, Boolean showFile, Integer siteId) {
		JPAQuery<SysQuestionnaireAnswer> query = new JPAQuery<SysQuestionnaireAnswer>(this.em);
		QSysQuestionnaireAnswer data = QSysQuestionnaireAnswer.sysQuestionnaireAnswer;
		query = queryAppend(query, data, true, null, null, null, null, null,
			null, null, null, null, showFile, siteId, questionnaireId);
		query.orderBy(data.subject.index.asc());
		return query.orderBy(data.createTime.desc()).fetch();
	}

	@Override
	public List<SysQuestionnaireAnswer> findByReplayNameAndQuestionnaireId(String replayName, Date createTime, Integer questIntegerId) {
		JPAQuery<SysQuestionnaireAnswer> query = new JPAQuery<SysQuestionnaireAnswer>(this.em);
		QSysQuestionnaireAnswer data = QSysQuestionnaireAnswer.sysQuestionnaireAnswer;
		BooleanBuilder builder = new BooleanBuilder();
		if (StringUtils.isNotBlank(replayName)) {
			builder.and(data.replayName.eq(replayName));
		}
		if (questIntegerId != null) {
			builder.and(data.questionnaireId.eq(questIntegerId));
		}
		if (createTime != null) {
			builder.and(data.createTime.eq(createTime));
		}
		query.from(data).where(builder);
		query.orderBy(data.subject.index.asc());
		return query.fetch();
	}

	@Override
	public Page<SysQuestionnaireAnswer> findBySubjectId(Integer subjectId, Integer questionnaireId, Pageable pageable) {
		QSysQuestionnaireAnswer data = QSysQuestionnaireAnswer.sysQuestionnaireAnswer;
		JPAQuery<SysQuestionnaireAnswer> query = new JPAQuery<SysQuestionnaireAnswer>(this.em);
		BooleanBuilder builder = new BooleanBuilder();
		if (subjectId != null) {
			builder.and(data.subjectId.eq(subjectId));
		}
		if (questionnaireId != null) {
			builder.and(data.questionnaireId.eq(questionnaireId));
		}
		query.from(data).where(builder);
		query.orderBy(data.subject.index.asc());
		return QuerydslUtils.page(query, pageable, data);
	}

	private JPAQuery<SysQuestionnaireAnswer> queryAppend(JPAQuery<SysQuestionnaireAnswer> query, QSysQuestionnaireAnswer data,
														 Boolean isEffective, String province, String city, String device, Integer deviceType,
														 String ip, Integer subjectId, String replayName, Date beginTime,
														 Date endTime, Boolean showFile, Integer siteId, Integer questionnaireId) {
		BooleanBuilder builder = new BooleanBuilder();
		if (StringUtils.isNotBlank(replayName)) {
			builder.and(data.replayName.like("%" + replayName + "%"));
		}
		if (StringUtils.isNotBlank(device)) {
			builder.and(data.device.eq(device));
		}
		if (deviceType != null) {
			if (deviceType.equals(COMPUTER) || deviceType.equals(MOBILE)) {
				builder.and(data.deviceType.eq(deviceType));
			}
		}
		if (StringUtils.isNotBlank(province)) {
			builder.and(data.province.eq(province));
		}
		if (StringUtils.isNotBlank(city)) {
			builder.and(data.city.eq(city));
		}
		if (ip != null) {
			builder.and(data.ip.like("%" + ip + "%"));
		}
		if (beginTime != null) {
			builder.and(data.createTime.goe(beginTime));
		}
		if (endTime != null) {
			builder.and(data.createTime.loe(endTime));
		}
		if (isEffective != null) {
			builder.and(data.isEffective.eq(isEffective));
		}
		if (questionnaireId != null) {
			builder.and(data.questionnaireId.eq(questionnaireId));
		}
		if (subjectId != null) {
			builder.and(data.subjectId.eq(subjectId));
		}
		//showFile为空或者为false则显示
		if (showFile == null || !showFile) {
			builder.and(data.subject.type.ne(QuestionnaireConstant.SUBJECT_TYPE_FILE));
		}
		builder.and(data.hasDeleted.isFalse());
		builder.and(data.questionnaire.siteId.eq(siteId));
		return query.from(data).where(builder);
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
