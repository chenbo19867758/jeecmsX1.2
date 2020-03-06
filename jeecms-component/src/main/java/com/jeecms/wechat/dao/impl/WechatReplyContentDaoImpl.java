package com.jeecms.wechat.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.wechat.dao.ext.WechatReplyContentDaoExt;
import com.jeecms.wechat.domain.WechatReplyContent;
import com.jeecms.wechat.domain.querydsl.QWechatReplyContent;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

public class WechatReplyContentDaoImpl extends BaseDao<WechatReplyContent> implements WechatReplyContentDaoExt {

	@Override
	public Page<WechatReplyContent> getPage(String appId,List<Integer> ids,Pageable pageable,String searchStr) {
		QWechatReplyContent content = QWechatReplyContent.wechatReplyContent;
		BooleanBuilder exp = new BooleanBuilder();
		exp.and(content.hasDeleted.eq(false));
		exp.and(content.id.notIn(ids));
		// 此处appId无需再此处判断，在之前判断
		exp.and(content.appId.eq(appId));
		if (StringUtils.isNotBlank(searchStr)) {
			exp.and(content.ruleName.like("%" + searchStr +"%")
					.or(content.wechatReplyKeywordList.any().wordkeyEq.like("%"+ searchStr+"%"))
					.or(content.wechatReplyKeywordList.any().wordkeyLike.like("%"+ searchStr+"%")));
		}
		JPAQuery<WechatReplyContent> query = getJpaQueryFactory().selectFrom(content);
		query.where(exp).orderBy(content.createTime.asc());
		return QuerydslUtils.page(query, pageable, content);
	}

}
