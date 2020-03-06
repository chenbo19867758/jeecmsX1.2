package com.jeecms.common.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

/**
 * 取数排序接口实现
 * 
 * @author tom
 * 
 */
public class PaginableRequest implements Paginable, Serializable {
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_MAX_SIZE = 5000;

	private Integer first;
	private Integer max;
	private Sort sort;

	public PaginableRequest(Integer first, Integer max) {
		this(first, max, Sort.unsorted());
	}

	public PaginableRequest(Integer first, Integer max, Direction direction, String... properties) {
		this(first, max, new Sort(direction, properties));
	}

	/**
	 * 创建 PaginableRequest
	 * @param first 第一条
	 * @param max 取多少条
	 * @param sort 排序
	 */
	public PaginableRequest(Integer first, Integer max, Sort sort) {
		if (first != null && first > 0) {
			this.first = first;
		}
		if (max != null && max > 0) {
			this.max = max;
		}
		this.sort = sort;
	}

	public PaginableRequest() {
		this(0, DEFAULT_MAX_SIZE, Sort.unsorted());
	}

	/**
	 * 设置默认排序
	 * @Title: defaultSort  
	 * @param paginable Paginable
	 * @param sort Sort
	 * @return: Paginable
	 */
	public static Paginable defaultSort(Paginable paginable, Sort sort) {
		if (paginable == null) {
			paginable = new PaginableRequest(null, null, sort);
		} else if (paginable.getSort() == null) {
			paginable.setSort(sort);
		}
		return paginable;
	}

	@Override
	public Integer getFirst() {
		return this.first;
	}

	@Override
	public Integer getMax() {
		return this.max;
	}

	@Override
	public Integer getOffset() {
		if (first != null && first > 0) {
			return first - 1;
		} else {
			return 0;
		}
	}

	@Override
	public int getLast() {
		int last = 0;
		Integer first = getFirst();
		if (first != null && first > 0) {
			last += first;
		}
		Integer max = getMax();
		if (max != null && max > 0) {
			last += max;
		}
		return last;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Sort setSort(String s) {
		List<Order> orders = new ArrayList<Order>();
		Order order;
		String[] pairArr = StringUtils.split(s, ',');
		for (String pairStr : pairArr) {
			String[] pair = StringUtils.split(pairStr);
			if (pair.length > 1) {
				order = new Order(Direction.fromString(pair[1]), pair[0]);
			} else {
				order = Order.by(pair[0]);
			}
			orders.add(order);
		}
		return new Sort(orders);
	}
	
	@Override
	public void setSort(Sort sort) {
		this.sort = sort;
	}
	
	@Override
	public Sort getSort() {
		return this.sort;
	}
}
