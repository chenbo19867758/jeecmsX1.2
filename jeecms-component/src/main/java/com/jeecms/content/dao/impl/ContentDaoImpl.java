/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.dao.impl;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.dao.ext.ContentDaoExt;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.dto.ContentSearchDto;
import com.jeecms.content.domain.querydsl.QContent;
import com.jeecms.content.domain.querydsl.QContentChannel;
import com.jeecms.content.domain.vo.ContentVo;
import com.jeecms.system.domain.querydsl.QContentType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.jeecms.content.constants.ContentConstant.STATUS_PIGEONHOLE;
import static com.jeecms.content.constants.ContentConstant.STATUS_TEMPORARY_STORAGE;

/**
 * 内容扩展查询dao实现
 *
 * @author: tom
 * @date: 2019年5月11日 下午1:49:25
 */
public class ContentDaoImpl extends BaseDao<Content> implements ContentDaoExt {

        @Override
        public Page<Content> getPage(ContentSearchDto dto, Pageable pageable) {
                QContent qContent = QContent.content;
                QContentChannel contentChannel = QContentChannel.contentChannel;
                JPAQuery<Tuple> query = getJpaQueryFactory().select(qContent, contentChannel.status,
                                contentChannel.createType, contentChannel.channelId, contentChannel.isRef)
                                .from(qContent);
                query = appendQuery(query, qContent, dto);
                if (dto.getOrderType() != null && dto.getOrderType() != 0) {
                        query.orderBy(orderType(qContent, dto.getOrderType()));
                } else {
                        // 归档倒序(使用修改时间)
                        query.orderBy(qContent.updateTime.desc());
                }
                query.offset(pageable.getOffset());
                query.limit(pageable.getPageSize());
                QueryResults<Tuple> result = query.fetchResults();
                List<Content> contents = new ArrayList<Content>(10);
                for (Tuple tuple : result.getResults()) {
                        Content vo = new Content();
                        vo = tuple.get(0, Content.class);
                        contents.add(vo);
                }
                Page<Content> page = new PageImpl<Content>(contents, pageable, result.getTotal());
                return page;
        }

        @Override
        public long getCount(ContentSearchDto dto) {
                QContent qContent = QContent.content;
                QContentChannel contentChannel = QContentChannel.contentChannel;
                JPAQuery<Tuple> query = getJpaQueryFactory().select(qContent, contentChannel.status,
                                contentChannel.createType, contentChannel.channelId, contentChannel.isRef)
                                .from(qContent);
                query = appendQuery(query, qContent, dto);
                return query.fetchCount();
        }

        @Override
        public long getSum(Date beginTime, Date endTime, Integer siteId, Integer status, Integer createType) {
                QContent qContent = QContent.content;
                BooleanBuilder builder = new BooleanBuilder();
                if (beginTime != null) {
                        builder.and(qContent.releaseTime.goe(beginTime));
                }
                if (endTime != null) {
                        builder.and(qContent.releaseTime.loe(endTime));
                }
                if (siteId != null) {
                        builder.and(qContent.siteId.eq(siteId));
                }
                if (status != null) {
                        builder.and(qContent.status.eq(status));
                }
                if (createType != null) {
                        builder.and(qContent.createType.eq(createType));
                }
                JPAQuery<Content> query = new JPAQuery<Content>(this.em);
                return query.from(qContent).where(builder).fetchCount();
        }

