/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.wechat.api.mp;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.comment.CommentListRequest;
import com.jeecms.common.wechat.bean.request.mp.comment.CommentRequest;
import com.jeecms.common.wechat.bean.request.mp.comment.MarkelectCommentRequest;
import com.jeecms.common.wechat.bean.response.mp.BaseResponse;
import com.jeecms.common.wechat.bean.response.mp.comment.CommentResponse;

/**   
 * 微信留言请求接口
 * @author: ljw
 * @date:   2019年5月29日 下午3:27:55     
 */
public interface CommentService {

	/**
	 *  打开已群发文章评论
	* @Title: openComment 
	* @param commentRequest 请求对象
	* @param validToken 令牌
	* @return BaseResponse 响应
	* @throws GlobalException 异常
	 */
	BaseResponse openComment(CommentRequest commentRequest, ValidateToken validToken)
			throws GlobalException;
	
	/**
	 *  关闭已群发文章评论
	* @Title: openComment 
	* @param commentRequest 请求对象
	* @param validToken 令牌
	* @return BaseResponse 响应
	* @throws GlobalException 异常
	 */
	BaseResponse closeComment(CommentRequest commentRequest, ValidateToken validToken)
			throws GlobalException;
	
	/**
	 *  查看指定文章的评论数据
	* @Title: openComment 
	* @param commentListRequest 请求对象
	* @param validToken 令牌
	* @return BaseResponse 响应
	* @throws GlobalException 异常
	 */
	CommentResponse commentList(CommentListRequest commentListRequest, ValidateToken validToken)
			throws GlobalException;
	
	/**
	 *   将评论标记精选
	* @Title: openComment 
	* @param markelect 请求对象
	* @param validToken 令牌
	* @return BaseResponse 响应
	* @throws GlobalException 异常
	 */
	BaseResponse markelect(MarkelectCommentRequest markelect, ValidateToken validToken)
			throws GlobalException;
	
	/**
	 *  将评论取消精选
	* @Title: openComment 
	* @param unmarkelect 请求对象
	* @param validToken 令牌
	* @return BaseResponse 响应
	* @throws GlobalException 异常
	 */
	BaseResponse unmarkelect(MarkelectCommentRequest unmarkelect, ValidateToken validToken)
			throws GlobalException;
	
	/**
	 *  删除评论
	* @Title: openComment 
	* @param delete 请求对象
	* @param validToken 令牌
	* @return BaseResponse 响应
	* @throws GlobalException 异常
	 */
	BaseResponse delete(MarkelectCommentRequest delete, ValidateToken validToken)
			throws GlobalException;
	
	/**
	 *  回复评论
	* @Title: openComment 
	* @param reply 请求对象
	* @param validToken 令牌
	* @return BaseResponse 响应
	* @throws GlobalException 异常
	 */
	BaseResponse reply(MarkelectCommentRequest reply, ValidateToken validToken)
			throws GlobalException;
	
	/**
	 *  删除回复
	* @Title: openComment 
	* @param deleteReply 请求对象
	* @param validToken 令牌
	* @return BaseResponse 响应
	* @throws GlobalException 异常
	 */
	BaseResponse deleteReply(MarkelectCommentRequest deleteReply, ValidateToken validToken)
			throws GlobalException;
	
}
