/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.ContentTxt;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysHotWord;
import com.jeecms.system.domain.dto.HotWordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 热词Service接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-28
 */
public interface SysHotWordService extends IBaseService<SysHotWord, Integer> {


	/**
	 * 查询热词列表
	 *
	 * @param hotWord           关键词
	 * @param siteId            站点ID
	 * @param hotWordCategoryId 热词分类ID
	 * @param channelId         栏目ID
	 * @param pageable          分页
	 * @Title: getPage
	 * @return: List
	 */
	Page<SysHotWord> getPage(String hotWord, Integer siteId, Integer hotWordCategoryId,
							 Integer channelId, Pageable pageable);

	/**
	 * 查询热词列表
	 *
	 * @param hotWord           关键词
	 * @param siteId            站点ID
	 * @param hotWordCategoryId 热词分类ID
	 * @param channelId         栏目ID
	 * @Title: getList
	 * @return: List
	 */
	List<SysHotWord> getList(String hotWord, Integer siteId, Integer hotWordCategoryId,
							 Integer channelId);

	/**
	 * 新增热词(分割输入的热词添加多个)
	 *
	 * @param dto    热词dto
	 * @param siteId 站点id
	 * @return List
	 * @throws GlobalException 异常
	 */
	List<SysHotWord> saveBatch(HotWordDto dto, Integer siteId) throws GlobalException;

	/**
	 * 修改热词
	 *
	 * @param dto 热词dto
	 * @return SysHotWord
	 * @throws GlobalException 异常
	 */
	SysHotWord update(HotWordDto dto) throws GlobalException;

	/**
	 * 校验热词是否存在
	 *
	 * @param hotWord 热词
	 * @param id      热词id
	 * @return true 不存在或热词为null false 存在
	 */
	boolean checkByHotWord(String hotWord, Integer id);

	/**
	 * 获取附加热词到正文之后的正文
	 *
	 * @param channelId 栏目Id
	 * @param txt       正文
	 * @param site      站点
	 * @return String
	 */
	String attachKeyword(Integer channelId, String txt, CmsSite site);

	/**
	 * 统计使用次数
	 *
	 * @param channelId   栏目id
	 * @param contentTxts 正文
	 * @param siteId      站点id
	 * @throws GlobalException 异常
	 */
	void totalUserCount(Integer channelId, List<ContentTxt> contentTxts, Integer siteId) throws GlobalException;

	/**
	 * 统计点击次数
	 *
	 * @param siteId 站点id
	 * @param id     热词id
	 */
	void click(Integer siteId, Integer id) throws GlobalException;
}
