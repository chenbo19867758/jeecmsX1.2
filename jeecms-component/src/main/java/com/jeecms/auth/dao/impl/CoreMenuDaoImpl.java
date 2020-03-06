package com.jeecms.auth.dao.impl;

import java.util.List;

import org.hibernate.jpa.QueryHints;

import com.jeecms.auth.dao.ext.CoreMenuDaoExt;
import com.jeecms.auth.domain.CoreMenu;
import com.jeecms.auth.domain.querydsl.QCoreMenu;
import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * 菜单Dao实现
 * 
 * @author: ztx
 * @date: 2018年10月11日 上午9:06:21
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class CoreMenuDaoImpl extends BaseDao<CoreMenu> implements CoreMenuDaoExt {

        @Override
        public List<CoreMenu> findByParams(Integer parentId, Boolean hasDeleted) throws GlobalException {
                QCoreMenu menu = QCoreMenu.coreMenu;
                Predicate parentPre = null;
                if (parentId == null) {
                        parentPre = menu.parentId.isNull();
                } else {
                        parentPre = menu.parentId.eq(parentId);
                }
                JPAQuery<CoreMenu> query = getJpaQueryFactory().select(menu);
                query.setHint(QueryHints.HINT_CACHEABLE, true);  //增加使用查询缓存
                List<CoreMenu> result = query.from(menu)
                                .where(menu.hasDeleted.eq(hasDeleted).and(parentPre)).orderBy(menu.sortNum.asc())
                                .fetch();
                return result;
        }

}
