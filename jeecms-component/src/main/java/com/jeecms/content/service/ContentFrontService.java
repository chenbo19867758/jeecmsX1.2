/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.dto.ContentContributeDto;
import com.jeecms.content.domain.vo.ContentContributeVo;
import com.jeecms.content.domain.vo.ContentFrontVo;
import com.jeecms.system.domain.CmsSite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 前台内容Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/19 9:25
 */

public interface ContentFrontService {

	/**
	 * 获取内容
	 *
	 * @param channelIds    栏目id集合
	 * @param tagIds        tagId集合
	 * @param channelPaths  栏目地址集合
	 * @param siteId        站点id
	 * @param typeIds       内容类型集合
	 * @param title         标题
	 * @param isNew         true 新内容
	 * @param releaseTarget 1 pc 2手机
	 * @param isTop         true 置顶
	 * @param timeBegin     开始时间
	 * @param timeEnd       结束时间
	 * @param excludeId     排除内容id集合（tagId不为空生效）
	 * @param modelId       模型id数组
	 * @param orderBy       排序
	 * @param pageable      分页组件
	 * @param site          站点
	 * @return Page
	 */
	Page<Content> getPage(Integer[] channelIds, Integer[] tagIds, String[] channelPaths, Integer siteId,
						  Integer[] typeIds, String title, Boolean isNew, Integer releaseTarget,
						  Boolean isTop, Date timeBegin, Date timeEnd, Integer[] excludeId,
						  Integer[] modelId, Integer orderBy, Pageable pageable, CmsSite site);

