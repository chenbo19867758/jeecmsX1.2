/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.member.dao.MemberLevelDao;
import com.jeecms.member.domain.MemberLevel;
import com.jeecms.member.service.MemberLevelService;
import com.jeecms.resource.service.ResourcesSpaceDataService;

/**
 * 会员等级
 * 
 * @author: ztx
 * @date: 2018年6月7日 上午11:53:20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberLevelServiceImpl extends BaseServiceImpl<MemberLevel, MemberLevelDao, Integer>
		implements MemberLevelService {

	@Override
	public MemberLevel saveMemberLevel(MemberLevel memberLevel) throws GlobalException {
		this.checkIntegralRange(memberLevel);
		if (memberLevel.getLevelIcon() != null) {
			memberLevel.setLogoResource(resDataService.findById(memberLevel.getLevelIcon()));
		}
		MemberLevel level = super.save(memberLevel);
		return level;
	}

	@Override
	public MemberLevel updateMemberLevel(MemberLevel memberLevel) throws GlobalException {
		this.checkIntegralRange(memberLevel);
		if (memberLevel.getLevelIcon() != null) {
			memberLevel.setLogoResource(resDataService.findById(memberLevel.getLevelIcon()));
		} else {
			memberLevel.setLogoResource(null);
		}
		memberLevel = super.updateAll(memberLevel);
		return memberLevel;
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	@Override
	public MemberLevel findByLevelName(String levelName) throws GlobalException {
		MemberLevel result = dao.findByLevelNameAndHasDeleted(levelName, false);
		return result;
	}

	/**
	 * 校验积分范围，最小值不能大于最大值<br>
	 * 积分范围不能超过六位数
	 * 
	 * @Title: checkIntegralRange
	 * @param memberLevel 会员等级
	 * @throws GlobalException 异常
	 * @return: void
	 */
	public void checkIntegralRange(MemberLevel memberLevel) throws GlobalException {
		final Integer length = 6;
		if (memberLevel.getIntegralMax() < memberLevel.getIntegralMin()) {
			throw new GlobalException(
					new SystemExceptionInfo(
							SettingErrorCodeEnum.MIN_CANNOT_EXCEED_MAX_ERROT
							.getDefaultMessage(),
							SettingErrorCodeEnum.MIN_CANNOT_EXCEED_MAX_ERROT
							.getCode()));
		}

		if (memberLevel.getIntegralMax().toString().length() > length
				|| memberLevel.getIntegralMin().toString().length() > length) {
			throw new GlobalException(
					new SystemExceptionInfo(
							SettingErrorCodeEnum.INTEGRAL_EXCEED_RANGE_ERROR
							.getDefaultMessage(),
							SettingErrorCodeEnum.INTEGRAL_EXCEED_RANGE_ERROR.getCode()));
		}
	}
	
	@Override
	public MemberLevel findBySecore(Integer secore) throws GlobalException {
		return dao.findBySecore(secore);
	}

	@Autowired
	private ResourcesSpaceDataService resDataService;

}