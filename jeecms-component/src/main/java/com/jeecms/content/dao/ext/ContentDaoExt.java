/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.dao.ext;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.page.Paginable;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.dto.ContentSearchDto;
import com.jeecms.content.domain.vo.ContentVo;

/**
 * 内容扩展查询dao接口
 *
 * @author: tom
 * @date: 2019年5月11日 下午1:47:17
 */
public interface ContentDaoExt {

	/**
	 * 内容管理列表分页
	 *
	 * @param dto      搜索Dto
	 * @param pageable 分页对象
	 * @return Page 返回的内容分页
	 * @Title: getPage
	 */
	Page<Content> getPage(ContentSearchDto dto, Pageable pageable);
	
	/**
	 * 内容管理列表分页
	 *
	 * @param dto      搜索Dto
	 * @param pageable 分页对象
	 * @return Page 返回的内容分页
	 * @Title: getPage
	 */
	Page<ContentVo> getPages(ContentSearchDto dto, Pageable pageable);

	/**
	 * 内容管理列表
	 *
	 * @param dto       搜索Dto
	 * @param paginable 列表对象
	 * @return List 返回的内容
	 * @Title: getList
	 */
	List<Content> getList(ContentSearchDto dto, Paginable paginable);

	/**根据筛选条件得到内容数**/
	long getCount(ContentSearchDto dto);

	/**
	 * 获取内容数
	 *
	 * @param beginTime  开始时间
	 * @param endTime    结束时间
	 * @param siteId     站点id
	 * @param status     内容状态(1:草稿; 2-初稿 3:流转中; 4:待发布; 5:已发布; 6:退回; 7:下线 8-归档 )
	 * @param createType 创建方式（1:直接创建 2:投稿 3:站群推送 4:站群采集 5:复制 6:链接型引用 7:镜像型引用）
	 * @return 内容发布数
	 */
	long getSum(Date beginTime, Date endTime, Integer siteId, Integer status, Integer createType);

	/**
	 * 根据标题和栏目id进行检索
	 *
	 * @param title     标题
	 * @param channelId 栏目名称
	 * @Title: findByTitle
	 * @return: Integer
	 */
	Integer findByTitle(String title, Integer channelId);
	
}
