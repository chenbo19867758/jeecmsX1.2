package com.jeecms.interact.service;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.Content;
import com.jeecms.interact.domain.UserComment;
import com.jeecms.interact.domain.dto.UserCommentSaveDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 评论pc端service实现类
 *
 * @author: chenming
 * @date: 2019年7月18日 下午3:59:16
 */
public interface UserCommentPcService {

	/**
	 * 新增评论
	 *
	 * @param dto      新增评论dto
	 * @param content  评论的内容对象
	 * @param request  request请求对象
	 * @param response response返回对象
	 * @throws GlobalException 全局异常
	 * @Title: save
	 * @return: void
	 */
	void save(UserCommentSaveDto dto, Content content, HttpServletRequest request,
			  HttpServletResponse response) throws GlobalException;

	/**
	 * 通过内容查询评论
	 *
	 * @param siteId     站点id
	 * @param contentId  内容id
	 * @param sortStatus 排序方式
	 * @param pageable   分页对象
	 * @throws GlobalException 全局异常
	 * @Title: getContent
	 * @return: Page
	 */
	Page<UserComment> getContent(Integer siteId, Integer contentId, Short sortStatus, Pageable pageable,
			HttpServletRequest request, CoreUser user) throws GlobalException;

	/**
	 * 查询手机端评论page集合(最新评论)
	 * @Title: getMobileContent  
	 * @param siteId		站点ID
	 * @param contentId		内容ID
	 * @param pageable		分页对象
	 * @param request		request请求
	 * @param user			用户user
	 * @throws GlobalException 	全局异常     
	 * @return: Page
	 */
	Page<UserComment> getMobileContent(Integer siteId,Integer contentId, Pageable pageable,
			HttpServletRequest request, CoreUser user) throws GlobalException;
	
	/**
	 * 查询手机端评论list集合(最热评论)
	 * @Title: getMobileContent  
	 * @param siteId		站点ID
	 * @param contentId		内容ID
	 * @param request		request请求
	 * @param user			用户user
	 * @throws GlobalException     全局异常
	 * @return: List
	 */
	List<UserComment> getMobileContent(Integer siteId,Integer contentId, 
			HttpServletRequest request, CoreUser user) throws GlobalException;
	
	/**
	 * 查询单个评论详情
	 * @Title: getComment  
	 * @param id		评论ID
	 * @param user		用户user
	 * @param request	request请求
	 * @param siteId	站点ID
	 * @throws GlobalException   全局异常  
	 * @return: UserComment
	 */
	UserComment getComment(Integer id, CoreUser user, HttpServletRequest request, Integer siteId) throws GlobalException;
	
	/**
	 * 评论/回复点赞
	 * @param user      用户
	 * @param commentId 评论/回复id
	 * @param request   {@link HttpServletRequest}
	 * @param response  {@link HttpServletResponse}
	 * @throws GlobalException 异常
	 */
	void like(CoreUser user, Integer commentId, HttpServletRequest request,
			  HttpServletResponse response) throws GlobalException;

	/**
	 * 评论/回复取消点赞
	 * @param user      用户
	 * @param commentId 评论/回复id
	 * @param request   {@link HttpServletRequest}
	 * @param response  {@link HttpServletResponse}
	 * @throws GlobalException 异常
	 */
	void cancelLike(CoreUser user, Integer commentId, HttpServletRequest request,
					HttpServletResponse response) throws GlobalException;
	
	/**
	 * 查询内容下评论数量
	 * @Title: count  
	 * @param contentId	内容ID
	 * @param isAll		是否查询所有评论(不区分是否推荐的已审核内容)
	 * @param isTop		是否推荐(true-最热，false-最新)
	 * @return: Integer
	 */
	Integer count(Integer contentId,boolean isAll,boolean isTop);
}
