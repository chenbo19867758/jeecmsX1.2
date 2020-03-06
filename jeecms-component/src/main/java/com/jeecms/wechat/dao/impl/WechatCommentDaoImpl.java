/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */

package com.jeecms.wechat.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.wechat.constants.WechatConstants;
import com.jeecms.wechat.dao.ext.WechatCommentDaoExt;
import com.jeecms.wechat.domain.WechatComment;
import com.jeecms.wechat.domain.querydsl.QWechatComment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;


/**
 * 微信留言扩展实现类
 * @author: ljw
 * @date: 2018年8月21日 上午11:05:40
 */
public class WechatCommentDaoImpl extends BaseDao<WechatComment> implements WechatCommentDaoExt {

	@Override
	public Page<WechatComment> getPages(List<String> appids, Boolean commentType, 
			Date startTime, Date endTime, 
			Integer orderType, String comment, String msgDataId, Integer msgDataIndex, Pageable pageable) 
					throws GlobalException {
		QWechatComment qcomment = QWechatComment.wechatComment;
		BooleanBuilder builder = new BooleanBuilder();
		JPAQuery<WechatComment> query = getJpaQueryFactory().selectFrom(qcomment);
		if (!appids.isEmpty()) {
			builder.and(qcomment.appId.in(appids));
		}
		if (commentType != null) {
			builder.and(qcomment.commentType.eq(commentType));
		}
		if (startTime != null && endTime != null) {
			builder.and(qcomment.commentTime.between(
					MyDateUtils.getStartDate(startTime), 
					MyDateUtils.getStartDate(endTime)));
		}
		if (StringUtils.isNotBlank(comment)) {
			builder.and(qcomment.content.like("%" + comment + "%"));
		}
		if (StringUtils.isNotBlank(msgDataId)) {
			builder.and(qcomment.msgDataId.eq(msgDataId));
		}
		if (msgDataIndex != null) {
			builder.and(qcomment.msgDataIndex.eq(msgDataIndex));
		}
		builder.and(qcomment.hasDeleted.eq(false));
		if (orderType != null) {
			query.orderBy(orderType(qcomment, orderType));
		} else {
			// 默认排序值精选留言倒序，创建时间倒序
			query.orderBy(qcomment.commentType.desc()).orderBy(qcomment.userCommentId.desc());
		}
		query.where(builder);
		return QuerydslUtils.page(query, pageable, qcomment);
	}

	/**
	 * 处理排序
	 * 
	 * @Title: orderType
	 * @param qContent 内容对象
	 * @param type     类型
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private OrderSpecifier orderType(QWechatComment qcomment, Integer type) {
		switch (type) {
			case WechatConstants.ORDER_TYPE_CREATETIME_DES:
				return qcomment.userCommentId.desc();
			case WechatConstants.ORDER_TYPE_CREATETIME_ASC:
				return qcomment.userCommentId.asc();
			default:
				break;
		}
		return qcomment.createTime.desc();
	}

	@Override
	public Long getUserCommentId(String msgDataId, Integer index) {
		Long result = 0L;
		QWechatComment qcomment = QWechatComment.wechatComment;
		BooleanBuilder builder = new BooleanBuilder();
		if (StringUtils.isNotBlank(msgDataId) && index != null) {
			builder.and(qcomment.msgDataId.eq(msgDataId)
					.and(qcomment.msgDataIndex.eq(index)));
		}
		
		
		JPAQuery<String> query = getJpaQueryFactory()
				.select(qcomment.userCommentId.max()).from(qcomment);
		if (StringUtils.isNotBlank(query.fetchFirst())) {
			result = Long.valueOf(query.fetchFirst());
		}
		return result;
	}

}
