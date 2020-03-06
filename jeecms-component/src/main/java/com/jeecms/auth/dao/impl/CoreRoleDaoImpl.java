/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.dao.impl;

import com.jeecms.auth.dao.ext.CoreRoleDaoExt;
import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.querydsl.QCoreRole;
import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.jpa.QuerydslUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 角色Dao的实现类
 * 
 * @author: tom
 * @date: 2019年3月5日 下午4:48:37
 */
public class CoreRoleDaoImpl extends BaseDao<CoreRole> implements CoreRoleDaoExt {

        @Override
        public Page<CoreRole> pageRole(List<Integer> orgids, String roleName, Pageable pageable) {
                QCoreRole coreRole = QCoreRole.coreRole;
                // 条件查询连接对象
                BooleanBuilder builder = new BooleanBuilder();
                if (orgids != null && !orgids.isEmpty()) {
                        builder.and(coreRole.org.id.in(orgids));
                }
                if (StringUtils.isNotBlank(roleName)) {
                        builder.and(coreRole.roleName.like("%" + roleName + "%"));
                }
                builder.and(coreRole.hasDeleted.eq(false));
                JPAQuery<CoreRole> query = super.getJpaQueryFactory().selectFrom(coreRole);
                query.setHint(QueryHints.HINT_CACHEABLE, true);  //增加使用查询缓存
                query.where(builder);
                return QuerydslUtils.page(query, pageable, coreRole);
        }

        /**
         * 查询列表
         * 
         * @Title: listRole
         * @param orgids
         *                组织ID
         * @param roleName
         *                角色名称
         * @return List
         */
        @Override
        public List<CoreRole> listRole(List<Integer> orgids, String roleName) {
                QCoreRole coreRole = QCoreRole.coreRole;
                // 条件查询连接对象
                BooleanBuilder builder = new BooleanBuilder();
                if (orgids != null && !orgids.isEmpty()) {
                        builder.and(coreRole.org.id.in(orgids));
                }
                if (StringUtils.isNotBlank(roleName)) {
                        builder.and(coreRole.roleName.like("%" + roleName + "%"));
                }
                builder.and(coreRole.hasDeleted.eq(false));
                JPAQuery<CoreRole> query = super.getJpaQueryFactory().selectFrom(coreRole)
                        //创建时间倒序
                        .orderBy(coreRole.createTime.desc());
                query.setHint(QueryHints.HINT_CACHEABLE, false);  //增加使用查询缓存
                query.where(builder);
                return query.fetch();
        }
}
