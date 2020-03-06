/**
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.dao.impl;

import org.springframework.stereotype.Repository;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.member.dao.ext.MemberLevelDaoExt;
import com.jeecms.member.domain.MemberLevel;
import com.jeecms.member.domain.querydsl.QMemberLevel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * 等级会员扩展dao层实现类
 * @author: ljw
 * @date:   2019年07月23日 下午3:36:46     
 */
@Repository 
public class MemberLevelDaoImpl extends BaseDao<Integer> implements MemberLevelDaoExt {

	@Override
	public MemberLevel findBySecore(Integer secore) throws GlobalException {
		QMemberLevel level = QMemberLevel.memberLevel;
		BooleanBuilder boolbuild = new BooleanBuilder();
		if (secore != null) {
			boolbuild.and(level.integralMin.loe(secore))
				.and(level.integralMax.gt(secore));
		}
		JPAQuery<MemberLevel> query = getJpaQueryFactory().selectFrom(level);
		return query.fetchFirst();
	}

}
