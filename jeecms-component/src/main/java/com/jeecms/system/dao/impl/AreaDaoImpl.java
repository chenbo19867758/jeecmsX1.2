package com.jeecms.system.dao.impl;

import java.util.List;

import org.hibernate.jpa.QueryHints;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.dao.ext.AreaDaoExt;
import com.jeecms.system.domain.Area;
import com.jeecms.system.domain.querydsl.QArea;
import com.querydsl.core.types.Predicate;

/**
 * Area设置dao实现类
 * @author: chenming
 * @date:   2019年4月13日 下午2:48:41
 */
public class AreaDaoImpl extends BaseDao<Area> implements AreaDaoExt {

	@Override
	public List<Area> findByParams(Integer parentId, Boolean hasDeleted) throws GlobalException {
		QArea area = QArea.area;
		Predicate parentPre = null;
		if (parentId == null) {
			parentPre = area.parentId.isNull();
		} else {
			parentPre = area.parentId.eq(parentId);
		}
		List<Area> result = getJpaQueryFactory().select(area).from(area)
				.setHint(QueryHints.HINT_CACHEABLE, true).where(area.hasDeleted.eq(hasDeleted)
						.and(parentPre)).orderBy(area.sortNum.asc()).fetch();
		return result;

	}

}
