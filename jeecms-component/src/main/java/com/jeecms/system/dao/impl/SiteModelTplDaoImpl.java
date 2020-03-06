package com.jeecms.system.dao.impl;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.dao.ext.SiteModelTplDaoExt;
import com.jeecms.system.domain.SiteModelTpl;
import com.jeecms.system.domain.querydsl.QSiteModelTpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.hibernate.jpa.QueryHints;

import java.util.List;

/**
 * 站点模型模板配置dao实现类
 * @author: chenming
 * @date:   2019年5月5日 下午1:44:30
 */
public class SiteModelTplDaoImpl extends BaseDao<SiteModelTpl> implements SiteModelTplDaoExt {

	@Override
	public List<SiteModelTpl> findBySiteId(Integer siteId) throws GlobalException {
		QSiteModelTpl siteModelTpl = QSiteModelTpl.siteModelTpl;
		BooleanExpression exp = siteModelTpl.hasDeleted.eq(false);
		if (siteId != null) {
			exp = exp.and(siteModelTpl.siteId.eq(siteId));
		}
		if (siteModelTpl.model != null) {
			exp = exp.and(siteModelTpl.model.tplType.eq(new Short("2")));
		}
		return getJpaQueryFactory().select(siteModelTpl)
				.setHint(QueryHints.HINT_CACHEABLE, true)
				.from(siteModelTpl).where(exp).fetch();
	}

	@Override
	public SiteModelTpl findBySiteIdAndModelId(Integer siteId, Integer modelId) throws GlobalException {
		QSiteModelTpl siteModelTpl = QSiteModelTpl.siteModelTpl;
		BooleanExpression exp = siteModelTpl.hasDeleted.eq(false);
		if (siteId != null) {
			exp = exp.and(siteModelTpl.siteId.eq(siteId));
		}
		if (modelId != null) {
			exp = exp.and(siteModelTpl.modelId.eq(modelId));
		}
		return getJpaQueryFactory().select(siteModelTpl)
				.setHint(QueryHints.HINT_CACHEABLE, true)
				.from(siteModelTpl).where(exp).fetchFirst();
	}
	
	
}
