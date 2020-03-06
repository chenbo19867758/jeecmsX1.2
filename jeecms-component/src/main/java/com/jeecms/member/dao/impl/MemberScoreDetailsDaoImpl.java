/**
 *  * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.member.dao.ext.MemberLevelDaoExt;
import com.jeecms.member.dao.ext.MemberScoreDetailsDaoExt;
import com.jeecms.member.domain.querydsl.QMemberScoreDetails;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * 等级会员扩展dao层实现类
 * 
 * @author: ljw
 * @date: 2019年07月23日 下午3:36:46
 */
@Repository
public class MemberScoreDetailsDaoImpl extends BaseDao<Integer> implements MemberScoreDetailsDaoExt {

	@Override
	public Integer scoreSum(Integer userId, Date startTime, Date endTime) {
		Integer sum = 0;
		QMemberScoreDetails details = QMemberScoreDetails.memberScoreDetails;
		BooleanBuilder boolbuild = new BooleanBuilder();
		if (userId != null) {
			boolbuild.and(details.userId.eq(userId));
		}
		if (startTime != null && endTime != null) {
			boolbuild.and(details.createTime.between(startTime, endTime));
		}
		JPAQuery<Integer> query = getJpaQueryFactory().select(details.score.sum())
				.from(details).where(boolbuild);
		if (query.fetchFirst() != null) {
			sum = query.fetchFirst();
		}
		return sum;
	}

}
