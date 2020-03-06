/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.system.domain.SysSearchWord;

import java.util.List;

/**
 * 搜索词service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-27
 */
public interface SysSearchWordService extends IBaseService<SysSearchWord, Integer> {

	/**
	 * 添加多个搜索词（分割word）
	 *
	 * @param sysSearchWord 搜索词实体
	 * @param siteId        站点Id
	 * @return responseInfo
	 * @throws GlobalException 异常
	 */
	List<SysSearchWord> saveBatch(SysSearchWord sysSearchWord, Integer siteId) throws GlobalException;

	/**
	 * 校验搜索词是否可用
	 *
	 * @param word   搜索词
	 * @param id     搜索词id
	 * @param siteId 站点id
	 * @return boolean true表示数据库不存在，可用，false表示数据库存在，不可用
	 */
	boolean checkWord(String word, Integer id, Integer siteId);

	/**
	 * 保存入库，使用缓存
	 *
	 * @param siteId 站点id
	 * @param word   搜索词
	 * @throws GlobalException 程序异常
	 * @Title: saveSearchWord
	 * @return: void
	 */
	void saveSearchWord(Integer siteId, String word) throws GlobalException;

	/**
	 * 获取搜索词列表
	 *
	 * @param siteId    站点id
	 * @param orderBy   排序
	 * @param paginable 分页
	 * @return List
	 */
	List<SysSearchWord> getList(Integer siteId, int orderBy, Paginable paginable);
}
