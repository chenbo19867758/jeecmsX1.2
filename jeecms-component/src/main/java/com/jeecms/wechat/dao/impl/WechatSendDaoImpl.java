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
import com.jeecms.wechat.dao.ext.WechatSendDaoExt;
import com.jeecms.wechat.domain.WechatFansSendLog;
import com.jeecms.wechat.domain.WechatSend;
import com.jeecms.wechat.domain.querydsl.QWechatSend;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;


/**
 * 粉丝推送扩展实现类
 * @author: ljw
 * @date: 2018年8月21日 上午11:05:40
 */
public class WechatSendDaoImpl extends BaseDao<WechatFansSendLog> implements WechatSendDaoExt {

	@Override
	public Page<WechatSend> getSendPage(List<String> appids, Date startTime, Date endTime,
			Integer status, String title,Pageable pageable) throws GlobalException {
		QWechatSend send = QWechatSend.wechatSend;
		BooleanBuilder builder = new BooleanBuilder();
		if (!appids.isEmpty()) {
			builder = builder.and(send.appId.in(appids));
		}
		if (status != null) {
			builder = builder.and(send.status.eq(status));
		}
		if (startTime != null && endTime != null) {
			builder = builder.and(send.createTime.between(startTime, endTime));
		}
		if (StringUtils.isNotBlank(title)) {
			builder = builder.and(send.materialJson.like("%" + title + "%"));
		}
		JPAQuery<WechatSend> query = super.getJpaQueryFactory().selectFrom(send);
		query.where(builder);
		return QuerydslUtils.page(query, pageable, send);
	}

}
