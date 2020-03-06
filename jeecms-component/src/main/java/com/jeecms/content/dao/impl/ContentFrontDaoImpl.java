/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.dao.impl;

import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.dao.ext.ContentFrontDaoExt;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.querydsl.QContent;
import com.jeecms.content.domain.querydsl.QContentChannel;
import com.jeecms.content.domain.querydsl.QContentRelation;
import com.jeecms.system.domain.querydsl.QContentTag;
import com.jeecms.system.domain.querydsl.QContentType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.jeecms.content.constants.ContentConstant.*;

/**
 * 前台内容Dao扩展实现
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/7/19 9:29
 */

public class ContentFrontDaoImpl implements ContentFrontDaoExt {
	@Override
	public Page<Content> getPage(Integer[] channelIds, Integer[] tagIds,
								 String[] channelPaths, Integer siteId,
								 Integer[] typeIds, String title, Date date,
								 Integer releaseTarget, Boolean isTop,
								 Date timeBegin, Date timeEnd,
								 Integer[] excludeId, Integer[] modelId,
								 Integer orderBy, Pageable pageable) {
		QContent qContent = QContent.content;
		JPAQuery<Content> query = new JPAQuery<Content>(this.em);
		query.from(qContent).distinct();
		BooleanBuilder builder = new BooleanBuilder();
		builder = appendContentTag(tagIds, qContent, query, builder, excludeId);
		query = appendQuery(query, qContent, builder, channelIds, channelPaths, typeIds,
				siteId, modelId, title, date, isTop, releaseTarget, timeBegin, timeEnd);
		query = orderType(query, qContent, orderBy);
		query.setHint("org.hibernate.cacheable", true);
		return QuerydslUtils.page(query, pageable, qContent);
	}

	@Override
	public Page<Content> getPage(Integer siteId, Integer userId, Integer status, String title,
								 Date startDate, Date endDate, Pageable pageable) {
		JPAQuery<Content> query = new JPAQuery<Content>(this.em);
		QContent qContent = QContent.content;
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(qContent.userId.eq(userId));
		builder.and(qContent.siteId.eq(siteId));
		builder.and(qContent.recycle.eq(false));
		builder.and(qContent.hasDeleted.eq(false));
		// 显示创建方式为投稿
		builder.and(qContent.createType.eq(ContentConstant.CONTENT_CREATE_TYPE_CONTRIBUTE));
		// 当状态为
		if (status != null) {
			switch (status) {
				case ContentConstant.CONTRIBUTE_PENDING_REVIEW:
					builder.and(qContent.status.ne(ContentConstant.STATUS_PUBLISH));
					builder.and(qContent.status.ne(ContentConstant.STATUS_TEMPORARY_STORAGE));
					break;
				case ContentConstant.CONTRIBUTE_TEMPORARY_STORAGE:
					builder.and(qContent.status.eq(ContentConstant.STATUS_TEMPORARY_STORAGE));
					break;
				case ContentConstant.CONTRIBUTE_RELEASE:
					builder.and(qContent.status.eq(ContentConstant.STATUS_PUBLISH));
					break;
				default:
					break;
			}
		}
		if (StringUtils.isNotBlank(title)) {
			builder.and(qContent.title.like("%" + title + "%"));
		}
		if (startDate != null) {
			builder.and(qContent.createTime.goe(startDate));
		}
		if (endDate != null) {
			builder.and(qContent.createTime.loe(endDate));
		}
		query.select(qContent).from(qContent).where(builder).orderBy(qContent.createTime.desc());
		query.setHint("org.hibernate.cacheable", true);
		return QuerydslUtils.page(query, pageable, qContent);
	}

