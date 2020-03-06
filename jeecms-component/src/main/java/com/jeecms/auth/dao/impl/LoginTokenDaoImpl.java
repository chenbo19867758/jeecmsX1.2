/**
 * 
 */

package com.jeecms.auth.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.jpa.QueryHints;

import com.jeecms.auth.dao.ext.LoginTokenDaoExt;
import com.jeecms.auth.domain.LoginToken;
import com.jeecms.auth.domain.querydsl.QLoginToken;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.common.page.Paginable;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * token dao扩展实现
 * 
 * @author: tom
 * @date: 2018年7月19日 下午6:44:07
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class LoginTokenDaoImpl implements LoginTokenDaoExt {

        @Override
        public List<LoginToken> getExpireTokenList(Paginable paginable) {
                JPAQuery<LoginToken> query = new JPAQuery<LoginToken>(this.em);
                QLoginToken loginToken = QLoginToken.loginToken;
                predicate(query, loginToken);
                query.setHint(QueryHints.HINT_CACHEABLE, true);  //增加使用查询缓存
                return QuerydslUtils.list(query, paginable, loginToken);
        }

        @Override
        public long getExpireTokenCount() {
                JPAQuery<LoginToken> query = new JPAQuery<LoginToken>(this.em);
                QLoginToken loginToken = QLoginToken.loginToken;
                predicate(query, loginToken);
                query.setHint(QueryHints.HINT_CACHEABLE, true);  //增加使用查询缓存
                return query.fetchCount();
        }

        private JPAQuery<LoginToken> predicate(JPAQuery<LoginToken> query, QLoginToken loginToken) {
                query.from(loginToken);
                BooleanBuilder exp = new BooleanBuilder();
                Date now = Calendar.getInstance().getTime();
                exp.and(loginToken.expireTime.before(now));
                query.where(exp);
                return query;
        }

        private EntityManager em;

        @PersistenceContext
        public void setEm(EntityManager em) {
                this.em = em;
        }

}
