/**
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.dao.impl;

import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.resource.dao.ext.ResourcesSpaceDaoExt;
import com.jeecms.resource.domain.ResourcesSpace;
import com.jeecms.resource.domain.querydsl.QResourcesSpace;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @Description:ResourcesSpace dao查询接口
 * @author: tom
 * @date: 2018年4月16日 上午11:05:40
 */
@Repository
public class ResourcesSpaceDaoImpl extends BaseDao<ResourcesSpace> implements ResourcesSpaceDaoExt {

	/**
	 * 大于
	 */
	public static final String MORE_THAN = "GT";
	/**
	 * 大于等于
	 */
	public static final String MORE_EQUAL_THAN = "GTE";

	@Override
	public List<ResourcesSpace> getListBySortNum(Integer id, Integer parentId, Integer sortNum, String compare) {
		JPAQuery<ResourcesSpace> query = new JPAQuery<ResourcesSpace>(this.em);
		QResourcesSpace qResourcesSpace = QResourcesSpace.resourcesSpace;
		BooleanBuilder exp = new BooleanBuilder();
		query.from(qResourcesSpace);
		if (StringUtils.isNotBlank(compare)) {
			if (MORE_THAN.equalsIgnoreCase(compare)) {
				exp.and(qResourcesSpace.sortNum.gt(sortNum));
			} else if (MORE_EQUAL_THAN.equalsIgnoreCase(compare)) {
				exp.and(qResourcesSpace.sortNum.goe(sortNum));
			}
		}
		if (parentId != null) {
			exp.and(qResourcesSpace.parentId.eq(parentId));
		} else {
			exp.and(qResourcesSpace.parentId.isNull());
		}
		if (id != null) {
			exp.and(qResourcesSpace.id.ne(id));
		}
		query.where(exp);
		return QuerydslUtils.list(query, null, qResourcesSpace);
	}

	@Override
	public List<ResourcesSpace> getListByUserIdAndShare(Integer userId, Boolean isShare) {
		QResourcesSpace qResourcesSpace = QResourcesSpace.resourcesSpace;
		BooleanBuilder exp = new BooleanBuilder();
		if (userId != null) {
			exp.and(qResourcesSpace.userId.eq(userId));
		}
		if (isShare != null) {
			if (isShare) {
				exp.and(qResourcesSpace.isShare.ne(ResourcesSpace.NOT_SHARED));
			} else {
				exp.and(qResourcesSpace.isShare.eq(ResourcesSpace.NOT_SHARED));
			}
		}
		return getJpaQueryFactory().select(qResourcesSpace)
				.from(qResourcesSpace)
				.where(exp).fetch();
	}

	private EntityManager em;

	@javax.persistence.PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
