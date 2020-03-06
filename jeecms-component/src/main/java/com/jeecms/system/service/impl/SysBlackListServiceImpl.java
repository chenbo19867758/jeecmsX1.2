/*  
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.interact.domain.UserComment;
import com.jeecms.interact.service.UserCommentService;
import com.jeecms.system.dao.SysBlackListDao;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysBlackList;
import com.jeecms.system.service.CmsSiteService;
import com.jeecms.system.service.SysBlackListService;

/**
 * 黑名单service实现类
 * 
 * @author: chenming
 * @date: 2019年5月6日 上午8:58:42
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysBlackListServiceImpl extends BaseServiceImpl<SysBlackList, SysBlackListDao, Integer>
		implements SysBlackListService {

	@Autowired
	private CmsSiteService cmsSiteService;
	@Autowired
	private UserCommentService userCommentService;
	@Autowired
	private CoreUserService coreUserService;

	@Override
	public boolean checkUserComment(Integer siteId, Integer userId, String ip) {
		List<SysBlackList> sLists = dao.findByIpAndUserId(siteId, ip, userId);
		boolean returnStatus = sLists.size() > 0 ? false : true;
		return returnStatus;
	}

	@Override
	public List<SysBlackList> findBySiteId(Integer siteId, Integer type) {
		return dao.findBySiteIdAndType(siteId, type);
	}

	@Override
	public Page<SysBlackList> getPage(boolean status, String userName, String ip, 
			Integer siteId, Pageable pageable) {
		Page<SysBlackList> sPage = dao.getPage(status, userName, ip, siteId, pageable);
		if (status) {
			// 查询出当前系统的所有站点信息以及其对应的站点名称
			Map<Integer, String> siteMap = cmsSiteService.findAll(false,true).stream()
					.collect(Collectors.toMap(CmsSite::getId, CmsSite::getName));
			CoreUser user = null;
			Integer userSiteId = null;
			for (SysBlackList sysBlackList : sPage) {
				user = coreUserService.findById(sysBlackList.getUserId());
				if (user != null) {
					userSiteId = user.getSourceSiteId();
					sysBlackList.setUserSiteName(siteMap.get(userSiteId));
				}
			}
		}
		return sPage;
	}

	@Override
	public SysBlackList findCommentList(Integer id) {
		SysBlackList sysBlackList = super.findById(id);
		List<UserComment> uList = null;
		if (sysBlackList.getIp() != null) {
			uList = userCommentService.findByIp(sysBlackList.getIp());
		}
		if (sysBlackList.getUserId() != null) {
			uList = userCommentService.findByUserId(sysBlackList.getUserId());
		}
		sysBlackList.setUserComments(uList);
		return sysBlackList;
	}

	@Override
	public SysBlackList findByUserIdByIp(Integer siteId, Integer type, Integer userId, String ip) {
		SysBlackList sysBlackList = null;
		if (userId != null) {
			sysBlackList = dao.findBySiteIdAndTypeAndUserId(siteId, type, userId);
		}
		if (ip != null) {
			sysBlackList = dao.findBySiteIdAndTypeAndIp(siteId, type, ip);
		}
		return sysBlackList;
	}

}