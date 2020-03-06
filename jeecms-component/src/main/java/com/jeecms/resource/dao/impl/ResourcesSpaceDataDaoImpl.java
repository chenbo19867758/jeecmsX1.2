package com.jeecms.resource.dao.impl;

import com.jeecms.auth.domain.querydsl.QCoreUser;
import com.jeecms.common.base.dao.BaseDao;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jpa.QuerydslUtils;
import com.jeecms.resource.dao.ext.ResourcesSpaceDataDaoExt;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.querydsl.QResourcesSpaceData;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:ResourcesSpaceData dao查询接口
 * @author: tom
 * @date: 2018年4月16日 上午11:05:40
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */
public class ResourcesSpaceDataDaoImpl extends BaseDao<ResourcesSpaceData> implements ResourcesSpaceDataDaoExt {

        @Override
        public List<ResourcesSpaceData> findByIds(Integer[] ids, Boolean hasDeleted) throws GlobalException {
                QResourcesSpaceData qSpaceData = QResourcesSpaceData.resourcesSpaceData;
                List<ResourcesSpaceData> spaceDatas = new ArrayList<>();
                for (Integer id : ids) {
                        ResourcesSpaceData spaceData = getJpaQueryFactory().select(qSpaceData).from(qSpaceData)
                                        .where(qSpaceData.hasDeleted.eq(hasDeleted).and(qSpaceData.id.eq(id)))
                                        .fetchOne();
                        spaceDatas.add(spaceData);
                }
                return spaceDatas;
        }

        @Override
        public ResourcesSpaceData findByAlias(String alias, Integer userId, Integer storeResourcesSpaceId) {
                JPAQuery<ResourcesSpaceData> jpaQuery = new JPAQuery<ResourcesSpaceData>(em);
                QResourcesSpaceData qSpaceData = QResourcesSpaceData.resourcesSpaceData;
                BooleanBuilder builder = new BooleanBuilder();
                if (alias != null) {
                        builder.and(qSpaceData.alias.eq(alias));
                }
                if (userId != null) {
                        builder.and(qSpaceData.userId.eq(userId));
                }
                if (storeResourcesSpaceId != null) {
                        builder.and(qSpaceData.storeResourcesSpaceId.eq(storeResourcesSpaceId));
                } else {
                        builder.and(qSpaceData.storeResourcesSpaceId.isNull());
                }
                return jpaQuery.from(qSpaceData).where(builder).fetchFirst();
        }

        @Override
        public Page<ResourcesSpaceData> getShareList(String alias, String shareUser, Short resourceType,
                        Integer resourcesSpaceId, Integer userId, Pageable pageable) {
                JPAQuery<ResourcesSpaceData> jpaQuery = new JPAQuery<ResourcesSpaceData>(em);
                QResourcesSpaceData qSpaceData = QResourcesSpaceData.resourcesSpaceData;
                BooleanBuilder builder = new BooleanBuilder();
                jpaQuery.from(qSpaceData);
                if (alias != null) {
                        builder.and(qSpaceData.alias.append(qSpaceData.suffix).contains(alias.trim()));
                }
                if (shareUser != null) {
                        builder.and(qSpaceData.createUser.like("%" + shareUser + "%"));
                }
                if (resourcesSpaceId != null) {
                        builder.and(qSpaceData.storeResourcesSpaceId.eq(resourcesSpaceId));
                }
                if (resourceType != null) {
                        builder.and(qSpaceData.resourceType.eq(resourceType));
                }
                QCoreUser qCoreUser = QCoreUser.coreUser;
                jpaQuery.leftJoin(qSpaceData.users, qCoreUser);
                builder.and(qCoreUser.id.eq(userId));
                builder.and(qSpaceData.shareStatus.eq(ResourcesSpaceData.STATUS_SHARED));
                builder.and(qSpaceData.hasDeleted.eq(false));
                builder.and(qSpaceData.display.eq(true));
                jpaQuery.where(builder);
                return QuerydslUtils.page(jpaQuery, pageable, qSpaceData);
        }

