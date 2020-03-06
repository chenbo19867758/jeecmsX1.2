/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.service.impl;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.member.dao.UserCollectionDao;
import com.jeecms.member.domain.UserCollection;
import com.jeecms.member.service.UserCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户收藏service实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCollectionServiceImpl extends BaseServiceImpl<UserCollection, UserCollectionDao, Integer>
		implements UserCollectionService {

	@Autowired
	UserCollectionDao dao;

	@Override
	public Page<UserCollection> getPage(String title, Date startTime, Date endTime
			, Integer userId, Pageable pageable) {
		return dao.getList(title, startTime, endTime, userId, pageable);
	}

	@Override
	public boolean isHaveCollection(Integer contentId, Integer userId) {
		return dao.getCount(contentId, userId) > 0;
	}

	@Override
	public UserCollection findByContentIdAndUserId(Integer contentId, Integer userId) {
		return dao.findByContentIdAndUserId(contentId, userId);
	}

	@Override
	public List<UserCollection> findAllByUserId(Integer userId) {
		return dao.findAllByUserId(userId);
	}
}