package com.jeecms.content.dao.impl;

import java.util.List;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.content.dao.ext.ContentCheckDetailDaoExt;
import com.jeecms.content.domain.ContentCheckDetail;
import com.jeecms.content.domain.querydsl.QContentCheckDetail;
import com.querydsl.core.BooleanBuilder;

/**
 * 内容审核详情dao实现类
 * @author: tom
 * @date:   2020年1月8日 上午10:47:17
 */
public class ContentCheckDetailDaoImpl extends BaseDao<ContentCheckDetail> implements ContentCheckDetailDaoExt {

	@Override
	public List<ContentCheckDetail> findUnderReviews() {
		QContentCheckDetail contentCheckDetail = QContentCheckDetail.contentCheckDetail;
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(contentCheckDetail.hasDeleted.eq(false));
		/*
		 * 此处不判断内容是否加入回收站设置那是因为 能进入ContentCheckDetail说明其实审核中、审核失败，这种情况下只有可能是逻辑删除，
		 * 而此处在逻辑删除时会将ContentCheckDetail这条数据干掉，所以判断ContentCheckDetail 是否删除就可以判断这些东西
		 */
		builder.and(contentCheckDetail.status.eq(ContentCheckDetail.UNDER_REVIEW));
		return getJpaQueryFactory().select(contentCheckDetail).from(contentCheckDetail).where(builder)
				.fetch();
	}

	@Override
	public List<ContentCheckDetail> findByContentIds(List<Integer> contentIds) {
		QContentCheckDetail contentCheckDetail = QContentCheckDetail.contentCheckDetail;
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(contentCheckDetail.hasDeleted.eq(false));
		builder.and(contentCheckDetail.contentId.in(contentIds));
		return getJpaQueryFactory().select(contentCheckDetail).from(contentCheckDetail).where(builder)
				.fetch();
	}

}
