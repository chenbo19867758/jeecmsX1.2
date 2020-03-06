package com.jeecms.wechat.dao.impl;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.wechat.dao.ext.WechatReplyKeywordDaoExt;
import com.jeecms.wechat.domain.WechatReplyKeyword;
import com.jeecms.wechat.domain.querydsl.QWechatReplyContent;
import com.jeecms.wechat.domain.querydsl.QWechatReplyKeyword;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * 关键字dao实现类
 * @author: chenming
 * @date:   2019年6月4日 上午11:20:45
 */
public class WechatReplyKeywordDaoImpl extends BaseDao<WechatReplyKeyword> implements WechatReplyKeywordDaoExt {

  
	@Override
	public WechatReplyKeyword getKeyword(String appId,String content) {
		QWechatReplyKeyword qWechatReplyKeyword = QWechatReplyKeyword.wechatReplyKeyword;
		QWechatReplyContent qWechatReplyContent = QWechatReplyContent.wechatReplyContent;
		BooleanBuilder boolbuild = new BooleanBuilder();
		boolbuild.and(qWechatReplyKeyword.appId.eq(appId))
				.and(qWechatReplyKeyword.wordkeyEq.eq(content)
						.or(qWechatReplyKeyword.wordkeyLike.like("%" + content + "%")))
				.and(qWechatReplyKeyword.hasDeleted.eq(false))
				.and(qWechatReplyContent.isEnable.eq(true));
		JPAQuery<WechatReplyKeyword> query = super.getJpaQueryFactory()
				.select(Projections.bean(WechatReplyKeyword.class,
						qWechatReplyKeyword.replyContentId.as("replyContentId"), 
						qWechatReplyKeyword.id.as("id"),
						qWechatReplyKeyword.appId.as("appId")))
				.from(qWechatReplyKeyword).leftJoin(qWechatReplyContent)
				.on(qWechatReplyKeyword.replyContentId.eq(qWechatReplyContent.id)).where(boolbuild)
				.orderBy(qWechatReplyContent.sortNum.desc()).limit(1);
		return QuerydslUtils.findOne(query, null);
	}

}
