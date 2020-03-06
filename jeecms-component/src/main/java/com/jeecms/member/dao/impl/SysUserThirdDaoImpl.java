/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jeecms.auth.domain.querydsl.QCoreUser;
import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.member.dao.ext.SysUserThirdDaoExt;
import com.jeecms.member.domain.SysUserThird;
import com.jeecms.member.domain.querydsl.QSysUserThird;
import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * 用户第三方配置DaoExt
 * @author: ljw
 * @date: 2019年3月1日 下午1:50:33
 */
public class SysUserThirdDaoImpl extends BaseDao<SysUserThird> implements SysUserThirdDaoExt {

	@Override
	public SysUserThird findByMemberIdAndThirdTypeCode(Integer memberId, String thirdTypeCode, Boolean hasDeleted)
			throws GlobalException {
		QSysUserThird third = QSysUserThird.sysUserThird;
		QCoreUser member = QCoreUser.coreUser;
		BooleanExpression condition = third.hasDeleted.eq(hasDeleted).and(member.hasDeleted.eq(hasDeleted));
		if (memberId != null) {
			condition = condition.and(third.memberId.eq(memberId));
		}
		if (StringUtils.isNotBlank(thirdTypeCode)) {
			condition = condition.and(third.thirdTypeCode.eq(thirdTypeCode));
		}
		return getJpaQueryFactory().select(third).from(third).innerJoin(third.member, member).where(condition)
				.fetchFirst();
	}

	@Override
	public List<SysUserThird> listByTypeCodeAndMember(Integer memberId, String thirdTypeCode, boolean hasDeleted)
			throws GlobalException {
		QSysUserThird third = QSysUserThird.sysUserThird;
		QCoreUser member = QCoreUser.coreUser;
		BooleanExpression condition = third.hasDeleted.eq(hasDeleted).and(member.hasDeleted.eq(hasDeleted));
		if (memberId != null) {
			condition = condition.and(third.memberId.eq(memberId));
		}
		if (StringUtils.isNotBlank(thirdTypeCode)) {
			condition = condition.and(third.thirdTypeCode.eq(thirdTypeCode));
		}
		return getJpaQueryFactory().select(third).from(third)
				.innerJoin(third.member, member).where(condition).fetch();
	}

	@Override
	public List<SysUserThird> listByThirdIdAndTypeCode(String thirdId, String typeCode, Boolean hasDeleted)
			throws GlobalException {
		QSysUserThird third = QSysUserThird.sysUserThird;
		QCoreUser member = QCoreUser.coreUser;
		BooleanExpression condition = third.hasDeleted.eq(hasDeleted).and(member.hasDeleted.eq(hasDeleted));
		if (StringUtils.isNotBlank(thirdId)) {
			condition = condition.and(third.thirdId.eq(thirdId));
		}
		if (StringUtils.isNotBlank(typeCode)) {
			condition = condition.and(third.thirdTypeCode.eq(typeCode));
		}
		return getJpaQueryFactory().select(third).from(third)
				.innerJoin(third.member, member).where(condition).fetch();
	}

}
