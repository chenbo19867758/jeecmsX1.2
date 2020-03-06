package com.jeecms.common.jpa;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jeecms.common.page.Paginable;

/**
 * JPA Specification Executor JPA接口定义通用接口
 * 
 * @author: tom
 * @date: 2018年1月26日 下午4:29:35
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface BaseJpaSpecificationExecutor<T, ID> extends JpaSpecificationExecutor<T> {
	/**
	 * 查询list 根据 Specification
	 * 
	 * @Title: findAll
	 * @param spec
	 *            Specification
	 * @param paginable
	 *            Paginable
	 * @return: List
	 */
	List<T> findAll(Specification<T> spec, Paginable paginable);
}
