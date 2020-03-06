/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao.impl;

import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.system.dao.ext.LinkDaoExt;
import com.jeecms.system.domain.Link;
import com.jeecms.system.domain.querydsl.QLink;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 友情链接查询实现接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/11 15:25
 */

public class LinkDaoImpl implements LinkDaoExt {

        @Override
        public List<Link> getListBySortNum(Integer fromSortNum, Integer toSortNum, Integer id, Integer linkTypeId) {
                JPAQuery<Link> query = new JPAQuery<Link>(this.em);
                QLink qLink = QLink.link;
                BooleanBuilder exp = new BooleanBuilder();
                query.from(qLink);
                exp.and(qLink.sortNum.loe(fromSortNum));
                exp.and(qLink.sortNum.goe(toSortNum));
                if (id != null) {
                        exp.and(qLink.id.ne(id));
                }
                exp.and(qLink.linkTypeId.eq(linkTypeId));
                query.where(exp);
                query.setHint("org.hibernate.cacheable", true);
                return QuerydslUtils.list(query, null, qLink);
        }

        private EntityManager em;

        @javax.persistence.PersistenceContext
        public void setEm(EntityManager em) {
                this.em = em;
        }
}
