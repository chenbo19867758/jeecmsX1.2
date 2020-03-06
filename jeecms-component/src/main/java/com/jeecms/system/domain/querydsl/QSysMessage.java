package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysMessage;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSysMessage is a Querydsl query type for SysMessage
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysMessage extends EntityPathBase<SysMessage> {

    private static final long serialVersionUID = -190940633L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysMessage sysMessage = new QSysMessage("sysMessage");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath content = createString("content");

    public final com.jeecms.auth.domain.querydsl.QCoreUser coreUser;

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> memeberGroupId = createNumber("memeberGroupId", Integer.class);

    public final NumberPath<Integer> memeberId = createNumber("memeberId", Integer.class);

    public final NumberPath<Integer> memeberLevelId = createNumber("memeberLevelId", Integer.class);

    public final NumberPath<Integer> orgId = createNumber("orgId", Integer.class);

    public final NumberPath<Integer> recTargetType = createNumber("recTargetType", Integer.class);

    public final StringPath sendUserName = createString("sendUserName");

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QSysMessage(String variable) {
        this(SysMessage.class, forVariable(variable), INITS);
    }

    public QSysMessage(Path<? extends SysMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysMessage(PathMetadata metadata, PathInits inits) {
        this(SysMessage.class, metadata, inits);
    }

    public QSysMessage(Class<? extends SysMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coreUser = inits.isInitialized("coreUser") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("coreUser"), inits.get("coreUser")) : null;
    }

}

