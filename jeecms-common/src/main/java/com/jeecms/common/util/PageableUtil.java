package com.jeecms.common.util;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

/**
 * Pageable 工具类
 * 
 * @author: qqwang
 * @date: 2018年4月11日 下午3:25:30
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class PageableUtil {

	/**
	 * 重组pageable对象中的排序，支持多条件对应不同的排序方式
	 * 
	 * @Title: by
	 * @Description:
	 * @param pageable  Pageable
	 * @param orders Order集合
	 * @return: Pageable
	 */
	public static Pageable by(Pageable pageable, List<Order> orders) {
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
	}

	/**
	 * 重组pageable对象中的排序，支持多条件对应单一排序方式
	 * 
	 * @Title: by
	 * @Description:
	 * @param pageable
	 *            Pageable
	 * @param direction
	 *            Direction
	 * @param properties
	 *            属性
	 * @return: Pageable
	 */
	public static Pageable by(Pageable pageable, Direction direction, String... properties) {
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), direction, properties);
	}
}
