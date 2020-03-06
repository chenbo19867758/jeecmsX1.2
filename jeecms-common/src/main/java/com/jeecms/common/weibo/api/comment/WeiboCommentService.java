/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.api.comment;

import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.weibo.bean.request.comment.CommentCreateRequest;
import com.jeecms.common.weibo.bean.request.comment.DeleteCommentRequest;
import com.jeecms.common.weibo.bean.request.comment.ReplyCommentRequest;
import com.jeecms.common.weibo.bean.request.comment.UserCommentRequest;
import com.jeecms.common.weibo.bean.request.comment.WeiboCommentRequest;
import com.jeecms.common.weibo.bean.response.comment.vo.CommentVO;

/**   
 * 微博评论Service
 * @author: ljw
 * @date:   2019年6月18日 下午2:45:10     
 */
public interface WeiboCommentService {

	/**
	 * @ 我的评论
	* @Title: getInfo 
	* @param request 请求
	* @return List 响应
	* @throws GlobalException 异常
	 */
	List<CommentVO> getInfo(UserCommentRequest request) throws GlobalException;
	
	/**
	 * 我发出的评论
	* @Title: getInfo 
	* @param request 请求
	* @return List 响应
	* @throws GlobalException 异常
	 */
	List<CommentVO> sendInfo(UserCommentRequest request) throws GlobalException;
	
	/**
	 * 我收到评论
	* @Title: getInfo 
	* @param request 请求
	* @return List 响应
	* @throws GlobalException 异常
	 */
	List<CommentVO> receiveInfo(UserCommentRequest request) throws GlobalException;
	
	/**
	 * 根据微博ID返回某条微博的评论列表
	* @Title: getInfo 
	* @param request 请求
	* @return List 响应
	* @throws GlobalException 异常
	 */
	List<CommentVO> findByWeiboID(WeiboCommentRequest request) throws GlobalException;
	
	/**
	 * 根据微博ID发表评论
	* @Title: getInfo 
	* @param request 请求
	* @return List 响应
	* @throws GlobalException 异常
	 */
	CommentVO createComment(CommentCreateRequest request) throws GlobalException;
	
	/**
	 * 根据评论ID删除评论（只能删除自己发的）
	* @Title: getInfo 
	* @param request 请求
	* @throws GlobalException 异常
	 */
	void deleteComment(DeleteCommentRequest request) throws GlobalException;
	
	/**
	 * 回复评论
	* @Title: getInfo 
	* @param request 请求
	* @return List 响应
	* @throws GlobalException 异常
	 */
	CommentVO replyComment(ReplyCommentRequest request) throws GlobalException;
}