        /**
         * 处理排序
         * 
         * @param qContent
         *                内容对象
         * @param type
         *                类型
         * @return
         */
        @SuppressWarnings("rawtypes")
        private OrderSpecifier orderType(QContent qContent, Integer type) {
                switch (type) {
                case ContentConstant.ORDER_TYPE_CREATETIME_DESC:
                        return qContent.createTime.desc();
                case ContentConstant.ORDER_TYPE_CREATETIME_ASC:
                        return qContent.createTime.asc();

                case ContentConstant.ORDER_TYPE_VIEWS_DESC:
                        return qContent.views.desc();
                case ContentConstant.ORDER_TYPE_VIEWS_ASC:
                        return qContent.views.asc();
                case ContentConstant.ORDER_TYPE_VIEWS_MONTH_DESC:
                        return qContent.contentExt.viewsMonth.desc();
                case ContentConstant.ORDER_TYPE_VIEWS_MONTH_ASC:
                        return qContent.contentExt.viewsMonth.asc();
                case ContentConstant.ORDER_TYPE_VIEWS_WEEK_DESC:
                        return qContent.contentExt.viewsWeek.desc();
                case ContentConstant.ORDER_TYPE_VIEWS_WEEK_ASC:
                        return qContent.contentExt.viewsWeek.asc();
                case ContentConstant.ORDER_TYPE_VIEWS_DAY_DESC:
                        return qContent.contentExt.viewsDay.desc();
                case ContentConstant.ORDER_TYPE_VIEWS_DAY_ASC:
                        return qContent.contentExt.viewsDay.asc();

                case ContentConstant.ORDER_TYPE_COMMENTS_DESC:
                        return qContent.comments.desc();
                case ContentConstant.ORDER_TYPE_COMMENTS_ASC:
                        return qContent.comments.asc();
                case ContentConstant.ORDER_TYPE_COMMENTS_MONTH_DESC:
                        return qContent.contentExt.commentsMonth.desc();
                case ContentConstant.ORDER_TYPE_COMMENTS_MONTH_ASC:
                        return qContent.contentExt.commentsMonth.asc();
                case ContentConstant.ORDER_TYPE_COMMENTS_WEEK_DESC:
                        return qContent.contentExt.commentsWeek.desc();
                case ContentConstant.ORDER_TYPE_COMMENTS_WEEK_ASC:
                        return qContent.contentExt.commentsWeek.asc();
                case ContentConstant.ORDER_TYPE_COMMENTS_DAY_DESC:
                        return qContent.contentExt.commentsDay.desc();
                case ContentConstant.ORDER_TYPE_COMMENTS_DAY_ASC:
                        return qContent.contentExt.commentsDay.asc();
                case ContentConstant.ORDER_TYPE_UPS_DESC:
                        return qContent.ups.desc();
                case ContentConstant.ORDER_TYPE_UPS_ASC:
                        return qContent.ups.asc();
                case ContentConstant.ORDER_TYPE_UPS_MONTH_DESC:
                        return qContent.contentExt.upsMonth.desc();
                case ContentConstant.ORDER_TYPE_UPS_MONTH_ASC:
                        return qContent.contentExt.upsMonth.asc();
                case ContentConstant.ORDER_TYPE_UPS_WEEK_DESC:
                        return qContent.contentExt.upsWeek.desc();
                case ContentConstant.ORDER_TYPE_UPS_WEEK_ASC:
                        return qContent.contentExt.upsWeek.asc();
                case ContentConstant.ORDER_TYPE_UPS_DAY_DESC:
                        return qContent.contentExt.upsDay.desc();
                case ContentConstant.ORDER_TYPE_UPS_DAY_ASC:
                        return qContent.contentExt.upsDay.asc();
                case ContentConstant.ORDER_TYPE_RELEASE_TIME_DESC:
                        return qContent.releaseTime.desc();
                case ContentConstant.ORDER_TYPE_RELEASE_TIME_ASC:
                        return qContent.releaseTime.asc();
                case ContentConstant.ORDER_TYPE_UPDATETIME_DESC:
                		return qContent.updateTime.desc();
                default:
                        break;
                }
                return qContent.releaseTime.desc();
        }

        @Override
        public List<Content> getList(ContentSearchDto dto, Paginable paginable) {
                List<Content> contents = new ArrayList<Content>(10);
                QContent qContent = QContent.content;
                QContentChannel contentChannel = QContentChannel.contentChannel;
                JPAQuery<Tuple> query = getJpaQueryFactory().select(qContent, contentChannel.status,
                                contentChannel.createType, contentChannel.channelId, contentChannel.isRef)
                                .from(qContent);
                query = appendQuery(query, qContent, dto);
                if (dto.getOrderType() != null && dto.getOrderType() != 0) {
                        query.orderBy(orderType(qContent, dto.getOrderType()));
                } else {
                        // 默认排序值升序，比重降序,创建时间倒序
                        query.orderBy(qContent.sortNum.asc()).orderBy(qContent.sortWeight.desc())
                                        .orderBy(qContent.createTime.desc());
                }
                List<Tuple> tuples = query.fetch();
                for (Tuple tuple : tuples) {
                        Content vo = new Content();
                        vo = tuple.get(0, Content.class);
                        contents.add(vo);
                }
                return contents;
        }

