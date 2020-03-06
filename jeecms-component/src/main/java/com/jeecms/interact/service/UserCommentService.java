/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.interact.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.interact.domain.UserComment;
import com.jeecms.interact.domain.dto.UserCommentDto;
import com.jeecms.interact.domain.dto.UserCommentTopDto;
import com.jeecms.interact.domain.vo.UserCommentCountVo;
import com.jeecms.interact.domain.vo.UserInteractionMoblieVo;
import com.jeecms.interact.domain.vo.UserInteractionVo;
import com.jeecms.system.domain.CmsSite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户评论service接口
 * 
 * @author: chenming
 * @date: 2019年5月6日 上午9:03:06
 */
public interface UserCommentService extends IBaseService<UserComment, Integer> {

	/**
	 * 通过查询检索出list集合
	 * @Title: findByList  
	 * @param siteId	站点Id
	 * @param status	审核状态
	 * @param isTop		是否推荐
	 * @param isReply	回复状态
	 * @param channelId	栏目Id
	 * @param startTime	起始评论时间
	 * @param endTime	截止评论时间
	 * @param replyStartTime	起始回复时间
	 * @param replyEndTime		截止回复时间
	 * @param fuzzySearch	模糊查询字段
	 * @param userName		评论人名称
	 * @param ip	评论Ip
	 * @param commentText	评论内容
	 * @param replytText	回复内容
	 * @param title		文章标题
	 * @param pageable	分页信息
	 * @throws GlobalException      全局异常
	 * @return: Page
	 */
	Page<UserComment> findByList(Integer siteId, Short status, Boolean isTop, Boolean isReply, Integer channelId, 
		Date startTime, Date endTime,Date replyStartTime, Date replyEndTime, String fuzzySearch, 
		String userName, String ip, String commentText, String replytText, String title,
		Pageable pageable) throws GlobalException;
	
	/**
	 * 特定条件通过条件精准查询检索出list集合
	 * @Title: findByList  
	 * @Title: findByList  
	 * @param siteId	站点Id
	 * @param status	审核状态
	 * @param isTop		是否推荐
	 * @param isReply	回复状态
	 * @param channelId	栏目Id
	 * @param startTime	起始评论时间
	 * @param endTime	截止评论时间
	 * @param replyStartTime	起始回复时间
	 * @param replyEndTime		截止回复时间
	 * @param fuzzySearch	模糊查询字段
	 * @param userName		评论人名称
	 * @param ip	评论Ip
	 * @param commentText	评论内容
	 * @param replytText	回复内容
	 * @param title		文章标题
	 * @param contentId	文章内容Id
	 * @param userId	用户id
	 * @param precisionIp	精准检索IP值
	 * @param pageable	分页条件
	 * @throws GlobalException   全局异常  
	 * @return: Page
	 */
	Page<UserComment> findByList(Integer siteId, Short status, Boolean isTop, Boolean isReply, Integer channelId, 
			Date startTime, Date endTime,Date replyStartTime, Date replyEndTime, String fuzzySearch, 
			String userName, String ip, String commentText, String replytText, String title,
			Integer contentId, Integer userId, String precisionIp, 
			Pageable pageable) throws GlobalException;
	
	/**
	 * 举报列表
	 * @Title: findByList  
	 * @param siteId	站点id
	 * @return: Page
	 */
	Page<UserComment> findByList(Integer siteId,Pageable pageable) throws GlobalException;
	
	/**
	 * 新增或修改回复
	 * @Title: saveReply  
	 * @param userComment	评论对象(需要或回复的对象)
	 * @param dto     接受前台dto
	 * @return: void
	 */
	void saveOrUpdateReply(UserComment userComment,UserCommentDto dto,HttpServletRequest request,
			CmsSite site) throws GlobalException;
	
