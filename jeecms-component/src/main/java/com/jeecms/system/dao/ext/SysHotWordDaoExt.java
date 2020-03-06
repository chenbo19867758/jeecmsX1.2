/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao.ext;

import com.jeecms.system.domain.SysHotWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 热词扩展dao接口
 * @author: tom
 * @date: 2019年5月21日 下午4:37:49
 */
public interface SysHotWordDaoExt {

	/**
	 *查询热词列表
	 * @Title: getPage
	 * @param hotWord 关键词
	 * @param siteId 站点ID
	 * @param hotWordCategoryId 热词分类ID
	 * @param channelId 栏目ID
	 * @param pageable 分页
	 * @return: List
	 */
	Page<SysHotWord> getPage(String hotWord, Integer siteId, Integer hotWordCategoryId,
							 Integer channelId, Pageable pageable);

	/**
	 *查询热词列表
	 * @Title: getList
	 * @param hotWord 关键词
	 * @param siteId 站点ID
	 * @param hotWordCategoryId 热词分类ID
	 * @param channelId 栏目ID
	 * @return: List
	 */
	List<SysHotWord> getList(String hotWord, Integer siteId, Integer hotWordCategoryId,
							 Integer channelId);

	/**
	 * 根据是否全栏目查找列表
	 * @param siteId 站点id
	 * @param applyScope 1 全栏目 2非全栏目
	 * @return List
	 */
	List<SysHotWord> findByApplyScope(Integer siteId, Integer applyScope);

}
