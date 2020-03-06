package com.jeecms.common.jpa;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.jeecms.common.page.Paginable;
import com.querydsl.core.types.Predicate;

/**
 * QueryDsl Predicate Executor
 * @author: tom
 * @date:   2018年1月26日 下午7:09:15     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 * */
public interface BaseQueryDslPredicateExecutor<T> extends
		QuerydslPredicateExecutor<T> {

	/**
	 * 查询list 带排序
	 * @Title: findAll   
	 * @param predicate Predicate
	 * @param sort Sort
	 * @return: List
	 */
	List<T> findAll(Predicate predicate, Sort sort);

	/**
	 * 查询list  带数量
	 * @Title: findAll  
	 * @param predicate Predicate
	 * @param paginable Paginable
	 * @return: List
	 */
	List<T> findAll(Predicate predicate,Paginable paginable);
}