	/**
	 * 删除评论
	 * @Title: deleted  
	 * @param userComments	评论list集合
	 * @param ids	评论id集合
	 * @throws GlobalException	全局异常      
	 * @return: void
	 */
	void deleted(List<UserComment> userComments, Integer[] ids) throws GlobalException;
	
	/**
	 * 修改审核状态
	 * @Title: updateCheck  
	 * @param dto	接受dto
	 * @param siteId	站点Id
	 * @throws GlobalException   全局异常   
	 * @return: void
	 */
	void updateCheck(UserCommentDto dto, Integer siteId) throws GlobalException;
	
	/**
	 * 新增禁止黑名单
	 * @Title: saveStop  
	 * @param dto	接收dto
	 * @param site	站点对象
	 * @throws GlobalException   全局异常   
	 * @return: void
	 */
	void saveStop(UserCommentDto dto, CmsSite site) throws GlobalException;
	
	/**
	 * 删除禁止黑名单
	 * @Title: deleteStop  
	 * @param dto	接收dto
	 * @param siteId	站点Id
	 * @throws GlobalException 	全局异常     
	 * @return: void
	 */
	void deleteStop(UserCommentDto dto, Integer siteId) throws GlobalException;
	
	/**
	 * 根据userId查询出评论集合
	 * @Title: findByUserId  
	 * @param userId	用户Id
	 * @return: List
	 */
	List<UserComment> findByUserId(Integer userId);
	
	/**
	 * 根据ip查询出评论集合
	 * @Title: findByIp  
	 * @param ip	禁言ip
	 * @return: List
	 */
	List<UserComment> findByIp(String ip);

	/**
	 * 获取评论数量
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param siteId 站点id
	 * @param status 状态（0-待审核 1-审核通过 2-审核未通过）
	 * @return
	 */
	long getCount(Date beginTime, Date endTime, Integer siteId, Short status);
	
	/**
	 * 查询数量
	 * @Title: getCount  
	 * @param contentId		内容id
	 * @param userComments	评论list集合
	 * @param siteId		站点id
	 * @throws GlobalException   全局异常   
	 * @return: UserCommentCountVo
	 */
	UserCommentCountVo getCount(Integer contentId,List<UserComment> userComments,Integer siteId) throws GlobalException;
	
	/**
	 * 推荐与取消推荐
	 * @Title: top  
	 * @param dto	推荐dto
	 * @throws GlobalException   全局异常   
	 * @return: void
	 */
	void top(UserCommentTopDto dto) throws GlobalException;
	
	/**
	 * 互动列表分页
	 * @Title: interactPage  
	 * @param startTime	起始评论时间
	 * @param endTime	截止评论时间
	 * @param userId 用户ID
	 * @param pageable	分页信息
	 * @throws GlobalException      全局异常
	 * @return: Page
	 */
	Page<UserInteractionVo> interactPage(Date startTime, Date endTime,
			 Integer userId,Pageable pageable) throws GlobalException;
	
	/**
	 * 手机端互动列表分页
	 * @Title: mobilePage  
	 * @param userId 用户ID
	 * @param pageable	分页信息
	 * @throws GlobalException      全局异常
	 * @return: Page
	 */
	Page<UserInteractionMoblieVo> mobilePage(Integer userId,Pageable pageable) 
			throws GlobalException;
	
	/**
	 * 根据回复的评论的id值得到评论数
	 * @Title: getListByReplyCommentId  
	 * @param ids 评论ids
	 * @return: List
	 */
	List<UserComment> getListByReplyCommentId(List<Integer> ids);
	
	/**
	 * 一键清空我的互动
	 * @Title: clear  
	 * @param userId 用户ID
	 * @throws GlobalException   全局异常
	 */
	ResponseInfo clear(Integer userId) throws GlobalException;
	
	/**
	 * 通过IP获取到地址
	 * @Title: getVersionArea  
	 * @param ip	用户评论ip
	 * @return: String
	 */
	String getVersionArea(String ip);
}
