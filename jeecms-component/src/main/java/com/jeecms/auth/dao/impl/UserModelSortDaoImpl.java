/**
 * @date: 2018年5月31日 上午10:18:47
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jeecms.auth.dao.ext.UserModelSortDaoExt;
import com.jeecms.auth.domain.UserModelSort;
import com.jeecms.auth.domain.querydsl.QUserModelSort;
import com.jeecms.common.base.dao.BaseDao;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * Dao实现接口
 * @author: ljw
 */
public class UserModelSortDaoImpl extends BaseDao<UserModelSort> implements UserModelSortDaoExt {

	@Override
	public List<UserModelSort> getSortList(Integer userId) {
		BooleanBuilder boolbuild = new BooleanBuilder();
		QUserModelSort qSort = QUserModelSort.userModelSort;
		if (userId != null) {
			boolbuild.and(qSort.userId.eq(userId));
		}
		//这里采用两条SQL语句查询，不用子查询的意思在于统计时间可能为Null,会出现报错；
		JPAQuery<Date> query1 = super.getJpaQueryFactory().select(qSort.statisticsDay).from(qSort)
			.where(boolbuild).groupBy(qSort.statisticsDay).orderBy(qSort.statisticsDay.desc());
		Date date = query1.fetchFirst();
		if (date != null) {
			boolbuild.and(qSort.statisticsDay.eq(date));
		} else {
			return new ArrayList<UserModelSort>();
		}
		JPAQuery<UserModelSort> query = super.getJpaQueryFactory().selectFrom(qSort).where(boolbuild)
			.orderBy(qSort.sort.desc());
		return query.fetch();
	}

}
