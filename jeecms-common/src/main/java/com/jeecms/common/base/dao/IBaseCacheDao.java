package com.jeecms.common.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.QueryHints;

import com.querydsl.core.types.Predicate;

/**
 * 查询缓存dao接口
 * 
 * @author: tom
 * @date: 2018年8月13日 上午9:59:35
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface IBaseCacheDao<T, ID extends Serializable> extends IBaseDao<T, ID> {
	
	/**
	 * 查询所有
	 * @return: List  List
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<T> findAll();

	/**
	 * 查询所有
	 * @param sort  Sort
	 * @return: List  List
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<T> findAll(Sort sort);

	/**
	 * 查询所有 
	 * @param pageable  Pageable
	 * @return: 分页对象  Page
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Page<T> findAll(Pageable pageable);

	/**
	 * 查询
	 * @param predicate  Predicate条件
	 * @param sort  排序条件
	 * @return: Iterable  Iterable
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Iterable<T> findAll(Predicate predicate, Sort sort);

	/**
	 * 查询所有
	 * @param spec  Specification
	 * @return: List  List
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<T> findAll(Specification<T> spec);

	/**
	 * 查询所有
	 * @param spec  Specification
	 * @param pageable  Pageable
	 * @return: List  List
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Page<T> findAll(Specification<T> spec, Pageable pageable);

	/**
	 * 查询所有
	 * @param spec  Specification
	 * @param sort  Sort
	 * @return: List  List
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<T> findAll(Specification<T> spec, Sort sort);

	/**
	 * 查询  根据ID
	 * @param ids  id 集合
	 * @return: List  List
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<T> findAllById(Iterable<ID> ids);

	/**
	 * 查询单个对象，可能为空
	 * @param spec Specification
	 * @return: List  List
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	Optional<T> findOne(Specification<T> spec);

	/**
	 * 查询数量 带Specification条件 (springdatajpa)
	 * @param spec Specification
	 * @return: 数量
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	long count(Specification<T> spec);


	/**
	 * 查询数量-带Predicate条件查询 （querydsl）
	 * @param predicate Predicate
	 * @return: 数量
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	long count(Predicate predicate);

	/**
	 * 查询数量 不带条件
	 * @return: 数量
	 */
	@Override
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	long count();

}
