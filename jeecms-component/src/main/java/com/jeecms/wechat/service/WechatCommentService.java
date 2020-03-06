/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.wechat.service;

import com.jeecms.wechat.domain.WechatComment;
import com.jeecms.wechat.domain.dto.WechatCommentDto;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;

/**
 * 微信留言Service
* @author ljw
* @version 1.0
* @date 2019-05-31
*/
public interface WechatCommentService extends IBaseService<WechatComment, Integer> {

	/**
	 * 开启留言
	* @Title: on 
	* @param appId 公众号ID
	* @param msgDataId 群发ID
	* @param msgDataIndex 文章序号
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	ResponseInfo on(String appId, Long msgDataId, Long msgDataIndex) throws GlobalException;
	
	/**
	 * 关闭留言
	* @Title: off 
	* @param appId 公众号ID
	* @param msgDataId 群发ID
	* @param msgDataIndex 文章序号
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	ResponseInfo off(String appId, Long msgDataId, Long msgDataIndex) throws GlobalException;
	
	/**
	 * 微信留言分页
	* @Title: getPages 
	* @param appids 所属公众号IDs
	* @param commentType 是否精选评论
	* @param startTime 评论开始时间
	* @param endTime 评论结束时间
	* @param orderType 排序方式
	* @param comment 评论内容
	* @param msgDataId 群发ID
	* @param msgDataIndex 文章序号
	* @param pageable 分页对象
	* @return Page 
	* @throws GlobalException 异常
	 */
	Page<WechatComment> getPages(List<String> appids, Boolean commentType, 
			Date startTime, Date endTime, Integer orderType,
			String comment,  String msgDataId, Integer msgDataIndex, Pageable pageable) 
					throws GlobalException;

	/**
	 * 评论设为精选
	* @Title: markelect 
	* @param id 留言ID
	* @param fansId 粉丝ID
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	ResponseInfo markelect(Integer id, Integer fansId) throws GlobalException;
	
	/**
	 * 取消评论精选
	* @Title: markelect 
	* @param id 留言ID
	* @param fansId 粉丝ID
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	ResponseInfo unmarkelect(Integer id, Integer fansId) throws GlobalException;
	
	/**
	 * 回复评论
	* @Title: reply 
	* @param dto 留言Dto
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	ResponseInfo reply(WechatCommentDto dto) throws GlobalException;
	
	/**
	 * 删除回复
	* @Title: deleteReply 
	* @param id 留言id
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	ResponseInfo deleteReply(Integer id) throws GlobalException;
	
	/**
	 * 删除评论
	* @Title: deleteComment 
	* @param id 留言ID
	* @return ResponseInfo 返回
	* @throws GlobalException 异常
	 */
	ResponseInfo deleteComment(Integer id) throws GlobalException;
	
	/**
	 *手动同步
	* @Title: hand 
	* @param appids 留言IDs
	* @param start 开始时间
	* @param end 结束时间
	* @throws GlobalException 异常
	 */
	void hand(List<String> appids, Date start, Date end) throws GlobalException;
	
	/**
	 * 获取最大评论数
	* @Title: getUserCommentId 
	* @param msgDataId 群发ID
	* @param msgDataIndex 文章序号
	* @return
	 */
	Long getUserCommentId(String msgDataId, Integer msgDataIndex);
	
	/**
	 * 根据用户的openId获取留言数
	* @Title: findByOpenId 
	* @param openId 粉丝OPENID
	* @return
	 */
	List<WechatComment> findByOpenId(String openId);
}
