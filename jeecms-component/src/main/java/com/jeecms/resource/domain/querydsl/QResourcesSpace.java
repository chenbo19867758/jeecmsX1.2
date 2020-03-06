package com.jeecms.resource.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.resource.domain.ResourcesSpace;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QResourcesSpace is a Querydsl query type for ResourcesSpace
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QResourcesSpace extends EntityPathBase<ResourcesSpace> {

    private static final long serialVersionUID = -1183714003L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QResourcesSpace resourcesSpace = new QResourcesSpace("resourcesSpace");

    public final com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain _super;

    //inherited
    public final ListPath<com.jeecms.common.base.domain.AbstractTreeDomain<?, ?>, com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain> childs;

    //inherited
    public final DateTimePath<java.util.Date> createTime;

    //inherited
    public final StringPath createUser;

    public final ListPath<com.jeecms.resource.domain.ResourcesSpaceData, QResourcesSpaceData> datas = this.<com.jeecms.resource.domain.ResourcesSpaceData, QResourcesSpaceData>createList("datas", com.jeecms.resource.domain.ResourcesSpaceData.class, QResourcesSpaceData.class, PathInits.DIRECT2);

    //inherited
    public final BooleanPath hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> isShare = createNumber("isShare", Integer.class);

    //inherited
    public final NumberPath<Integer> lft;

    public final StringPath name = createString("name");

    public final QResourcesSpace parent;

    //inherited
    public final NumberPath<Integer> parentId;

    //inherited
    public final NumberPath<Integer> rgt;

    //inherited
    public final NumberPath<Integer> sortNum;

    //inherited
    public final DateTimePath<java.util.Date> updateTime;

    //inherited
    public final StringPath updateUser;

    public final com.jeecms.auth.domain.querydsl.QCoreUser user;

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final ListPath<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser> users = this.<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser>createList("users", com.jeecms.auth.domain.CoreUser.class, com.jeecms.auth.domain.querydsl.QCoreUser.class, PathInits.DIRECT2);

    public QResourcesSpace(String variable) {
        this(ResourcesSpace.class, forVariable(variable), INITS);
    }

    public QResourcesSpace(Path<? extends ResourcesSpace> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QResourcesSpace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QResourcesSpace(PathMetadata metadata, PathInits inits) {
        this(ResourcesSpace.class, metadata, inits);
    }

    public QResourcesSpace(Class<? extends ResourcesSpace> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain(type, metadata, inits);
        this.childs = _super.childs;
        this.createTime = _super.createTime;
        this.createUser = _super.createUser;
        this.hasDeleted = _super.hasDeleted;
        this.lft = _super.lft;
        this.parent = inits.isInitialized("parent") ? new QResourcesSpace(forProperty("parent")) : null;
        this.parentId = _super.parentId;
        this.rgt = _super.rgt;
        this.sortNum = _super.sortNum;
        this.updateTime = _super.updateTime;
        this.updateUser = _super.updateUser;
        this.user = inits.isInitialized("user") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("user"), inits.get("user")) : null;
    }

}