        private JPAQuery<Tuple> appendQuery(JPAQuery<Tuple> query, QContent qContent, ContentSearchDto dto) {
                BooleanBuilder builder = new BooleanBuilder();
                QContentChannel contentChannel = QContentChannel.contentChannel;
                query.innerJoin(qContent.contentChannels, contentChannel);
                builder.and(contentChannel.recycle.eq(false));
                // 内容状态
                if (dto.getStatus() != null) {
                        if (dto.getStatus().size() == 1) {
                                builder.and(contentChannel.status.eq(dto.getStatus().get(0)));
                        } else {
                                builder.and(contentChannel.status.in(dto.getStatus()));
                        }
                } else {
                        // 默认过滤暂存状态和已归档
                        builder.and(qContent.status.ne(STATUS_TEMPORARY_STORAGE))
                                        .and(qContent.status.ne(STATUS_PIGEONHOLE));
                }
                // 如果内容id存在则排除内容id
                if (dto.getContentId() != null) {
                        builder.and(qContent.id.ne(dto.getContentId()));
                }
                // 内容类型
                if (dto.getContentType() != null) {
                        QContentType contentType = QContentType.contentType;
                        query.innerJoin(qContent.contentTypes, contentType);
                        builder.and(contentType.id.eq(dto.getContentType()));
                }
                // 是否我创建的
                if (dto.getMyself() != null && dto.getMyself()) {
                        builder.and(qContent.userId.eq(dto.getUserId()));
                }
                // 重新编辑
                if (dto.getUpdate() != null && dto.getUpdate()) {
                        builder.and(qContent.edit.eq(dto.getUpdate()));
                }
                // 起始创建时间
                if (dto.getCreateStartTime() != null && dto.getCreateEndTime() != null) {
                        builder.and(qContent.createTime.goe(dto.getCreateStartTime()).and(
                                        qContent.createTime.loe(MyDateUtils.getFinallyDate(dto.getCreateEndTime()))));
                }
                // 起始发布时间
                if (dto.getReleaseStartTime() != null && dto.getReleaseEndTime() != null) {
                        builder.and(qContent.releaseTime.goe(dto.getReleaseStartTime()).and(
                                        qContent.releaseTime.loe(MyDateUtils.getFinallyDate(dto.getReleaseEndTime()))));
                }
                // 归档时间（使用内容对象的updateTime）
                if (dto.getFileStartTime() != null && dto.getFileEndTime() != null) {
                        builder.and(qContent.updateTime.goe(dto.getFileStartTime()).and(
                                        qContent.updateTime.loe(MyDateUtils.getFinallyDate(dto.getFileEndTime()))));
                }
                // 创建方式
                if (dto.getCreateType() != null) {
                        builder.and(contentChannel.createType.eq(dto.getCreateType()));
                }
                // 内容模型
                if (dto.getModelId() != null) {
                        builder.and(qContent.modelId.eq(dto.getModelId()));
                }
                // 栏目ID
                if (dto.getChannelIds() != null && dto.getChannelIds().length > 0) {
                        builder.and(contentChannel.channelId.in(dto.getChannelIds()));
                }
                // 关键字类型
                if (dto.getKeyType() != null && StringUtils.isNotEmpty(dto.getKey())) {
                        switch (dto.getKeyType()) {
                        case ContentConstant.KEY_TYPE_TITLE:
                                // 标题
                                builder.and(qContent.title.like("%" + dto.getKey() + "%"));
                                break;
                        case ContentConstant.KEY_TYPE_AUTHOR:
                                // 作者名称
                                builder.and(qContent.contentExt.author.like("%" + dto.getKey() + "%"));
                                break;
                        case ContentConstant.KEY_TYPE_SOURCE:
                                // 来源
                                builder.and(qContent.contentExt.contentSource.sourceName
                                                .like("%" + dto.getKey() + "%"));
                                break;
                        case ContentConstant.KEY_TYPE_DESCRIBE:
                                // 简短标题
                                builder.and(qContent.shortTitle.like("%" + dto.getKey() + "%"));
                                break;
                        case ContentConstant.KEY_TYPE_USER:
                                // 创建人
                                builder.and(qContent.createUser.like("%" + dto.getKey() + "%"));
                                break;
                        default:
                                break;
                        }
                }
                // 内容密级
		if (dto.getContentSecretIds() != null && dto.getContentSecretIds().length != 0) {
			// 给密级做筛选
			if (dto.getSecret()) {
				builder.and(qContent.contentSecretId.in(dto.getContentSecretIds()));
			} else {
				builder.and(
						qContent.contentSecretId.in(dto.getContentSecretIds())
						.or(qContent.contentSecretId.isNull()));
			}
		} else {
                        builder.and(qContent.contentSecretId.isNull());
                }
                // 发文字号-机关代字
                if (dto.getIssueOrg() != null) {
                        builder.and(qContent.contentExt.issueOrg.eq(dto.getIssueOrg()));
                }
                // 发文字号-年份
                if (dto.getIssueYear() != null) {
                        builder.and(qContent.contentExt.issueYear.eq(dto.getIssueYear()));
                }
                // 发文顺序号
                if (StringUtils.isNotBlank(dto.getIssueNum())) {
                        builder.and(qContent.contentExt.issueNum.like("%" + dto.getIssueNum() + "%"));
                }
                // 站点ID
                if (dto.getSiteId() != null) {
                        builder.and(qContent.siteId.eq(dto.getSiteId()));
                }
                // 过滤删除
                builder.and(qContent.hasDeleted.eq(false));
                /** 回收站 */
                if (dto.getRecycle() != null) {
                        builder.and(qContent.recycle.eq(dto.getRecycle()));
                }
                return query.where(builder);
        }

