package com.jeecms.content.service;

import com.jeecms.channel.domain.Channel;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.dto.ContentSearchDto;
import com.jeecms.content.domain.vo.ContentVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 内容查询service接口
 * @author: chenming
 * @date:   2019年8月5日 上午10:38:39
 */
public interface ContentGetService {

	/**
	 * 内容管理列表
	 * 
	 * @Title: getPage
	 * @param dto      搜索Dto
	 * @param pageable 分页对象
	 * @return Page
	 * @throws GlobalException 异常
	 */
	Page<ContentVo> getPage(ContentSearchDto dto, Pageable pageable) throws GlobalException;

	/**
	 * 内容列表分页
	 * 
	 * @Title: getPages
	 * @param dto      搜索Dto
	 * @param pageable 分页对象
	 * @return Page
	 * @throws GlobalException 异常
	 */
	Page<Content> getPages(ContentSearchDto dto, Pageable pageable);

	/**
	 * 内容列表分页
	 * 
	 * @Title: getList
	 * @param dto       搜索Dto
	 * @param paginable 列表对象
	 * @return List 集合
	 * @throws GlobalException 异常
	 */
	List<Content> getList(ContentSearchDto dto, Paginable paginable);
	
	/**
	 * 查询栏目下内容数
	 * 
	 * @Title: getCountByChannel
	 * @param channel      栏目
	 * @param containChild 是否包含子栏目内容
	 * @return: Integer
	 */
	Integer getCountByChannel(Channel channel, boolean containChild);

	/**
	 * 搜索到内容的个数
	 * @Title: getCount  
	 * @param dto	内容搜索Dto
	 * @return: long
	 */
	long getCount(ContentSearchDto dto);
	
	/**
	 * 发布内容数量
	 *
	 * @param beginTime 开始时间
	 * @param endTime   结束时间
	 * @param siteId    站点id
	 * @return 发布内容数量
	 */
	long getReleaseSum(Date beginTime, Date endTime, Integer siteId);

	/**
	 * 投稿数
	 *
	 * @param beginTime 开始时间
	 * @param endTime   结束时间
	 * @param siteId    站点id
	 * @return 投稿数
	 */
	long getSubmissionSum(Date beginTime, Date endTime, Integer siteId);
}
