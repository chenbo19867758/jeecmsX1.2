package com.jeecms.wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.api.applet.MemberTesterApiService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.applet.BindTesterRequest;
import com.jeecms.common.wechat.bean.request.applet.UnbindTesterRequest;
import com.jeecms.wechat.dao.MiniprogramMemberDao;
import com.jeecms.wechat.domain.MiniprogramMember;
import com.jeecms.wechat.service.MiniprogramMemberService;

/**
 * 小程序成员管理service层实现类
 * @author: chenming
 * @date:   2019年6月13日 上午11:26:29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MiniprogramMemberServiceImpl extends BaseServiceImpl<MiniprogramMember, MiniprogramMemberDao, Integer>
		implements MiniprogramMemberService {

	@Autowired
	private MemberTesterApiService mTesterApiService;

	private ValidateToken validateToken;

	private ValidateToken getToken(String appId) {
		synchronized (appId) {
			validateToken = new ValidateToken();
			validateToken.setAppId(appId);
		}
		return validateToken;
	}

	@Override
	public void addMember(String appId, String wechatId) throws GlobalException {
		synchronized (appId) {
			validateToken = this.getToken(appId);
			MiniprogramMember bean = new MiniprogramMember();
			bean.setWechatId(wechatId);
			bean.setAppId(appId);
			MiniprogramMember newMember = super.save(bean);
			UnbindTesterRequest uRequest = new UnbindTesterRequest(wechatId);
			mTesterApiService.unbindTester(uRequest, validateToken);
			if (newMember != null) {
				BindTesterRequest bRequest = new BindTesterRequest(wechatId);
				mTesterApiService.bindTester(bRequest, validateToken);
			}
		}
	}

	@Override
	public void deleteMember(MiniprogramMember miniprogramMember) throws GlobalException {
		synchronized (miniprogramMember.getAppId()) {
			validateToken = this.getToken(miniprogramMember.getAppId());
			super.physicalDelete(miniprogramMember.getId());
			UnbindTesterRequest uRequest = new UnbindTesterRequest(miniprogramMember.getWechatId());
			mTesterApiService.unbindTester(uRequest, validateToken);
		}
	}

	@Override
	public List<MiniprogramMember> getMember(String appId) throws GlobalException {
		return dao.getMember(appId);
	}

	@Override
	public Boolean isExist(String appId, String wechatId) throws GlobalException {
		Boolean statue;
		if (dao.isExist(appId, wechatId).size() > 0) {
			statue = true;
		} else {
			statue = false;
		}
		return statue;
	}

}