        @Override
        public Integer findByTitle(String title, Integer channelId) {
                QContent content = QContent.content;
                BooleanBuilder builder = new BooleanBuilder();
                builder.and(content.hasDeleted.eq(false));
                builder.and(content.recycle.eq(false));
                if (title != null) {
                        builder.and(content.title.eq(title));
                }
                if (channelId != null) {
                        builder.and(content.channelId.eq(channelId));
                }
                return getJpaQueryFactory().select(content).from(content).where(builder).fetch().size();
        }

        @Override
        public Page<ContentVo> getPages(ContentSearchDto dto, Pageable pageable) {
                QContent qContent = QContent.content;
                QContentChannel contentChannel = QContentChannel.contentChannel;
                JPAQuery<Tuple> query = getJpaQueryFactory().select(qContent, contentChannel.status,
                                contentChannel.createType, contentChannel.channelId, contentChannel.isRef)
                                .from(qContent);
                query = appendQuery(query, qContent, dto);
                if (dto.getOrderType() != null && dto.getOrderType() != 0) {
                        query.orderBy(orderType(qContent, dto.getOrderType()));
                } else {
                        // 默认置顶最前，排序值升序，比重降序
                        query.orderBy(qContent.top.desc()).orderBy(qContent.sortNum.asc())
                                        .orderBy(qContent.sortWeight.desc()).orderBy(qContent.createTime.desc());
                }
                query.offset(pageable.getOffset());
                query.limit(pageable.getPageSize());
                QueryResults<Tuple> result = query.fetchResults();
                List<ContentVo> contents = new ArrayList<ContentVo>(10);
                for (Tuple tuple : result.getResults()) {
                        ContentVo vo = new ContentVo();
                        vo.setCmsContent(tuple.get(0, Content.class));
                        vo.setStatus(tuple.get(contentChannel.status));
                        vo.setCreateType(tuple.get(contentChannel.createType));
                        vo.setChannelId(tuple.get(contentChannel.channelId));
                        vo.setQuote(tuple.get(contentChannel.isRef));
                        contents.add(vo);
                }
                Page<ContentVo> page = new PageImpl<ContentVo>(contents, pageable, result.getTotal());
                return page;
        }

        private EntityManager em;

        @javax.persistence.PersistenceContext
        public void setEm(EntityManager em) {
                this.em = em;
        }

}
