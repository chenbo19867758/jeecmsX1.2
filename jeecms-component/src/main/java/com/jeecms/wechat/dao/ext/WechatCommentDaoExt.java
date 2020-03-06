/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.wechat.dao.ext;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.domain.WechatComment;

/**   
 * 微信留言扩展
 * @author: ljw
 * @date:   2019年6月3日 上午10:10:32     
 */
public interface WechatCommentDaoExt {

	/**
	 * 微信留言分页
	* @Title: getPages 
	* @param appids 所属公众号IDs
	* @param commentType 是否精选评论
	* @param startTime 评论开始时间
	* @param endTime 评论结束时间
	* @param orderType 排序方式
	* @param comment 评论内容
	* @param pageable 分页对象
	* @param msgDataId 群发ID
	* @param msgDataIndex 文章序号
	* @return Page 
	* @throws GlobalException 异常
	 */
	Page<WechatComment> getPages(List<String> appids, Boolean commentType, 
			Date startTime, Date endTime, Integer orderType,
			String comment, String msgDataId, Integer msgDataIndex, Pageable pageable) 
					throws GlobalException;
	
	/**
	 * 根据群发ID和文章序号查询最大评论数
	* @Title: getUserCommentId 
	 * @param msgDataId 群发id
	 * @param index 文章序号
	* @return
	 */
	Long getUserCommentId(String msgDataId, Integer index);
}
