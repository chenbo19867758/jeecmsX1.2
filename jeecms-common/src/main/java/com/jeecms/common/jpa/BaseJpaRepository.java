package com.jeecms.common.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeecms.common.page.Paginable;

/**
 * 扩展JpaRepository接口 继承了JpaRepository就有此接口方法
 * @author: tom
 * @date:   2018年1月26日 下午2:39:35     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 * */
public interface BaseJpaRepository<T, ID extends Serializable> extends
		JpaRepository<T, ID> {
	
	/**
	 * 查询list
	 * @Title: findAll   
	 * @param paginable Paginable
	 * @return: List
	 */
	List<T> findAll(Paginable paginable);
}