	/**
	 * 内容分页
	 *
	 * @param siteId    站点id
	 * @param userId    用户id
	 * @param status    状态
	 * @param title     标题
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param pageable  分页组件
	 * @return Page
	 */
	Page<Content> getPage(Integer siteId, Integer userId,
						  Integer status, String title,
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
	 * @param isNew         true 新内容
	 * @param releaseTarget 1pc 2手机
	 * @param isTop         true 置顶
	 * @param timeBegin     开始时间
	 * @param timeEnd       结束时间
	 * @param excludeId     排除内容id集合（tagId不为空生效）
	 * @param modelId       模型id数组
	 * @param orderBy       排序
	 * @param count         数量
	 * @param site          站点
	 * @return 内容集合
	 */
	List<Content> getList(Integer[] channelIds, Integer[] tagIds, String[] channelPaths,
						  Integer siteId, Integer[] typeIds, String title, Boolean isNew,
						  Integer releaseTarget, Boolean isTop, Date timeBegin, Date timeEnd,
						  Integer[] excludeId, Integer[] modelId, Integer orderBy,
						  Integer count, CmsSite site);

	/**
	 * 根据tag词获取列表
	 *
	 * @param channelIds    栏目id集合
	 * @param tagIds        tagId集合
	 * @param channelPaths  栏目地址集合
	 * @param siteId        站点id
	 * @param typeIds       内容类型集合
	 * @param title         标题
	 * @param isNew         true 新内容
	 * @param releaseTarget 1pc 2手机
	 * @param isTop         true 置顶
	 * @param timeBegin     开始时间
	 * @param timeEnd       结束时间
	 * @param excludeId     排除内容id集合（tagId不为空生效）
	 * @param modelId       模型id数组
	 * @param orderBy       排序
	 * @param count         数量
	 * @param user          用户
	 * @param site          站点
	 * @return 内容集合
	 */
	List<Content> getList(Integer[] channelIds, Integer[] tagIds, String[] channelPaths,
						  Integer siteId, Integer[] typeIds, String title, Boolean isNew,
						  Integer releaseTarget, Boolean isTop, Date timeBegin, Date timeEnd,
						  Integer[] excludeId, Integer[] modelId, Integer orderBy,
						  Integer count, CoreUser user, CmsSite site);

	/**
	 * 相关内容
	 *
	 * @param relationIds 相关内容id集合
	 * @param orderBy     排序
	 * @param count       数量
	 * @return 内容集合
	 */
	List<Content> getList(Integer[] relationIds, Integer orderBy, Integer count);

	/**
	 * 相关内容
	 *
	 * @param relationIds 相关内容id集合
	 * @param orderBy     排序
	 * @param count       数量
	 * @param user        用户
	 * @return 内容集合
	 */
	List<Content> getList(Integer[] relationIds, Integer orderBy, Integer count, CoreUser user);

	/**
	 * 根据id集合查找内容
	 *
	 * @param ids     id集合
	 * @param orderBy 排序 {@link ContentConstant}}
	 * @return 内容列表
	 */
	List<Content> findAllById(List<Integer> ids, Integer orderBy);

	/**
	 * 根据id集合查找内容
	 *
	 * @param ids     id集合
	 * @param orderBy 排序 {@link ContentConstant}}
	 * @param user    用户
	 * @return 内容列表
	 */
	List<Content> findAllById(List<Integer> ids, Integer orderBy, CoreUser user);

	/**
	 * 根据id查找内容
	 *
	 * @param id 内容id
	 * @return 内容
	 */
	Content findById(Integer id);

	/**
	 * 获得一篇内容的上一篇或下一篇内容
	 *
	 * @param id        文章ID。
	 * @param siteId    站点ID。可以为null。
	 * @param channelId 栏目ID。可以为null。
	 * @param next      根据文章ID，大者为下一篇，小者为上一篇。true：下一篇；fasle：上一篇。
	 * @return Content
	 */
	Content getSide(Integer id, Integer siteId, Integer channelId,
					Boolean next);

	/**
	 * 投稿
	 *
	 * @param dto 投稿内容dto
	 * @throws GlobalException 异常
	 */
	void contribute(ContentContributeDto dto) throws GlobalException;

	/**
	 * 更新投稿内容
	 *
	 * @param dto     投稿内容dto
	 * @param channel 栏目
	 * @param content 内容
	 * @param request {@link HttpServletRequest}
	 * @throws GlobalException 异常
	 */
	void updateContribute(ContentContributeDto dto, Channel channel, Content content,
						  HttpServletRequest request) throws GlobalException;

	/**
	 * 初始化内容
	 *
	 * @param content 内容数据
	 * @return 内容VO数据
	 */
	Content initialize(Content content);

	/**
	 * 初始化列表显示数据
	 *
	 * @param contents 内容数据集合
	 * @return 内容数据集合
	 */
	List<Content> initializeList(List<Content> contents);

	/**
	 * 投稿信息初始化
	 *
	 * @param content 内容
	 * @return 详细投稿信息
	 */
	ContentContributeVo splicContributeVo(Content content) throws GlobalException;

	/**
	 * 判断内容是否点赞
	 *
	 * @param user      用户
	 * @param contentId 内容id
	 * @param request   {@link HttpServletRequest}
	 * @return true 已点赞 false 未点赞
	 */
	boolean isUp(CoreUser user, Integer contentId, HttpServletRequest request);

	/**
	 * 点赞
	 *
	 * @param user      用户
	 * @param contentId 内容id
	 * @param request   {@link HttpServletRequest}
	 * @param response  {@link HttpServletResponse}
	 * @throws GlobalException 异常
	 */
	void up(CoreUser user, Integer contentId, HttpServletRequest request,
			HttpServletResponse response) throws GlobalException;

	/**
	 * 取消点赞
	 *
	 * @param user      用户
	 * @param contentId 内容id
	 * @param request   {@link HttpServletRequest}
	 * @param response  {@link HttpServletResponse}
	 * @throws GlobalException 异常
	 */
	void cancelUp(CoreUser user, Integer contentId, HttpServletRequest request,
				  HttpServletResponse response) throws GlobalException;

	/**
	 * 新增或修改内容和内容数值
	 *
	 * @param contentId  内容id值
	 * @param commentNum 评论数量
	 * @param type       数值类型
	 * @param isDelete   是否是删除
	 * @throws GlobalException 异常
	 */
	JSONObject saveOrUpdateNum(Integer contentId, Integer commentNum, String type, boolean isDelete)
			throws GlobalException;
	
	/**
	 * 列表分页
	* @Title: getMobilePage 
	* @param siteId 站点
	* @param userId 用户
	* @param status 状态
	* @param pageable 分页对象
	* @throws GlobalException 异常
	 */
	Page<ContentFrontVo> getMobilePage(Integer siteId, Integer userId,
			Integer status,Pageable pageable) throws GlobalException;
	
	/**
	 * 初始化手机VO
	 * 
	 * @Title: initMobileVo
	 * @param vo      手机VO
	 * @param content 内容
	 * @throws GlobalException 异常
	 */
	ContentFrontVo initMobileVo(ContentFrontVo vo, Content content) throws GlobalException;
	
	/**
	 * 初始化ContentFrontVo数据
	 * @Title: initPartVo  
	 * @param contentPage
	 * @throws GlobalException      
	 * @return: ContentFrontVo
	 */
	ContentFrontVo initPartVo(Content content) throws GlobalException;
}
