package com.jeecms.system.dao.impl;

import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.common.page.Paginable;
import com.jeecms.system.dao.ext.SearchWordDaoExt;
import com.jeecms.system.domain.SysSearchWord;
import com.jeecms.system.domain.querydsl.QSysSearchWord;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @Description:查询dao扩展实现
 * @author: tom
 * @date: 2018年6月13日 上午9:59:03
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Repository
public class SearchWordDaoImpl implements SearchWordDaoExt {

	@Override
	public List<SysSearchWord> getList(Integer siteId, String word, Boolean recommend, int orderBy,
									   Paginable paginable) {
		JPAQuery<SysSearchWord> query = new JPAQuery<SysSearchWord>(this.em);
		QSysSearchWord searchWord = QSysSearchWord.sysSearchWord;
		query.from(searchWord);
		BooleanBuilder exp = new BooleanBuilder();

		if (siteId != null) {
			exp.and(searchWord.siteId.eq(siteId));
		}
		if (word != null) {
			exp.and(searchWord.word.eq(word));
		}

		if (recommend != null) {
			exp.and(searchWord.isRecommend.eq(recommend));
		}
		if (orderBy == SysSearchWord.ODER_BY_SORT_NUM_DESC) {
			query.orderBy(searchWord.sortNum.desc());
		} else if (orderBy == SysSearchWord.ODER_BY_SEARCH_COUNT_DESC) {
			query.orderBy(searchWord.searchCount.desc());
		}
		query.where(exp);
		return QuerydslUtils.list(query, paginable, searchWord);
	}

	@Override
	public List<SysSearchWord> getList(Integer siteId, int orderBy, Paginable paginable) {
		QSysSearchWord searchWord = QSysSearchWord.sysSearchWord;
		JPAQuery<SysSearchWord> query = new JPAQuery<SysSearchWord>(this.em);
		query.from(searchWord);
		BooleanBuilder exp = new BooleanBuilder();

		if (siteId != null) {
			exp.and(searchWord.siteId.eq(siteId));
		}
		if (orderBy == SysSearchWord.ODER_BY_SORT_NUM_DESC) {
			query.orderBy(searchWord.sortNum.desc());
		} else if (orderBy == SysSearchWord.ODER_BY_SORT_NUM_ASC) {
			query.orderBy(searchWord.sortNum.asc());
		} else if (orderBy == SysSearchWord.ODER_BY_SEARCH_COUNT_DESC) {
			query.orderBy(searchWord.searchCount.desc());
		} else if (orderBy == SysSearchWord.ODER_BY_SEARCH_COUNT_ASC) {
			query.orderBy(searchWord.searchCount.asc());
		}
		query.orderBy(searchWord.id.desc());
		query.where(exp);
		//增加使用查询缓存
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		return QuerydslUtils.list(query, paginable, searchWord);
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

}
