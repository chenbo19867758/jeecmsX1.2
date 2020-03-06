/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.dao.impl;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.member.dao.UserCollectionDao;
import com.jeecms.member.dao.ext.UserCollectionDaoExt;
import com.jeecms.member.domain.UserCollection;
import com.jeecms.member.domain.querydsl.QUserCollection;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-24
 */
public class UserCollectionDaoImpl extends BaseDao<UserCollectionDao> implements UserCollectionDaoExt {

	@Override
	public Page<UserCollection> getList(String title, Date startTime, Date endTime
			, Integer userId, Pageable pageable) {
		JPAQuery<UserCollection> query = new JPAQuery<UserCollection>(this.em);
		QUserCollection userCollection = QUserCollection.userCollection;
		BooleanBuilder exp = new BooleanBuilder();
		query.from(userCollection);
		if (StringUtils.isNotBlank(title)) {
			exp.and(userCollection.content.title.like("%" + title + "%"));
		}
		if (startTime != null) {
			exp.and(userCollection.createTime.goe(startTime));
		}
		if (endTime != null) {
			exp.and(userCollection.createTime.loe(endTime));
		}
		if (userId != null) {
			exp.and(userCollection.userId.eq(userId));
		}
		exp.and(userCollection.content.recycle.eq(false));
		exp.and(userCollection.content.hasDeleted.eq(false));
		query.where(exp);
		return QuerydslUtils.page(query, pageable, userCollection);
	}

	@Override
	public long getCount(Integer contentId, Integer userId) {
		JPAQuery<UserCollection> query = new JPAQuery<UserCollection>(this.em);
		QUserCollection userCollection = QUserCollection.userCollection;
		BooleanBuilder exp = new BooleanBuilder();
		query.from(userCollection);
		if (contentId != null) {
			exp.and(userCollection.contentId.eq(contentId));
		}
		if (userId != null) {
			exp.and(userCollection.userId.eq(userId));
		}
		query.where(exp);
		return query.fetchCount();
	}

	@Override
	public UserCollection findByContentIdAndUserId(Integer contentId, Integer userId) {
		JPAQuery<UserCollection> query = new JPAQuery<UserCollection>(this.em);
		QUserCollection userCollection = QUserCollection.userCollection;
		BooleanBuilder exp = new BooleanBuilder();
		query.from(userCollection);
		if (contentId != null) {
			exp.and(userCollection.contentId.eq(contentId));
		}
		if (userId != null) {
			exp.and(userCollection.userId.eq(userId));
		}
		query.where(exp);
		return query.fetchOne();
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}


}
