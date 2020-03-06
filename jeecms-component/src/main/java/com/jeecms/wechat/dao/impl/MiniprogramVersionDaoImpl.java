package com.jeecms.wechat.dao.impl;

import org.springframework.stereotype.Repository;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.wechat.dao.ext.MiniprogramVersionDaoExt;
import com.jeecms.wechat.domain.MiniprogramVersion;
import com.jeecms.wechat.domain.querydsl.QMiniprogramVersion;
import com.querydsl.core.BooleanBuilder;

/**
 * 
 * @Description: 小程序代码管理dao实现类
 * @author: chenming
 * @date:   2019年1月25日 下午7:09:13     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Repository
public class MiniprogramVersionDaoImpl extends BaseDao<MiniprogramVersion> implements MiniprogramVersionDaoExt{

	@Override
	public MiniprogramVersion getSubmitFail(String appId, Integer versionType, Integer status) throws GlobalException {
		QMiniprogramVersion miniprogramVersion = QMiniprogramVersion.miniprogramVersion;
		BooleanBuilder builder = new BooleanBuilder();
		if (appId!=null) {
			builder.and(miniprogramVersion.appId.eq(appId));
		}
		if (versionType!=null) {
			builder.and(miniprogramVersion.versionType.eq(versionType));
		}
		if (status!=null) {
			builder.and(miniprogramVersion.status.eq(status));
		}
		builder.and(miniprogramVersion.hasDeleted.eq(false));
		return getJpaQueryFactory().select(miniprogramVersion).from(miniprogramVersion).where(builder).orderBy(miniprogramVersion.createTime.desc()).fetchFirst();
	}

}
