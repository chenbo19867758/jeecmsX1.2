package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysBlackList;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSysBlackList is a Querydsl query type for SysBlackList
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysBlackList extends EntityPathBase<SysBlackList> {

    private static final long serialVersionUID = -333246627L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysBlackList sysBlackList = new QSysBlackList("sysBlackList");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final com.jeecms.auth.domain.querydsl.QCoreUser user;

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final StringPath userName = createString("userName");

    public QSysBlackList(String variable) {
        this(SysBlackList.class, forVariable(variable), INITS);
    }

    public QSysBlackList(Path<? extends SysBlackList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysBlackList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysBlackList(PathMetadata metadata, PathInits inits) {
        this(SysBlackList.class, metadata, inits);
    }

    public QSysBlackList(Class<? extends SysBlackList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QCmsSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("user"), inits.get("user")) : null;
    }

}

