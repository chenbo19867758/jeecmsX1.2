package com.jeecms.statistics.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.statistics.dao.ext.StatisticsFlowDaoExt;
import com.jeecms.statistics.domain.StatisticsFlow;
import com.jeecms.statistics.domain.dto.StatisticsFlowDto;
import com.jeecms.statistics.domain.querydsl.QStatisticsFlow;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

public class StatisticsFlowDaoImpl extends BaseDao<StatisticsFlow> implements StatisticsFlowDaoExt {

	@Override
	public List<StatisticsFlow> getFlow(StatisticsFlowDto dto) {
		QStatisticsFlow flow = QStatisticsFlow.statisticsFlow;
		BooleanBuilder exp = new BooleanBuilder();
		exp.and(flow.hasDeleted.eq(false));
		exp.and(flow.siteId.eq(dto.getSiteId()));
		exp.and(flow.createTime.goe(dto.getMinTime()));
		exp.and(flow.createTime.loe(dto.getMaxTime()));
		if (dto.getSorceUrlType() != null) {
			exp.and(flow.sorceUrlType.isNotNull());
			exp.and(flow.sorceUrlType.eq(dto.getSorceUrlType()));
		}
		if (dto.getProvince() != null) {
			exp.and(flow.visitorProvince.isNotNull());
			exp.and(flow.visitorProvince.eq(dto.getProvince()));
			if (dto.getCity() != null) {
				exp.and(flow.visitorCity.isNotNull());
				exp.and(flow.visitorCity.eq(dto.getCity()));
			}
		}
		if (dto.getAccessSourceClient() != null) {
			exp.and(flow.visitorDeviceType.isNotNull());
			exp.and(flow.visitorDeviceType.eq(Integer.valueOf(dto.getAccessSourceClient())));
		}
		if (dto.getNewVisitor() != null) {
			exp.and(flow.isNewVisitor.isNotNull());
			exp.and(flow.isNewVisitor.eq(dto.getNewVisitor()));
		}
		if (StringUtils.isNotBlank(dto.getEngineName())) {
			exp.and(flow.engineName.isNotNull());
			exp.and(flow.engineName.eq(dto.getEngineName()));
		}
		return getJpaQueryFactory().selectFrom(flow).where(exp).fetch();
	}
	
	@Override
	public List<StatisticsFlow> getFlowList(Date start, Date end, Integer siteId, Integer sourceType, 
			Boolean visit, String province)
			throws GlobalException {
		QStatisticsFlow flow = QStatisticsFlow.statisticsFlow;
		BooleanBuilder boolbuild = new BooleanBuilder();
		if (start != null && end != null) {
			boolbuild.and(flow.statisticsDay.between(MyDateUtils.formatDate(start),
					MyDateUtils.formatDate(end)));
		}
		if (siteId != null) {
			boolbuild.and(flow.siteId.eq(siteId));
		}
		if (sourceType != null) {
			boolbuild.and(flow.sorceUrlType.eq(sourceType));
		}
		if (visit != null) {
			boolbuild.and(flow.isNewVisitor.eq(visit));
		}
		if (StringUtils.isNotBlank(province)) {
			boolbuild.and(flow.visitorProvince.like("%" + province));
		}
		JPAQuery<StatisticsFlow> query = super.getJpaQueryFactory().selectFrom(flow).where(boolbuild);
		return query.fetch();
	}

}