	@Override
	public List<Content> getList(Integer[] channelIds, Integer[] tagIds,
								 String[] channelPaths, Integer siteId,
								 Integer[] typeIds, String title, Date date,
								 Integer releaseTarget, Boolean isTop, Date timeBegin,
								 Date timeEnd, Integer[] excludeId, Integer[] modelId,
								 Integer orderBy, Integer count) {
		JPAQuery<Content> query = new JPAQuery<Content>(this.em);
		QContent qContent = QContent.content;
		query.from(qContent).distinct();
		BooleanBuilder builder = new BooleanBuilder();
		builder = appendContentTag(tagIds, qContent, query, builder, excludeId);
		query = appendQuery(query, qContent, builder, channelIds, channelPaths, typeIds,
				siteId, modelId, title, date, isTop, releaseTarget, timeBegin, timeEnd);
		query = orderType(query, qContent, orderBy);
		if (count != null) {
			query.limit(count);
		}
		query.setHint("org.hibernate.cacheable", true);
		return query.fetch();
	}

	@Override
	public List<Content> getList(Integer[] relationIds, Integer orderBy, Integer count) {
		JPAQuery<Content> query = new JPAQuery<Content>(this.em);
		QContent qContent = QContent.content;
		query.from(qContent).distinct();
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(qContent.hasDeleted.eq(false));
		builder.and(qContent.recycle.eq(false));
		builder.and(qContent.status.eq(ContentConstant.STATUS_PUBLISH));
		if (relationIds != null) {
			QContentRelation qContentRelation = QContentRelation.contentRelation;
			query.innerJoin(qContent.contentRelations, qContentRelation);
			builder.and(qContentRelation.id.in(Arrays.asList(relationIds)));
		}
		query.where(builder);
		query = orderType(query, qContent, orderBy);
		if (count != null) {
			query.limit(count);
		}
		query.setHint("org.hibernate.cacheable", true);
		return query.fetch();
	}

	@Override
	public List<Content> findByIds(List<Integer> ids, Integer orderBy) {
		JPAQuery<Content> query = new JPAQuery<Content>(this.em);
		QContent qContent = QContent.content;
		query.from(qContent).distinct();
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(qContent.hasDeleted.eq(false));
		builder.and(qContent.status.eq(ContentConstant.STATUS_PUBLISH));
		builder.and(qContent.id.in(ids));
		query.where(builder);
		query = orderType(query, qContent, orderBy);
		query.setHint("org.hibernate.cacheable", true);
		return query.fetch();
	}

	@Override
	public Content getSide(Integer id, Integer siteId, Integer channelId, Boolean next, boolean cacheable) {
		JPAQuery<Content> query = new JPAQuery<Content>(this.em);
		QContent qContent = QContent.content;
		query.from(qContent).distinct();
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(qContent.hasDeleted.eq(false));
		builder.and(qContent.status.eq(ContentConstant.STATUS_PUBLISH));
		query.setHint(QueryHints.HINT_CACHEABLE, cacheable);
		if (channelId != null) {
			builder.and(qContent.channelId.eq(channelId));
		} else if (siteId != null) {
			builder.and(qContent.siteId.eq(siteId));
		}
		if (next != null) {
			if (next) {
				builder.and(qContent.id.gt(id));
				query.where(builder).orderBy(qContent.id.asc());
			} else {
				builder.and(qContent.id.lt(id));
				query.where(builder).orderBy(qContent.id.desc());
			}
		} else {
			builder.and(qContent.id.eq(id));
			query.where(builder);
		}

		return query.fetchFirst();
	}

	/**
	 * 关联tagIds词
	 *
	 * @param tagIds    tagIds集合
	 * @param qContent  QContent
	 * @param query     JPAQuery
	 * @param builder   BooleanBuilder
	 * @param excludeId 排除id集合
	 * @return BooleanBuilder
	 */
	private BooleanBuilder appendContentTag(Integer[] tagIds, QContent qContent,
											JPAQuery<Content> query,
											BooleanBuilder builder,
											Integer[] excludeId) {
		if (tagIds != null && tagIds.length > 0) {
			QContentTag qContentTag = QContentTag.contentTag;
			query.innerJoin(qContent.contentTags, qContentTag);
			builder.and(qContentTag.id.in(Arrays.asList(tagIds)));
			if (excludeId != null) {
				builder.and(qContent.id.notIn(Arrays.asList(excludeId)));
			}
		}
		return builder;
	}

