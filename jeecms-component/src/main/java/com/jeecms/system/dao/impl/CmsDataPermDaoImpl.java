/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao.impl;

import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.common.page.Paginable;
import com.jeecms.system.dao.ext.CmsDataPermDaoExt;
import com.jeecms.system.domain.CmsDataPerm;
import com.jeecms.system.domain.querydsl.QCmsDataPerm;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.jpa.QueryHints;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 数据权限dao扩展实现
 * 
 * @author: tom
 * @date: 2019年3月5日 下午6:28:33
 */
public class CmsDataPermDaoImpl implements CmsDataPermDaoExt {

        @Override
        public List<CmsDataPerm> findList(Integer orgId, Integer roleId, Integer userId, Integer siteId, Short dataType,
                         Short operation, Integer dataId, Paginable paginable) {
                JPAQuery<CmsDataPerm> query = new JPAQuery<CmsDataPerm>(this.em);
                QCmsDataPerm data = QCmsDataPerm.cmsDataPerm;
                appendQuery(query, data, orgId, roleId, userId, siteId, dataType, operation,dataId);
                return QuerydslUtils.list(query, paginable, data);
        }

        private void appendQuery(JPAQuery<CmsDataPerm> query, QCmsDataPerm data, Integer orgId, Integer roleId,
                        Integer userId, Integer siteId, Short dataType, 
                        Short operation,Integer dataId) {
                query.from(data);
                BooleanBuilder exp = new BooleanBuilder();
                exp.and(data.hasDeleted.eq(false));
                if (orgId != null) {
                        exp.and(data.orgId.eq(orgId));
                }
                if (roleId != null) {
                        exp.and(data.roleId.eq(roleId));
                }
                if (userId != null) {
                        exp.and(data.userId.eq(userId));
                }
                if (siteId != null) {
                        exp.and(data.siteId.eq(siteId));
                }
                if (dataType != null) {
                        exp.and(data.dataType.eq(dataType));
                }
                if (operation != null) {
                        exp.and(data.operation.eq(operation));
                }
                if (dataId != null) {
                        exp.and(data.dataId.eq(dataId));
                }
                query.setHint(QueryHints.HINT_CACHEABLE, true);
                query.where(exp);
        }

        private EntityManager em;

        @javax.persistence.PersistenceContext
        public void setEm(EntityManager em) {
                this.em = em;
        }

}
