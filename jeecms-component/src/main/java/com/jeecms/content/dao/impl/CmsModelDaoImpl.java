package com.jeecms.content.dao.impl;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.content.dao.ext.CmsModelDaoExt;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.querydsl.QCmsModel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 模型dao层实现
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年4月18日 上午10:06:42
 */
public class CmsModelDaoImpl extends BaseDao<CmsModel> implements CmsModelDaoExt {

	@Override
	public Page<CmsModel> getPage(Short tplType, Short isGlobal, Boolean isEnable, String modelName, 
			Integer siteId, Pageable pageable) throws GlobalException {
		QCmsModel qModel = QCmsModel.cmsModel;
		BooleanBuilder builder = new BooleanBuilder();
		// 模型类型
		builder.and(qModel.tplType.eq(tplType));
		// 模型类型分为三种情况查询
		if (isGlobal != null) {
			// 1.全站查询,只需过滤isGlobal
			if (CmsModel.WHOLE_SITE_MODEL.equals(isGlobal)) {
				builder.and(qModel.isGlobal.eq(isGlobal));
			} else {
				// 2.本站查询,先过滤isGlobal,再去比较siteId
				builder.and(qModel.isGlobal.eq(CmsModel.THIS_SITE_MODEL).and(qModel.siteId.eq(siteId)));
			}
		} else {
			// 3.全部查询 = 全站模型的数据 + (本站模型,过滤siteId之后的数据)
			builder.and(qModel.isGlobal.eq(CmsModel.WHOLE_SITE_MODEL)
				   .or(qModel.isGlobal.eq(CmsModel.THIS_SITE_MODEL).and(qModel.siteId.eq(siteId))));
		}

		// 模型状态
		if (isEnable != null) {
			builder.and(qModel.isEnable.eq(isEnable));
		}
		builder.and(qModel.hasDeleted.eq(false));
		// 模型名称
		if (modelName != null) {
			builder.and(qModel.modelName.like("%" + modelName + "%"));
		}
		OrderSpecifier<Integer> sortOrder = qModel.sortNum.desc();
		OrderSpecifier<Integer> sortWeightOrder = qModel.sortWeight.desc();
		JPAQuery<CmsModel> query = super.getJpaQueryFactory().selectFrom(qModel).orderBy(sortOrder , sortWeightOrder).where(builder);
		return QuerydslUtils.page(query, pageable, null);
	}

	@Override
	public List<CmsModel> getList(Short tplType, Boolean isEnable, Integer siteId) throws GlobalException {
		QCmsModel model = QCmsModel.cmsModel;
		BooleanBuilder exp = new BooleanBuilder();
		if (tplType != null) {
			exp.and(model.tplType.eq(tplType));
		}
		if (isEnable != null) {
			exp.and(model.isEnable.eq(isEnable));
		}
		if (siteId != null) {
			exp.and(model.isGlobal.eq(CmsModel.WHOLE_SITE_MODEL)
			   .or(model.isGlobal.eq(CmsModel.THIS_SITE_MODEL).and(model.siteId.eq(siteId))));
		} else {
		    exp.and(model.isGlobal.eq(CmsModel.WHOLE_SITE_MODEL));
        }
		exp.and(model.hasDeleted.eq(false));
		OrderSpecifier<Integer> sortOrder = model.sortNum.desc();
		OrderSpecifier<Integer> sortWeightOrder = model.sortWeight.desc();
		return getJpaQueryFactory().select(model).from(model)
				.setHint(QueryHints.HINT_CACHEABLE, true)
				.orderBy(sortOrder,sortWeightOrder).where(exp).fetch();
	}

	@Override
	public CmsModel getMaxModelType(Short tplType) {
		QCmsModel model = QCmsModel.cmsModel;
		BooleanBuilder exp = new BooleanBuilder();
		exp.and(model.tplType.eq(tplType));
		OrderSpecifier<Integer> sortOrder = model.sortNum.desc();
		OrderSpecifier<Integer> sortWeightOrder = model.sortWeight.desc();
		JPAQuery<CmsModel> query = super.getJpaQueryFactory().selectFrom(model)
				.setHint(QueryHints.HINT_CACHEABLE, true)
				.orderBy(sortOrder , sortWeightOrder).where(exp);
		return query.fetchFirst();
	}
}
