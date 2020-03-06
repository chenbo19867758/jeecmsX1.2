package com.jeecms.process.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.ProcessOldDataService;
import com.jeecms.interact.dao.UserCommentDao;
import com.jeecms.interact.domain.UserComment;
import com.jeecms.interact.service.UserCommentService;

/**
 * 处理老数据(后期有需求部分地方需要修改，需要匹配之前的老数据)
 * @author: chenming
 * @date:   2019年12月9日 上午11:00:06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcessOldDataServiceImpl implements ProcessOldDataService{
	@Autowired
	private UserCommentDao userCommentDao;
	@Autowired
	private UserCommentService commentService;
	
	@Override
	public void processUserCommentArea() {
		List<UserComment> comments = userCommentDao.findByVisitorAreaIsNullAndIpIsNotNullAndHasDeleted(false);
		if (comments != null && comments.size() > 0) {
			for (UserComment userComment : comments) {
				userComment.setVisitorArea(commentService.getVersionArea(userComment.getIp()));
			}
			try {
				commentService.batchUpdate(comments);
			} catch (GlobalException e) {
				e.printStackTrace();
			}
		}
		
	}

}
