/*   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.interact.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.interact.dao.ext.UserCommentReportDaoExt;
import com.jeecms.interact.domain.UserCommentReport;

/**
 * 评论举报dao接口
 * @author: chenming
 * @date:   2019年6月15日 下午3:20:34
 */
public interface UserCommentReportDao extends IBaseDao<UserCommentReport, Integer>,UserCommentReportDaoExt {
	
	/**
	 * 通过评论id查询该评论的举报信息的列表
	 * @Title: findByCommentId  
	 * @param commentId
	 * @return: List
	 */
	List<UserCommentReport> findByCommentIdIn(Integer[] commentIds);
	
	List<UserCommentReport> findByReplyUserIdAndHasDeleted(Integer userId,Boolean deleted);
}
