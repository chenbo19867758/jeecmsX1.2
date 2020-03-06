package com.jeecms.common.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import com.jeecms.common.page.Paginable;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;

/**
 * QueryDsl JpaRepository
 * @author: tom
 * @date:   2018年1月26日 下午6:39:35     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 * */
public class BaseQueryDslJpaRepository<T, ID extends Serializable> extends
	QuerydslJpaRepository<T, ID> implements BaseQueryDslPredicateExecutor<T> {

	private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;
	private final EntityPath<T> path;
	private final PathBuilder<T> builder;
	private final Querydsl querydsl;

	public BaseQueryDslJpaRepository(
			JpaEntityInformation<T, ID> entityInformation,
			EntityManager entityManager) {
		this(entityInformation, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
	}

	/**
	 * 构造器
	 * @param entityInformation  JpaEntityInformation
	 * @param entityManager EntityManager
	 * @param resolver EntityPathResolver
	 */
	public BaseQueryDslJpaRepository(
			JpaEntityInformation<T, ID> entityInformation,
			EntityManager entityManager, EntityPathResolver resolver) {
		super(entityInformation, entityManager, resolver);
		this.path = resolver.createPath(entityInformation.getJavaType());
		this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
		this.querydsl = new Querydsl(entityManager, builder);
	}

	@Override
	public List<T> findAll(Predicate predicate, Sort sort) {
		JPQLQuery<T> query = createQuery(predicate).select(path);
		querydsl.applySorting(sort, query);
		return query.fetch();
	}

	@Override
	public List<T> findAll(Predicate predicate, Paginable paginable) {
		JPQLQuery<T> query = createQuery(predicate).select(path);
		if (paginable != null) {
			querydsl.applySorting(paginable.getSort(), query);
			Integer offset = paginable.getOffset();
			if (offset != null && offset > 0) {
				query.offset(offset);
			}
			Integer count = paginable.getMax();
			if (count != null && count > 0) {
				query.limit(count);
			}
		}
		return query.fetch();
	}

}
