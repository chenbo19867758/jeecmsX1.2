/**
 * * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 
 */

package com.jeecms.auth.dao.impl;

import com.jeecms.auth.dao.ext.CoreUserMessageDaoExt;
import com.jeecms.auth.domain.querydsl.QCoreUserMessage;
import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.system.domain.SysMessage;
import com.jeecms.system.domain.querydsl.QSysMessage;
import com.jeecms.system.domain.vo.MessageVo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * 用户接收信息状态DaoImpl
 * @author: ljw
 * @date: 2019年1月23日 上午11:33:15
 */
public class CoreUserMessageDaoImpl extends BaseDao<SysMessage> implements CoreUserMessageDaoExt {

	@Override
	public Page<MessageVo> getMessagePage(Pageable pageable, Integer userId, Boolean status, Date startTime,
			Date endTime, String title, String content, String sendUserName) throws GlobalException {
		QSysMessage qSysMessage = QSysMessage.sysMessage;
		QCoreUserMessage message = QCoreUserMessage.coreUserMessage;
		BooleanBuilder boolbuild = new BooleanBuilder();
		if (status != null) {
			// 已读
			if (status) {
				boolbuild.and(message.status.eq(SysMessage.MESSAGE_STATUS_NORMAL));
			} else {
				boolbuild.and(message.status.isNull());
			}
		} else {
			// 排除已经删除的管理员消息
			boolbuild.and(message.status.isNull().or(message.status.eq(SysMessage.MESSAGE_STATUS_NORMAL)));
		}
		// 判断发件时间
		if (startTime != null && endTime != null) {
			boolbuild.and(qSysMessage.createTime.goe(startTime)
					.and(qSysMessage.createTime.loe(MyDateUtils.getFinallyDate(endTime))));
		}
		// 标题
		if (StringUtils.isNotEmpty(title)) {
			boolbuild.and(qSysMessage.title.like("%" + title + "%"));
		}
		// 发件人
		if (StringUtils.isNotEmpty(sendUserName)) {
			boolbuild.and(qSysMessage.sendUserName.like("%" + sendUserName + "%"));
		}
		// 内容
		if (StringUtils.isNotEmpty(content)) {
			boolbuild.and(qSysMessage.content.like("%" + content + "%"));
		}
		boolbuild.and(
				// 接收对象为全部
				qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_ALL)
						// 全部管理员
						.or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_ALL_ADMIN))
						// 指定管理员
						.or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_APPOINT_ADMIN)
								.and(qSysMessage.userId.eq(userId))));
		// 排除发件箱已经删除的信息
		boolbuild.and(qSysMessage.status.eq(SysMessage.MESSAGE_STATUS_NORMAL));
		JPAQuery<MessageVo> query = super.getJpaQueryFactory()
				.select(Projections.bean(MessageVo.class, 
						qSysMessage.id.as("messageId"),
						message.id.as("coreUserMessageId"), 
						qSysMessage.title.as("title"),
						qSysMessage.content.as("content"), 
						qSysMessage.createTime.as("createTime"),
						message.status.as("status"),
						qSysMessage.sendUserName.as("sendUserName")
						))
				.from(qSysMessage).leftJoin(message).on(qSysMessage.id.eq(message.messageId))
				.where(boolbuild)
				.orderBy(qSysMessage.createTime.desc());
		query.setHint(QueryHints.HINT_CACHEABLE, true);  //增加使用查询缓存
		return QuerydslUtils.page(query, pageable, null);
	}

	@Override
	public Long unreadMessage(Integer userId) throws GlobalException {
		if (userId == null) {
			return 0L;
		}
		QSysMessage qSysMessage = QSysMessage.sysMessage;
		QCoreUserMessage message = QCoreUserMessage.coreUserMessage;
		BooleanBuilder boolbuild = new BooleanBuilder();
		// 排除发件箱已经删除的信息
		boolbuild.and(qSysMessage.status.eq(SysMessage.MESSAGE_STATUS_NORMAL));
		// 选择未读的
		boolbuild.and(message.status.isNull()).and(
				// 接收对象为全部
				qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_ALL)
						// 全部管理员
						.or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_ALL_ADMIN))
						// 指定管理员
						.or(qSysMessage.recTargetType.eq(SysMessage.TARGETTYPE_APPOINT_ADMIN)
								.and(qSysMessage.userId.eq(userId))));
		JPAQuery<Long> query = super.getJpaQueryFactory().select(message.status.count()).from(qSysMessage)
				.leftJoin(message).on(qSysMessage.id.eq(message.messageId)).where(boolbuild);
		return query.fetchCount();
	}
}
