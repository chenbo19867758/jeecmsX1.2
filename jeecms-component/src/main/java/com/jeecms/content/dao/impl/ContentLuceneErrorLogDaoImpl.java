/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.jpa.QueryHints;

import com.jeecms.common.page.Paginable;
import com.jeecms.content.dao.ext.ContentLuceneErrorLogExt;
import com.jeecms.content.domain.ContentLuceneError;
import com.jeecms.content.domain.querydsl.QContentLuceneError;
import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.jpa.QuerydslUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**   
 * 内容索引异常dao实现
 * @author: tom
 * @date:   2019年5月27日 上午11:31:40     
 */
public class ContentLuceneErrorLogDaoImpl extends BaseDao<ContentLuceneError> implements ContentLuceneErrorLogExt {

        @Override
        public List<ContentLuceneError> getList(Integer contentId, Short op, Paginable paginable) {
                JPAQuery<ContentLuceneError> query = new JPAQuery<ContentLuceneError>(this.em);
                query.setHint(QueryHints.HINT_CACHEABLE, true);
                QContentLuceneError luceneError = QContentLuceneError.contentLuceneError;
                query.from(luceneError);
                BooleanBuilder exp = new BooleanBuilder();
                if (contentId != null) {
                        exp.and(luceneError.contentId.eq(contentId));
                }

                if (op != null) {
                        exp.and(luceneError.luceneOp.eq(op));
                }
                query.where(exp);

                return QuerydslUtils.list(query, paginable, luceneError);
        }

        private EntityManager em;

        @javax.persistence.PersistenceContext
        public void setEm(EntityManager em) {
                this.em = em;
        }

}
