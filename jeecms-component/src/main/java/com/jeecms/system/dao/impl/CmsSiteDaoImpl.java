package com.jeecms.system.dao.impl;

import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.page.PaginableRequest;
import com.jeecms.system.dao.ext.CmsSiteDaoExt;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.querydsl.QCmsSite;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.QueryHints;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

public class CmsSiteDaoImpl implements CmsSiteDaoExt {

        @Override
        public List<CmsSite> findByDomain(String domain) {
                JPAQuery<CmsSite> query = new JPAQuery<CmsSite>(this.em);
                QCmsSite site = QCmsSite.cmsSite;
                appendQuery(query, site, domain);
                Paginable paginable = new PaginableRequest(0, Integer.MAX_VALUE);
                return QuerydslUtils.list(query, paginable, site);
        }

        @Override
        public List<CmsSite> findByIds(Collection<Integer> ids) {
                JPAQuery<CmsSite> query = new JPAQuery<CmsSite>(this.em);
                QCmsSite site = QCmsSite.cmsSite;
                query.from(site);
                BooleanBuilder exp = new BooleanBuilder();
                exp.and(site.id.in(ids));
                exp.and(site.hasDeleted.eq(false));
                query.where(exp).orderBy(site.sortNum.desc());
                query.setHint(QueryHints.HINT_CACHEABLE, true);
                return  query.fetch();
        }

        private void appendQuery(JPAQuery<CmsSite> query, QCmsSite site, String domain) {
                query.from(site);
                query.setHint(QueryHints.HINT_CACHEABLE, true);
                BooleanBuilder exp = new BooleanBuilder();
                exp.and(site.hasDeleted.eq(false));
                if (StringUtils.isNotBlank(domain)) {
                        BooleanBuilder exp2 = new BooleanBuilder();
                        exp2.or(site.domain.eq(domain));
                        exp2.or(site.domainAlias.like("%" + domain + "%"));
                        exp.and(exp2);
                }
                query.where(exp);
                query.setHint("org.hibernate.cacheable", true);
                query.orderBy(site.sortNum.asc());
        }

        private EntityManager em;

        @javax.persistence.PersistenceContext
        public void setEm(EntityManager em) {
                this.em = em;
        }

}
