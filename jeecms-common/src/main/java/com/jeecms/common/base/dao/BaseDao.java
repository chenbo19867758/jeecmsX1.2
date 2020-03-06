package com.jeecms.common.base.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.jpa.JpaQueryBuilder;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.common.page.Paginable;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 扩展DAO实现类的基类
 * @author: tom
 * @date: 2018年4月7日 上午10:37:22
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public abstract class BaseDao<T> {

	public JPAQueryFactory getJpaQueryFactory() {
		entityManager.clear();
		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(HQLTemplates.DEFAULT,entityManager);;
		return jpaQueryFactory;
	}

	/**
	 * 查询list
	 * @Title: getListByDsl  
	 * @param query JPAQuery对象
	 * @param paginable Paginable
	 * @return: List 
	 */
	public List<T> getListByDsl(JPAQuery<T> query, Paginable paginable) {
		query.limit(paginable.getMax());
		// paginable封装的时候+1了，从1开始，但是jpa查询下标从0开始
		query.offset(paginable.getOffset());
		List<T> list = query.fetch();
		return list;
	}

	/**
	 * 查询list 
	 * @Title: getList  
	 * @param jb  JpaQueryBuilder
	 * @param domain Class
	 * @param paginable Paginable
	 * @return: List 
	 */
	public List<T> getList(JpaQueryBuilder jb, Class<T> domain, Paginable paginable) {
		TypedQuery<T> query = jb.createQuery(entityManager, domain,paginable.getSort());
		query.setMaxResults(paginable.getMax());
		// paginable封装的时候+1了，从1开始，但是jpa查询下标从0开始
		query.setFirstResult(paginable.getOffset());
		return query.getResultList();
	}

	public Page<T> getPage(JpaQueryBuilder jb, Class<T> domain, Pageable pageable) {
		return jb.page(entityManager, domain, pageable);
	}

	/**
	 * 查询list 根据hql
	 * @Title: getListByHql  
	 * @param queryString hql
	 * @param domain Class
	 * @param paginable Paginable
	 * @return: List
	 */
	public List<T> getListByHql(String queryString, Class<T> domain, Paginable paginable) {
		JpaQueryBuilder jb = new JpaQueryBuilder(queryString);
		TypedQuery<T> query = jb.createQuery(entityManager, domain);
		query.setMaxResults(paginable.getMax());
		// paginable封装的时候+1了，从1开始，但是jpa查询下标从0开始
		query.setFirstResult(paginable.getOffset());
		return query.getResultList();
	}

	public Page<T> getPageByHql(String queryString, Class<T> domain, Pageable pageable) {
		JpaQueryBuilder jb = new JpaQueryBuilder(queryString);
		return jb.page(entityManager, domain, pageable);
	}

	/**
	 * 查询单个对象根据hql
	 * @Title: getSingleResultByHql  
	 * @param queryString hql
	 * @param domain Class
	 * @return: T
	 */
	public T getSingleResultByHql(String queryString, Class<T> domain) {
		JpaQueryBuilder jb = new JpaQueryBuilder(queryString);
		TypedQuery<T> query = jb.createQuery(entityManager, domain);
		return query.getSingleResult();
	}

	/**
	 * 查询数量 根据hql
	 * @Title: getCountByHql  
	 * @param queryString hql
	 * @param domain Class
	 * @return: int
	 */
	public int getCountByHql(String queryString, Class<T> domain) {
		JpaQueryBuilder jb = new JpaQueryBuilder(queryString);
		TypedQuery<T> query = jb.createQuery(entityManager, domain);
		return query.getMaxResults();
	}

	/**
	 * 执行hql 
	 * @Title: executeUpdate  
	 * @param queryString hql
	 * @return: int
	 */
	public int executeUpdate(String queryString) {
		JpaQueryBuilder jb = new JpaQueryBuilder(queryString);
		Query query = jb.createQuery(entityManager);
		return query.executeUpdate();
	}

	/**
	 * 查询分页 QueryDSL方式
	 * @Title: getPageByDsl  
	 * @param query JPAQuery对象
	 * @param entityPath EntityPathBase
	 * @param pageable Pageable
	 * @return: Page 
	 */
	public Page<T> getPageByDsl(JPAQuery<T> query, EntityPathBase<T> entityPath, 
			Pageable pageable) {
		return QuerydslUtils.page(query, pageable, entityPath);
	}

	@PersistenceContext
	protected EntityManager entityManager;
}