	private JPAQuery<Content> appendQuery(JPAQuery<Content> query, QContent qContent,
										  BooleanBuilder builder,
										  Integer[] channelIds,
										  String[] channelPaths,
										  Integer[] typeIds, Integer siteId,
										  Integer[] modelId, String title,
										  Date date, Boolean isTop,
										  Integer releaseTarget,
										  Date timeBegin, Date timeEnd) {
		QContentChannel qContentChannel = QContentChannel.contentChannel;
		query.innerJoin(qContent.contentChannels, qContentChannel);
		if (channelIds != null && channelIds.length > 0) {
			builder.and(qContent.channelId.in(Arrays.asList(channelIds)));
			builder.or(qContentChannel.channelId.in(Arrays.asList(channelIds)));
		}
		if (channelPaths != null && channelPaths.length > 0) {
			List<String> paths = Arrays.asList(channelPaths);
			builder.and(qContent.channel.path.in(paths));
		}
		if (siteId != null) {
			builder.and(qContent.siteId.eq(siteId));
		}
		if (title != null) {
			builder.and(qContent.title.like("%" + title + "%"));
		}
		if (isTop != null) {
			builder.and(isTop ? qContent.top.isTrue() : qContent.top.isFalse());
		}
		if (CONTENT_RELEASE_TERRACE_WAP_NUMBER.equals(releaseTarget)) {
			builder.and(qContent.releaseWap.isTrue());
		} else if (CONTENT_RELEASE_TERRACE_PC_NUMBER.equals(releaseTarget)) {
			builder.and(qContent.releasePc.isTrue());
		} else if (CONTENT_RELEASE_TERRACE_APP_NUMBER.equals(releaseTarget)) {
			builder.and(qContent.releaseApp.isTrue());
		} else if (CONTENT_RELEASE_TERRACE_MINIPROGRAM_NUMBER.equals(releaseTarget)) {
			builder.and(qContent.releaseMiniprogram.isTrue());
		}
		if (date != null) {
			builder.and(qContent.releaseTime.goe(date));
		}
		if (typeIds != null && typeIds.length > 0) {
			QContentType qContentType = QContentType.contentType;
			query.innerJoin(qContent.contentTypes, qContentType);
			builder.and(qContentType.id.in(Arrays.asList(typeIds)));
		}
		if (modelId != null && modelId.length > 0) {
			builder.and(qContent.modelId.in(Arrays.asList(modelId)));
		}
		if (timeBegin != null) {
			builder.and(qContent.releaseTime.goe(timeBegin));
		}
		if (timeEnd != null) {
			builder.and(qContent.releaseTime.loe(timeEnd));
		}
		builder.and(qContent.recycle.eq(false));
		builder.and(qContent.status.eq(ContentConstant.STATUS_PUBLISH));
		builder.and(qContent.hasDeleted.eq(false));
		return query.where(builder);
	}

