package com.jeecms.interact.dao.ext;

import java.util.List;

/**
 * 评论举报dao扩展接口
 * @author: chenming
 * @date:   2019年9月19日 下午2:46:31
 */
public interface UserCommentReportDaoExt {
	
	List<Integer> findByDeleted();
}
