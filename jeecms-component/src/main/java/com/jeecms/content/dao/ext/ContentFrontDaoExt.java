/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.dao.ext;

import com.jeecms.content.domain.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/19 9:27
 */

public interface ContentFrontDaoExt {

	/**
	 * 获取内容
	 *
	 * @param channelIds    栏目id集合
	 * @param tagIds        tagId集合
	 * @param channelPaths  栏目地址集合
	 * @param siteId        站点id
	 * @param typeIds       内容类型集合
	 * @param title         标题
	 * @param date          新内容时间点
	 * @param releaseTarget 1pc 2手机
	 * @param isTop         true 置顶
	 * @param timeBegin     开始时间
	 * @param timeEnd       结束时间
	 * @param excludeId     排除内容id集合（tagId不为空生效）
	 * @param modelId       模型id数组
	 * @param orderBy       排序
	 * @param pageable      分页组件
	 * @return Page
	 */
	Page<Content> getPage(Integer[] channelIds, Integer[] tagIds, String[] channelPaths, Integer siteId,
						  Integer[] typeIds, String title, Date date, Integer releaseTarget,
						  Boolean isTop, Date timeBegin, Date timeEnd, Integer[] excludeId,
						  Integer[] modelId, Integer orderBy, Pageable pageable);

	/**
	 * 投稿分页查询
	 *
	 * @param siteId    站点id
	 * @param userId    用户id
	 * @param status    稿件状态
	 * @param title     内容标题
	 * @param startDate 最小时间
	 * @param endDate   截止时间
	 * @param pageable  分页对象
	 * @return Page
	 */
	Page<Content> getPage(Integer siteId, Integer userId, Integer status, String title,
						  Date startDate, Date endDate, Pageable pageable);

	/**
	 * 根据tag词获取列表
	 *
	 * @param channelIds    栏目id集合
	 * @param tagIds        tagId集合
	 * @param channelPaths  栏目地址集合
	 * @param siteId        站点id
	 * @param typeIds       内容类型集合
	 * @param title         标题
	 * @param date          新内容时间点
	 * @param releaseTarget 1pc 2手机
	 * @param isTop         true 置顶
	 * @param timeBegin     开始时间
	 * @param timeEnd       结束时间
	 * @param excludeId     排除内容id集合（tagId不为空生效）
	 * @param modelId       模型id数组
	 * @param orderBy       排序
	 * @param count         数量
	 * @return 内容集合
	 */
	List<Content> getList(Integer[] channelIds, Integer[] tagIds, String[] channelPaths,
						  Integer siteId, Integer[] typeIds, String title,
						  Date date, Integer releaseTarget, Boolean isTop,
						  Date timeBegin, Date timeEnd, Integer[] excludeId,
						  Integer[] modelId, Integer orderBy, Integer count);

	/**
	 * 获取相关内容
	 *
	 * @param relationIds 相关内容id
	 * @param orderBy     排序
	 * @param count       数量
	 * @return List
	 */
	List<Content> getList(Integer[] relationIds, Integer orderBy, Integer count);

	/**
	 * 根据ids获取内容集合
	 *
	 * @param ids     内容id集合
	 * @param orderBy 排序
	 * @return List
	 */
	List<Content> findByIds(List<Integer> ids, Integer orderBy);

	/**
	 * 获得一篇内容的上一篇或下一篇内容
	 *
	 * @param id        文章ID。
	 * @param siteId    站点ID。可以为null。
	 * @param channelId 栏目ID。可以为null。
	 * @param next      根据文章ID，大者为下一篇，小者为上一篇。true：下一篇；fasle：上一篇。
	 * @param cacheable 是否使用缓存。
	 * @return Content
	 */
	Content getSide(Integer id, Integer siteId, Integer channelId,
					Boolean next, boolean cacheable);

}
