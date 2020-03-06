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
import com.jeecms.common.page.Paginable;
import com.jeecms.wechat.dao.ext.WechatFansSendLogDaoExt;
import com.jeecms.wechat.domain.WechatFansSendLog;
import com.jeecms.wechat.domain.querydsl.QWechatFansSendLog;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;


/**
 * 粉丝推送扩展实现类
 * @author: ljw
 * @date: 2018年8月21日 上午11:05:40
 */
public class WechatFansSendLogDaoImpl extends BaseDao<WechatFansSendLog> implements WechatFansSendLogDaoExt {

	@Override
	public Page<WechatFansSendLog> getLogPage(List<String> appids, Integer type, Date startTime, Date endTime,
			String title,Boolean black, String openId, Pageable pageable) throws GlobalException {
		QWechatFansSendLog fansSendLog = QWechatFansSendLog.wechatFansSendLog;
		BooleanBuilder builder = new BooleanBuilder();
		if (!appids.isEmpty()) {
			builder = builder.and(fansSendLog.appId.in(appids));
		}
		if (StringUtils.isNotBlank(openId)) {
			builder = builder.and(fansSendLog.openId.eq(openId));
		}
		if (type != null) {
			builder = builder.and(fansSendLog.sendType.eq(type));
		}
		if (startTime != null && endTime != null) {
			builder = builder.and(fansSendLog.createTime.between(startTime, endTime));
		}
		if (StringUtils.isNotBlank(title)) {
			builder = builder.and(fansSendLog.mediaJson.like("%" + title + "%"));
		}
		if (black != null) {
			if (black) {
				builder = builder.and(fansSendLog.fans.isBlackList.eq(false));
			} 
		}
		JPAQuery<WechatFansSendLog> query = super.getJpaQueryFactory().selectFrom(fansSendLog);
		query.where(builder);
		return QuerydslUtils.page(query, pageable, fansSendLog);
	}

	@Override
	public List<WechatFansSendLog> getList(List<String> appids, Integer type, Paginable paginable)
			throws GlobalException {
		QWechatFansSendLog fansSendLog = QWechatFansSendLog.wechatFansSendLog;
		BooleanBuilder builder = new BooleanBuilder();
		if (!appids.isEmpty()) {
			builder = builder.and(fansSendLog.appId.in(appids));
		}
		if (type != null) {
			builder = builder.and(fansSendLog.sendType.eq(type));
		}
		JPAQuery<WechatFansSendLog> query = super.getJpaQueryFactory().selectFrom(fansSendLog);
		query.where(builder);
		//默认ID排序
		query.orderBy(fansSendLog.id.desc());
		return QuerydslUtils.list(query, paginable, fansSendLog);
	}

}
