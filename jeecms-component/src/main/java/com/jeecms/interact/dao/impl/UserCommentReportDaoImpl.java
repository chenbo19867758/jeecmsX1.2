package com.jeecms.interact.dao.impl;

import java.util.List;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.interact.dao.ext.UserCommentReportDaoExt;
import com.jeecms.interact.domain.UserCommentReport;
import com.jeecms.interact.domain.querydsl.QUserCommentReport;
import com.querydsl.core.BooleanBuilder;

/**
 * 评论举报dao实现类
 * @author: chenming
 * @date:   2019年9月19日 下午2:50:15
 */
public class UserCommentReportDaoImpl extends BaseDao<UserCommentReport> implements UserCommentReportDaoExt {

	@Override
	public List<Integer> findByDeleted() {
		QUserCommentReport commentReport = QUserCommentReport.userCommentReport;
		BooleanBuilder exp = append();
		return getJpaQueryFactory().select(commentReport.commentId).from(commentReport).where(exp).fetch();
	}
	
	private BooleanBuilder append() {
		BooleanBuilder exp = new BooleanBuilder();
		QUserCommentReport commentReport = QUserCommentReport.userCommentReport;
		exp.and(commentReport.hasDeleted.eq(false));
		return exp;
	}

}
