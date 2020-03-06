package com.jeecms.common.page;

import org.springframework.data.domain.Sort;

/**
 * 取数排序接口
 * 
 * @author tom
 * 
 */
public interface Paginable {
	/**
	 * 第一条
	 * @Title: getFirst  
	 * @return: Integer
	 */
	public Integer getFirst();

	/**
	 * 数量
	 * @Title: getMax  
	 * @return: Integer
	 */
	public Integer getMax();
	
	/**
	 * 数量-偏移量
	 * @Title: getOffset  
	 * @return: Integer
	 */
	public Integer getOffset();

	/**
	 * 最后一条
	 * @Title: getLast  
	 * @return: int
	 */
	public int getLast();

	/**
	 * 获取排序
	 * @Title: getSort  
	 * @return: Sort
	 */
	public Sort getSort();

	/**
	 * 设置排序
	 * @Title: setSort  
	 * @param sort   Sort    
	 * @return: void
	 */
	public void setSort(Sort sort);
	
	/**
	 *  设置排序
	 * @Title: setSort  
	 * @param s 排序字符串 例如  id,desc id,asc
	 * @return: Sort
	 */
	public Sort setSort(String s);
}
