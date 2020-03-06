package com.jeecms.auth.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.auth.domain.CoreUserExt;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoreUserExt is a Querydsl query type for CoreUserExt
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCoreUserExt extends EntityPathBase<CoreUserExt> {

    private static final long serialVersionUID = 514371857L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoreUserExt coreUserExt = new QCoreUserExt("coreUserExt");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final DateTimePath<java.util.Date> birthday = createDateTime("birthday", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Integer> gender = createNumber("gender", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath linephone = createString("linephone");

    public final StringPath lockedIp = createString("lockedIp");

    public final DateTimePath<java.util.Date> lockedTime = createDateTime("lockedTime", java.util.Date.class);

    public final StringPath realname = createString("realname");

    public final StringPath remark = createString("remark");

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData resourcesSpaceData;

    public final StringPath shareOrgId = createString("shareOrgId");

    public final StringPath shareRoleId = createString("shareRoleId");

    public final StringPath shareUserId = createString("shareUserId");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final QCoreUser user;

    public final NumberPath<Integer> userImgId = createNumber("userImgId", Integer.class);

    public QCoreUserExt(String variable) {
        this(CoreUserExt.class, forVariable(variable), INITS);
    }

    public QCoreUserExt(Path<? extends CoreUserExt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoreUserExt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoreUserExt(PathMetadata metadata, PathInits inits) {
        this(CoreUserExt.class, metadata, inits);
    }

    public QCoreUserExt(Class<? extends CoreUserExt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.resourcesSpaceData = inits.isInitialized("resourcesSpaceData") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("resourcesSpaceData"), inits.get("resourcesSpaceData")) : null;
        this.user = inits.isInitialized("user") ? new QCoreUser(forProperty("user"), inits.get("user")) : null;
    }

}

