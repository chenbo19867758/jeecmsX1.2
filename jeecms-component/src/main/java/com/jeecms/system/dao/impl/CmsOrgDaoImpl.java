/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.QueryHints;

import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.common.page.Paginable;
import com.jeecms.system.dao.ext.CmsOrgDaoExt;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.querydsl.QCmsOrg;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * 组织dao扩展实现
 * 
 * @author: tom
 * @date: 2019年3月21日 下午2:04:39
 */
public class CmsOrgDaoImpl implements CmsOrgDaoExt {

	@Override
	public List<CmsOrg> findList(Integer parentId, Boolean isVirtual, String key, Paginable paginable) {
		JPAQuery<CmsOrg> query = new JPAQuery<CmsOrg>(this.em);
		QCmsOrg org = QCmsOrg.cmsOrg;
		appendQuery(query, org, parentId, null, isVirtual, key);
		return QuerydslUtils.list(query, paginable, org);
	}

	private void appendQuery(JPAQuery<CmsOrg> query, QCmsOrg org, Integer parentId, 
			List<Integer> ids, Boolean isVirtual, String key) {
		query.from(org);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		BooleanBuilder exp = new BooleanBuilder();
		if (parentId != null && parentId != 0) {
			exp.and(org.parentId.eq(parentId));
		} 
		if (ids != null && !ids.isEmpty()) {
			exp.and(org.id.in(ids));
		} 
		if (isVirtual != null) {
			exp.and(org.isVirtual.eq(isVirtual));
		}
		if (StringUtils.isNotBlank(key)) {
			exp.and(org.name.like("%" + key + "%")
					.or(org.orgNum.like("%" + key).or(org.orgLeader.like("%" + key + "%"))));
		}
		exp.and(org.hasDeleted.eq(false));
		query.where(exp);
		//比较权重排序
		query.orderBy(org.sortNum.asc()).orderBy(org.sortWeight.asc());
	}

	@Override
	public List<CmsOrg> findListByIDS(List<Integer> ids, Boolean isVirtual, String key, Paginable paginable) {
		JPAQuery<CmsOrg> query = new JPAQuery<CmsOrg>(this.em);
		QCmsOrg org = QCmsOrg.cmsOrg;
		appendQuery(query, org, null, ids, isVirtual, key);
		return QuerydslUtils.list(query, paginable, org);
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
