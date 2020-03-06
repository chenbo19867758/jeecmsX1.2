package com.jeecms.member.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MathUtil;
import com.jeecms.member.dao.MemberMessageDao;
import com.jeecms.member.domain.MemberMessage;
import com.jeecms.member.domain.vo.front.MemberMessageVO;
import com.jeecms.member.service.MemberMessageService;
import com.jeecms.system.domain.vo.MessageVo;

/**
 * 用户接收信息状态
 * 
 * @author ljw
 * @version 1.0
 * @date 2018-09-25
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberMessageServiceImpl extends BaseServiceImpl<MemberMessage, MemberMessageDao, Integer>
		implements MemberMessageService {

	@Override
	public ResponseInfo getSysMessagePage(Integer groupId, Integer levelId, Pageable pageable) throws GlobalException {

		Page<MessageVo> page = dao.getSysMessagePage(groupId,levelId, pageable);

		return new ResponseInfo(page);
	}

	@Override
	public ResponseInfo getPriMessagePage(Integer memberId, Pageable pageable) throws GlobalException {

		Page<MessageVo> page = dao.getPriMessagePage(memberId, pageable);

		return new ResponseInfo(page);
	}

	@Override
	public List<MemberMessage> findByMessageId(Integer messageId) {

		return dao.findByMessageId(messageId);
	}

	@Override
	public ResponseInfo delMemberMes(Integer memberId, Integer[] ids) throws GlobalException {

		for (Integer integer : ids) {
			MemberMessage bean = new MemberMessage();
			List<MemberMessage> list = dao.findByMessageId(integer);
			// 已读的标记删除
			if (list != null && !list.isEmpty()) {
				for (MemberMessage memberMessage : list) {
					memberMessage.setStatus(2);
					super.update(memberMessage);
				}
			} else {
				// 未读的标记删除
				bean.setStatus(2);
				bean.setMemberId(memberId);
				;
				bean.setMessageId(integer);
				super.save(bean);
			}
		}
		return new ResponseInfo();
	}

	@Override
	public Long unreadNumSys(Integer groupId,Integer levelId) throws GlobalException {
		Long sum = dao.unreadNumSys(groupId,levelId);
		return sum;
	}

	@Override
	public Long unreadNumPri(Integer memberId) throws GlobalException {
		Long sum = dao.unreadNumPri(memberId);
		return sum;
	}

	@Override
	public MemberMessageVO messageInfo(Integer groupId,Integer levelId, Integer memberId) throws GlobalException {
		Long unreadSys = memMessageService.unreadNumSys(groupId,levelId);
		Long unreadPro = memMessageService.unreadNumPri(memberId);
		BigDecimal unreadNum = MathUtil.add(BigDecimal.valueOf(unreadSys), BigDecimal.valueOf(unreadPro));
		return new MemberMessageVO(unreadNum.longValue());
	}

	@Autowired
	private MemberMessageService memMessageService;

}