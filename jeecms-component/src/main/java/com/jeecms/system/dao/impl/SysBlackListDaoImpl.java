package com.jeecms.system.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.auth.domain.querydsl.QCoreUser;
import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.system.dao.ext.SysBlackListDaoExt;
import com.jeecms.system.domain.SysBlackList;
import com.jeecms.system.domain.querydsl.QSysBlackList;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * 黑名单dao实现类
 * 
 * @author: chenming
 * @date: 2019年5月8日 上午11:03:02
 */
public class SysBlackListDaoImpl extends BaseDao<SysBlackList> implements SysBlackListDaoExt {

	@Override
	public List<SysBlackList> findByIpAndUserId(Integer siteId, String ip, Integer userId) {
		QSysBlackList sysBlackList = QSysBlackList.sysBlackList;
		BooleanBuilder exp = new BooleanBuilder();
		if (siteId == null) {
			return null;
		}
		exp.and(sysBlackList.siteId.eq(siteId));
		if (ip != null) {
			exp.and(sysBlackList.ip.eq(ip));
		}
		if (userId != null) {
			exp.and(sysBlackList.userId.eq(userId));
		}
		exp.and(sysBlackList.type.eq(SysBlackList.USER_COMMENT_TYPE));
		return getJpaQueryFactory().select(sysBlackList).from(sysBlackList).where(exp)
				.orderBy(sysBlackList.createTime.desc()).fetch();
	}

	@Override
	public Page<SysBlackList> getPage(boolean status, String userName, String ip, 
			Integer siteId, Pageable pageable) {
		QSysBlackList sysBlackList = QSysBlackList.sysBlackList;
		BooleanBuilder exp = new BooleanBuilder();
		if (siteId == null) {
			return null;
		}
		exp.and(sysBlackList.siteId.eq(siteId));
		if (status) {
			exp.and(sysBlackList.user.isNotNull());
			if (userName != null) {
				exp.and(sysBlackList.userName.like("%" + userName + "%"));
			}
		} else {
			exp.and(sysBlackList.ip.isNotNull());
			if (ip != null) {
				exp.and(sysBlackList.ip.like("%" + ip + "%"));
			}
		}
		exp.and(sysBlackList.type.eq(SysBlackList.USER_COMMENT_TYPE));
		JPAQuery<SysBlackList> query = new JPAQuery<SysBlackList>(this.em);
		if (status) {
			QCoreUser coreUser = QCoreUser.coreUser;
			query.from(sysBlackList).join(coreUser).on(sysBlackList.userId.eq(coreUser.id)).where(exp).orderBy(sysBlackList.createTime.desc());
		} else {
			query.from(sysBlackList).where(exp).orderBy(sysBlackList.createTime.desc());
		}
		return QuerydslUtils.page(query, pageable, sysBlackList);
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

}
