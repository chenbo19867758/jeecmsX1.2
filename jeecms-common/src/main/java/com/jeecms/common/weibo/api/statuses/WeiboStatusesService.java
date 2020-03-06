/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.common.weibo.api.statuses;

import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.weibo.bean.request.statuses.StatusesRequest;
import com.jeecms.common.weibo.bean.response.comment.vo.CommentVO;
import com.jeecms.common.weibo.bean.response.statuses.vo.StatusesVO;

/**   
 * 微博相关接口
 * @author: ljw
 * @date:   2019年6月19日 下午3:48:14     
 */
public interface WeiboStatusesService {

	/**
	 * 获取@ 我的微博信息
	* @Title: getInfo 
	* @param request 请求
	* @return StatusesResponse
	* @throws GlobalException 异常
	 */
	List<StatusesVO> getInfo(StatusesRequest request) throws GlobalException;
	
	/**
	 * 处理评论和回复数据
	* @Title: dealComment 
	* @param comments 评论列表
	* @return
	 */
	public List<CommentVO> dealComment(List<CommentVO> comments);
}
