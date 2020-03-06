package com.jeecms.common.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.jeecms.common.page.Paginable;

/**
 * 扩展 Simple JPA Repository ，是JpaRepository默认实现
 *实现 ExtJpaSpecificationExecutor  ExtJpaRepository  接口定义的方法
 * @author: tom
 * @date:   2018年1月26日 下午8:39:35     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.  Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 * */
public class BaseSimpleJpaRepository<T, ID extends Serializable> extends
		SimpleJpaRepository<T, ID> implements BaseJpaRepository<T, ID>,
		BaseJpaSpecificationExecutor<T,ID> {

	private final EntityManager entityManager;

	public BaseSimpleJpaRepository(JpaEntityInformation<T, ?> entityInformation,
			EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	public BaseSimpleJpaRepository(Class<T> domainClass, EntityManager em) {
		this(JpaEntityInformationSupport.getEntityInformation(domainClass, em),em);
	}

	/**
	 * findAll 不分页方法默认使用查询缓存
	 * paginable 分页接口
	 */
	@Override
	public List<T> findAll(Paginable paginable) {
		if (paginable != null) {
			TypedQuery<T> query = getQuery(null, paginable.getSort());
			//默认开启查询缓存
			//query.setHint(QueryHints.HINT_CACHEABLE, true);
			Integer firstResult = paginable.getOffset();
			if (firstResult != null && firstResult > 0) {
				query.setFirstResult(firstResult);
			}
			Integer maxResults = paginable.getMax();
			if (maxResults != null && maxResults > 0) {
				query.setMaxResults(maxResults);
			}
			return query.getResultList();
		} else {
			return findAll();
		}
	}

	/**
	 * findAll 不分页方法默认使用查询缓存
	 * spec 是带查询条件的 Specification
	 * paginable 分页接口
	 */
	@Override
	public List<T> findAll(Specification<T> spec,Paginable paginable) {
		if (paginable != null) {
			TypedQuery<T> query = getQuery(spec, paginable.getSort());
			if (paginable.getOffset() != null) {
				query.setFirstResult(paginable.getOffset());
			}
			if (paginable.getMax() != null) {
				query.setMaxResults(paginable.getMax());
			}
			return query.getResultList();
		} else {
			return findAll(spec);
		}
	}

	public void refresh(T entity) {
		entityManager.refresh(entity);
	}
}