	/**
	 * 处理排序
	 *
	 * @param qContent 内容对象
	 * @param type     类型
	 * @return
	 */
	private JPAQuery<Content> orderType(JPAQuery<Content> query, QContent qContent, int type) {
		switch (type) {
			case ContentConstant.ORDER_TYPE_CREATETIME_DESC:
				return query.orderBy(qContent.createTime.desc());
			case ContentConstant.ORDER_TYPE_CREATETIME_ASC:
				return query.orderBy(qContent.createTime.asc());
			case ContentConstant.ORDER_TYPE_VIEWS_DESC:
				return query.orderBy(qContent.views.desc());
			case ContentConstant.ORDER_TYPE_VIEWS_ASC:
				return query.orderBy(qContent.views.asc());
			case ContentConstant.ORDER_TYPE_VIEWS_MONTH_DESC:
				return query.orderBy(qContent.contentExt.viewsMonth.desc());
			case ContentConstant.ORDER_TYPE_VIEWS_MONTH_ASC:
				return query.orderBy(qContent.contentExt.viewsMonth.asc());
			case ContentConstant.ORDER_TYPE_VIEWS_WEEK_DESC:
				return query.orderBy(qContent.contentExt.viewsWeek.desc());
			case ContentConstant.ORDER_TYPE_VIEWS_WEEK_ASC:
				return query.orderBy(qContent.contentExt.viewsWeek.asc());
			case ContentConstant.ORDER_TYPE_VIEWS_DAY_DESC:
				return query.orderBy(qContent.contentExt.viewsDay.desc());
			case ContentConstant.ORDER_TYPE_VIEWS_DAY_ASC:
				return query.orderBy(qContent.contentExt.viewsDay.asc());
			case ContentConstant.ORDER_TYPE_COMMENTS_DESC:
				return query.orderBy(qContent.comments.desc());
			case ContentConstant.ORDER_TYPE_COMMENTS_ASC:
				return query.orderBy(qContent.comments.asc());
			case ContentConstant.ORDER_TYPE_COMMENTS_MONTH_DESC:
				return query.orderBy(qContent.contentExt.commentsMonth.desc());
			case ContentConstant.ORDER_TYPE_COMMENTS_MONTH_ASC:
				return query.orderBy(qContent.contentExt.commentsMonth.asc());
			case ContentConstant.ORDER_TYPE_COMMENTS_WEEK_DESC:
				return query.orderBy(qContent.contentExt.commentsWeek.desc());
			case ContentConstant.ORDER_TYPE_COMMENTS_WEEK_ASC:
				return query.orderBy(qContent.contentExt.commentsWeek.asc());
			case ContentConstant.ORDER_TYPE_COMMENTS_DAY_DESC:
				return query.orderBy(qContent.contentExt.commentsDay.desc());
			case ContentConstant.ORDER_TYPE_COMMENTS_DAY_ASC:
				return query.orderBy(qContent.contentExt.commentsDay.asc());
			case ContentConstant.ORDER_TYPE_UPS_DESC:
				return query.orderBy(qContent.ups.desc());
			case ContentConstant.ORDER_TYPE_UPS_ASC:
				return query.orderBy(qContent.ups.asc());
			case ContentConstant.ORDER_TYPE_UPS_MONTH_DESC:
				return query.orderBy(qContent.contentExt.upsMonth.desc());
			case ContentConstant.ORDER_TYPE_UPS_MONTH_ASC:
				return query.orderBy(qContent.contentExt.upsMonth.asc());
			case ContentConstant.ORDER_TYPE_UPS_WEEK_DESC:
				return query.orderBy(qContent.contentExt.upsWeek.desc());
			case ContentConstant.ORDER_TYPE_UPS_WEEK_ASC:
				return query.orderBy(qContent.contentExt.upsWeek.asc());
			case ContentConstant.ORDER_TYPE_UPS_DAY_DESC:
				return query.orderBy(qContent.contentExt.upsDay.desc());
			case ContentConstant.ORDER_TYPE_UPS_DAY_ASC:
				return query.orderBy(qContent.contentExt.upsDay.asc());
			case ContentConstant.ORDER_TYPE_RELEASE_TIME_DESC:
				return query.orderBy(qContent.releaseTime.desc());
			case ContentConstant.ORDER_TYPE_RELEASE_TIME_ASC:
				return query.orderBy(qContent.releaseTime.asc());
			case ContentConstant.ORDER_TYPE_ID_DESC:
				return query.orderBy(qContent.id.desc());
			case ContentConstant.ORDER_TYPE_ID_ASC:
				return query.orderBy(qContent.id.asc());
			case ContentConstant.ORDER_TYPE_DAY_DOWNLOAD_DESC:
				return query.orderBy(qContent.contentExt.downloadsDay.desc());
			case ContentConstant.ORDER_TYPE_WEEK_DOWNLOAD_DESC:
				return query.orderBy(qContent.contentExt.downloadsWeek.desc());
			case ContentConstant.ORDER_TYPE_MONTH_DOWNLOAD_DESC:
				return query.orderBy(qContent.contentExt.downloadsMonth.desc());
			case ContentConstant.ORDER_TYPE_DOWNLOAD_DESC:
				return query.orderBy(qContent.downloads.desc());
			default:
				return query.orderBy(qContent.top.desc())
						.orderBy(qContent.sortNum.asc())
						.orderBy(qContent.sortWeight.desc())
						.orderBy(qContent.createTime.desc());
		}
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

}
