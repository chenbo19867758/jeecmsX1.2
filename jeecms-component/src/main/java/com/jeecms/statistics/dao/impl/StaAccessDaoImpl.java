/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.statistics.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.statistics.dao.ext.StaAccessDaoExt;
import com.jeecms.statistics.domain.StatisticsAccess;
import com.jeecms.statistics.domain.dto.StatisticsDto;
import com.jeecms.statistics.domain.querydsl.QStatisticsAccess;
import com.jeecms.statistics.domain.vo.AccessVo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * 忠诚度统计
 * @author: ljw
 * @date: 2019年6月27日 上午10:05:58
 */
public class StaAccessDaoImpl extends BaseDao<StatisticsAccess> implements StaAccessDaoExt {

	@Override
	public List<AccessVo> getAccessVo(StatisticsDto dto) throws GlobalException {
		QStatisticsAccess access = QStatisticsAccess.statisticsAccess;
		BooleanBuilder boolbuild = new BooleanBuilder();
		if (dto.getSiteId() != null) {
			boolbuild.and(access.siteId.eq(dto.getSiteId()));
		}
		if (dto.getStartTime() != null && dto.getEndTime() != null) {
			boolbuild.and(access.statisticsDay.between(MyDateUtils.formatDate(new Date(dto.getStartTime())),
					MyDateUtils.formatDate(new Date(dto.getEndTime()))));
		}
		if (dto.getSourceType() != null) {
			boolbuild.and(access.sorceUrlType.eq(dto.getSourceType()));
		}
		if (StringUtils.isNotBlank(dto.getProvince())) {
			boolbuild.and(access.visitorArea.like("%" + dto.getProvince()));
		}
		if (dto.getVisitor() != null) {
			boolbuild.and(access.isNewVisitor.eq(dto.getVisitor()));
		}
		if (dto.getType().equals(StatisticsDto.PAGE_TYPE)) {
			boolbuild.and(access.accessPage.isNotNull());
			JPAQuery<AccessVo> query = super.getJpaQueryFactory().select(
					Projections.bean(AccessVo.class, access.accessPage.as("key"), 
							access.accessCount.sum().as("count")))
					.from(access);
			query.where(boolbuild).groupBy(access.accessPage);
			return query.fetch();
		} else if (dto.getType().equals(StatisticsDto.HIGH_TYPE)) {
			boolbuild.and(access.depthPage.isNotNull());
			JPAQuery<AccessVo> query = super.getJpaQueryFactory().select(
					Projections.bean(AccessVo.class, access.depthPage.as("key"), 
							access.accessCount.sum().as("count")))
					.from(access);
			query.where(boolbuild).groupBy(access.depthPage,access.accessCount);
			return query.fetch();
		} else {
			boolbuild.and(access.accessTime.isNotNull());
			JPAQuery<AccessVo> query = super.getJpaQueryFactory().select(
					Projections.bean(AccessVo.class, access.accessTime.as("key"), 
							access.accessCount.sum().as("count")))
					.from(access);
			query.where(boolbuild).groupBy(access.accessTime, access.accessCount);
			return query.fetch();
		}
	}
}