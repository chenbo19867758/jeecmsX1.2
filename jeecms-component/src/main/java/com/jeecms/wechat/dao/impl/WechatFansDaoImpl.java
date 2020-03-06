/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */

package com.jeecms.wechat.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.wechat.constants.WechatConstants;
import com.jeecms.wechat.dao.ext.WechatFansDaoExt;
import com.jeecms.wechat.domain.WechatFans;
import com.jeecms.wechat.domain.querydsl.QWechatFans;
import com.jeecms.wechat.domain.vo.WechatFansVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * 粉丝扩展实现类
 * 
 * @author: ljw
 * @date: 2018年8月21日 上午11:05:40
 */
public class WechatFansDaoImpl extends BaseDao<WechatFans> implements WechatFansDaoExt {

	@Override
	public Page<WechatFans> selectFans(List<String> appids, String nickname, String tagid, Boolean black,
			Pageable pageable) {
		QWechatFans qWechatFans = QWechatFans.wechatFans;
		BooleanBuilder builder = new BooleanBuilder();
		if (!appids.isEmpty()) {
			builder = builder.and(qWechatFans.appId.in(appids));
		}
		// 如果昵称不为空
		if (StringUtils.isNotBlank(nickname)) {
			builder = builder.and(qWechatFans.nickname.like("%" + nickname + "%"));
		}
		// 如果标签ID不为空
		if (StringUtils.isNotBlank(tagid)) {
			builder.and(qWechatFans.tagidList.like('%' + "," + tagid + "," + '%')
					.or(qWechatFans.tagidList.like(tagid + "," + '%')));
		}
		// 是否黑名单
		if (black != null) {
			builder.and(qWechatFans.isBlackList.eq(black));
		}
		JPAQuery<WechatFans> query = super.getJpaQueryFactory().selectFrom(qWechatFans);
		query.where(builder);
		return QuerydslUtils.page(query, pageable, qWechatFans);
	}

	@Override
	public List<WechatFansVO> fansVOs(List<String> appids, Integer type, String province) {
		QWechatFans qWechatFans = QWechatFans.wechatFans;
		BooleanBuilder builder = new BooleanBuilder();
		if (!appids.isEmpty()) {
			builder = builder.and(qWechatFans.appId.in(appids));
		}
		JPAQuery<WechatFansVO> query = null;
		// 语言
		if (type == WechatConstants.LANGUAGE) {
			query = super.getJpaQueryFactory().select(Projections.bean(WechatFansVO.class,
					qWechatFans.language.as("name"), qWechatFans.language.count().as("number")))
					.from(qWechatFans);
			query.where(builder).groupBy(qWechatFans.language);
		} else if (type == WechatConstants.SEX) {
			// 性别
			query = super.getJpaQueryFactory().select(
					Projections.bean(WechatFansVO.class, qWechatFans.sex.as("sex"),
					qWechatFans.sex.count().as("number")))
					.from(qWechatFans);
			query.where(builder).groupBy(qWechatFans.sex);;

		} else if (type == WechatConstants.PROVINCE) {
			builder.and(qWechatFans.country.eq("中国"));
			// 省份
			query = super.getJpaQueryFactory().select(Projections.bean(WechatFansVO.class,
					qWechatFans.province.as("name"), qWechatFans.province.count().as("number")))
					.from(qWechatFans);
			query.where(builder).groupBy(qWechatFans.province);
		} else if (type == WechatConstants.CITY) {
			builder.and(qWechatFans.country.eq("中国"));
			if (StringUtils.isNotBlank(province)) {
				builder.and(qWechatFans.province.like("%" + province));
			}
			// 城市
			query = super.getJpaQueryFactory().select(
					Projections.bean(WechatFansVO.class, qWechatFans.city.as("name"),
					qWechatFans.city.count().as("number")))
					.from(qWechatFans);
			query.where(builder).groupBy(qWechatFans.city);
		}
		return query.fetch();
	}
}
