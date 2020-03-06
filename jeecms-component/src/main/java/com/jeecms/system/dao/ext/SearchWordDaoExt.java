package com.jeecms.system.dao.ext;

import com.jeecms.common.page.Paginable;
import com.jeecms.system.domain.SysSearchWord;

import java.util.List;

/**
 * 搜索词扩展dao接口
 *
 * @author: tom
 * @date: 2018年6月13日 上午9:57:51
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface SearchWordDaoExt {
	/**
	 * 获取搜索词列表
	 *
	 * @param siteId    站点id
	 * @param word      搜索词
	 * @param recommend 是否推荐
	 * @param orderBy   排序
	 * @param paginable 取条
	 * @Title: getList
	 * @return: List
	 */
	List<SysSearchWord> getList(Integer siteId, String word, Boolean recommend, int orderBy,
								Paginable paginable);

	/**
	 * 获取搜索词列表
	 *
	 * @param siteId    站点id
	 * @param orderBy   排序
	 * @param paginable 取条数
	 * @return List
	 */
	List<SysSearchWord> getList(Integer siteId, int orderBy, Paginable paginable);
}
