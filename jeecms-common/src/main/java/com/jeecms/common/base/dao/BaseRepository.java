package com.jeecms.common.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import com.jeecms.common.page.Paginable;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * JpaRepository扩展实现类
 * 
 * @author: tom
 * @date: 2018年12月24日 下午1:36:28
 * @param <T>
 *            domain泛型类
 * @param <Id>
 *            主键ID类
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("unused")
public class BaseRepository<T, Id extends Serializable> extends QuerydslJpaRepository<T, Id>
		implements IBaseDao<T, Id> {

	private final JPAQueryFactory jpaQueryFactory;
	private final EntityManager entityManager;
	private final JpaEntityInformation<T, ?> entityInformation;
	private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;

	private final EntityPath<T> path;
	private final PathBuilder<T> builder;
	private final Querydsl querydsl;

	public BaseRepository(JpaEntityInformation<T, Id> entityInformation, EntityManager entityManager) {
		this(entityInformation, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
	}

	/**
	 * 构造器
	 * 
	 * @param entityInformation
	 *            JpaEntityInformation
	 * @param entityManager
	 *            EntityManager
	 * @param resolver
	 *            EntityPathResolver
	 */
	public BaseRepository(JpaEntityInformation<T, Id> entityInformation, EntityManager entityManager,
			EntityPathResolver resolver) {
		// super(entityInformation, entityManager, resolver);
		super(entityInformation, entityManager, com.jeecms.common.jpa.SimpleEntityPathResolver.INSTANCE);
		this.entityInformation = entityInformation;
		this.entityManager = entityManager;
		this.jpaQueryFactory = new JPAQueryFactory(HQLTemplates.DEFAULT, entityManager);
		// this.path = resolver.createPath(entityInformation.getJavaType());
		this.path = com.jeecms.common.jpa.SimpleEntityPathResolver.INSTANCE.createPath(entityInformation.getJavaType());
		this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
		this.querydsl = new Querydsl(entityManager, builder);
	}

	@Override
	public List<T> findAll(Specification<T> spec, Paginable paginable, boolean cacheable) {
		TypedQuery<T> query;
		if (paginable != null) {
			query = getQuery(spec, paginable.getSort());
			if (paginable.getOffset() != null) {
				query.setFirstResult(paginable.getOffset());
			}
			if (paginable.getMax() != null) {
				query.setMaxResults(paginable.getMax());
			}
		} else {
			query = getQuery(spec, Sort.unsorted());
		}
		query.setHint("org.hibernate.cacheable", cacheable);
		return query.getResultList();
	}

	@Override
	public Page<T> findAll(Specification<T> spec, Pageable pageable, boolean cacheable) {
		TypedQuery<T> query = getQuery(spec, pageable);
		query.setHint("org.hibernate.cacheable", cacheable);
		return pageable.isUnpaged() ? new PageImpl<T>(query.getResultList())
				: readPage(query, getDomainClass(), pageable, spec);
	}

	@Override
	public Optional<T> findOne(Specification<T> spec, boolean cacheable) {
		TypedQuery<T> query = getQuery(spec, Sort.unsorted());
		query.setHint("org.hibernate.cacheable", cacheable);
		try {
			return Optional.of(query.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public <S extends T> S save(S entity) {
		return super.save(entity);
	}

	@Override
	public Class<T> getDynamicClass() {
		return super.getDomainClass();
	}

	public JpaEntityInformation<T, ?> getEntityInformation() {
		return entityInformation;
	}

}
