package com.jeecms.common.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

import com.jeecms.common.page.Paginable;

/**
 * 扩展DAO接口类的基类
 * 
 * @author: tom
 * @date: 2018年4月7日 上午10:37:22
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@NoRepositoryBean
public interface IBaseDao<T, ID extends Serializable>
		extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>, QuerydslPredicateExecutor<T> {

	/**
	 * 查询list
	 * 
	 * @Title: findAll
	 * @param spec
	 *            Specification
	 * @param paginable
	 *            Paginable
	 * @param cacheable
	 *            是否使用缓存
	 * @return: List
	 */
	List<T> findAll(Specification<T> spec, Paginable paginable, boolean cacheable);

	/**
	 * 查询page
	 * 
	 * @Title: findAll
	 * @param spec
	 *            Specification
	 * @param pageable
	 *            Pageable
	 * @param cacheable
	 *            是否使用缓存
	 * @return: Page
	 */
	Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable, boolean cacheable);

	/**
	 * 查询单个对象
	 * @Title: findOne
	 * @param spec 查询条件
	 * @param cacheable 是否使用查询缓存
	 * @return: Optional
	 */
	Optional<T> findOne(@Nullable Specification<T> spec, boolean cacheable);
	
	/**
	 * 根据ID查找
	 * @Title: findAllById
	 * @param ids 
	 * @return: List<T>
	 */
	List<T> findAllById(Iterable<ID> ids);

	/**
	 * 获取class
	 * 
	 * @Title: getDynamicClass
	 * @return: Class
	 */
	Class<T> getDynamicClass();
}
