package com.jeecms.content.dao.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.content.dao.ext.ContentRecordDaoExt;
import com.jeecms.content.domain.ContentRecord;
import com.jeecms.content.domain.querydsl.QContentRecord;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * 内容操作记录dao实现类
 * @author: chenming
 * @date:   2019年6月24日 下午3:22:29
 */
public class ContentRecordDaoImpl extends BaseDao<ContentRecord> implements ContentRecordDaoExt {

	@Override
	public Page<ContentRecord> getPage(Integer contentId, Date startTime, Date endTime, String userName,
			Pageable pageable) {
		QContentRecord contentRecord = QContentRecord.contentRecord;
		BooleanBuilder exp = new BooleanBuilder();
		exp.and(contentRecord.hasDeleted.eq(false));
		if (contentId != null) {
			exp.and(contentRecord.contentId.eq(contentId));
		}
		if (startTime != null) {
			exp.and(contentRecord.createTime.goe(startTime));
		}
		if (endTime != null) {
			exp.and(contentRecord.createTime.loe(endTime));
		}
		if (StringUtils.isNotBlank(userName)) {
			exp.and(contentRecord.userName.like("%" + userName + "%"));
		}
		JPAQuery<ContentRecord> query = getJpaQueryFactory().selectFrom(contentRecord);
		query.where(exp).orderBy(contentRecord.createTime.desc());
		return QuerydslUtils.page(query, pageable, contentRecord);
	}
	
	
}	