        @Override
        public Page<ResourcesSpaceData> getList(String alias, Integer userId, Integer storeResourcesSpaceId,
                        Short resourceType, Short shareStatus, Date beginCreateTime, Date endCreateTime,
                        Pageable pageable) {
                JPAQuery<ResourcesSpaceData> jpaQuery = new JPAQuery<ResourcesSpaceData>(em);
                QResourcesSpaceData qSpaceData = QResourcesSpaceData.resourcesSpaceData;
                BooleanBuilder builder = new BooleanBuilder();
                if (alias != null) {
                        builder.and(qSpaceData.alias.append(qSpaceData.suffix).contains(alias.trim()));
                }
                if (userId != null) {
                        builder.and(qSpaceData.userId.eq(userId));
                }
                if (storeResourcesSpaceId != null) {
                        builder.and(qSpaceData.storeResourcesSpaceId.eq(storeResourcesSpaceId));
                }
                if (beginCreateTime != null) {
                        builder.and(qSpaceData.createTime.goe(beginCreateTime));
                }
                if (endCreateTime != null) {
                        builder.and(qSpaceData.createTime.loe(endCreateTime));

                }
                if (resourceType != null) {
                        builder.and(qSpaceData.resourceType.eq(resourceType));

                }
                if (shareStatus != null) {
                        builder.and(qSpaceData.shareStatus.eq(shareStatus));
                }
                builder.and(qSpaceData.hasDeleted.eq(false));
                builder.and(qSpaceData.display.eq(true));
                jpaQuery.from(qSpaceData).where(builder);
                return QuerydslUtils.page(jpaQuery, pageable, qSpaceData);
        }

        @Override
        public List<ResourcesSpaceData> findBySpaceIds(List<Integer> spaceIds) {
                JPAQuery<ResourcesSpaceData> jpaQuery = new JPAQuery<ResourcesSpaceData>(em);
                QResourcesSpaceData qSpaceData = QResourcesSpaceData.resourcesSpaceData;
                BooleanBuilder builder = new BooleanBuilder();
                builder.and(qSpaceData.hasDeleted.eq(false));
                builder.and(qSpaceData.display.eq(true));
                builder.and(qSpaceData.storeResourcesSpaceId.in(spaceIds));
                return jpaQuery.from(qSpaceData).where(builder).fetch();
        }

        @Override
        public List<ResourcesSpaceData> getByUserId(Integer userId) {
                JPAQuery<ResourcesSpaceData> jpaQuery = new JPAQuery<ResourcesSpaceData>(em);
                QResourcesSpaceData qSpaceData = QResourcesSpaceData.resourcesSpaceData;
                BooleanBuilder builder = new BooleanBuilder();
                jpaQuery.from(qSpaceData);
                builder.and(qSpaceData.storeResourcesSpaceId.isNull());
                builder.and(qSpaceData.userId.isNotNull());
                builder.and(qSpaceData.display.eq(true));
                //QCoreUser qCoreUser = QCoreUser.coreUser;
                //jpaQuery.leftJoin(qSpaceData.users, qCoreUser);
                //builder.and(qCoreUser.id.eq(userId));
                builder.and(qSpaceData.userId.eq(userId));
                jpaQuery.where(builder);
                return jpaQuery.fetch();
        }

        @Override
        public List<ResourcesSpaceData> findByQuestionnairePic() {
                JPAQuery<ResourcesSpaceData> jpaQuery = new JPAQuery<ResourcesSpaceData>(em);
                QResourcesSpaceData qSpaceData = QResourcesSpaceData.resourcesSpaceData;
                BooleanBuilder builder = new BooleanBuilder();
                builder.and(qSpaceData.hasDeleted.eq(false));
                builder.and(qSpaceData.type.isNotNull());
                return jpaQuery.from(qSpaceData).where(builder).fetch();
        }

        private EntityManager em;

        @javax.persistence.PersistenceContext
        public void setEm(EntityManager em) {
                this.em = em;
        }

}
