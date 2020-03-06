/*  
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.interact.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.interact.dao.UserCommentReportDao;
import com.jeecms.interact.domain.UserCommentReport;
import com.jeecms.interact.service.UserCommentReportService;

/**
 * 评论举报service实现类
 * 
 * @author: chenming
 * @date: 2019年6月15日 下午3:23:05
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCommentReportServiceImpl extends BaseServiceImpl<UserCommentReport, UserCommentReportDao, Integer>
		implements UserCommentReportService {

	@Override
	public List<UserCommentReport> findByCommentId(List<Integer> commentIds) {
		
		return dao.findByCommentIdIn(commentIds.toArray(new Integer[commentIds.size()]));
	}

	@Override
	public List<UserCommentReport> findByReplyUserId(Integer replyUserId) {
		return dao.findByReplyUserIdAndHasDeleted(replyUserId, false);
	}

	@Override
	public List<Integer> findByDeleted() {
		return dao.findByDeleted();
	}

}