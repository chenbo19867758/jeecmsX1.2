/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.dao.impl;

import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.system.dao.ext.SysLinkTypeDaoExt;
import com.jeecms.system.domain.SysLinkType;
import com.jeecms.system.domain.querydsl.QSysLinkType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 友情链接类别查询实现接口
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/11 14:42
 */

public class SysLinkTypeDaoImpl implements SysLinkTypeDaoExt {
        @Override
        public List<SysLinkType> getListBySortNum(Integer sortNum, Integer id) {
                JPAQuery<SysLinkType> query = new JPAQuery<SysLinkType>(this.em);
                QSysLinkType qSysLinkType = QSysLinkType.sysLinkType;
                BooleanBuilder exp = new BooleanBuilder();
                query.from(qSysLinkType);
                exp.and(qSysLinkType.sortNum.goe(sortNum));
                if (id != null) {
                        exp.and(qSysLinkType.id.ne(id));
                }
                query.where(exp);
                query.setHint("org.hibernate.cacheable", true);
                return QuerydslUtils.list(query, null, qSysLinkType);
        }

        private EntityManager em;

        @javax.persistence.PersistenceContext
        public void setEm(EntityManager em) {
                this.em = em;
        }
}
