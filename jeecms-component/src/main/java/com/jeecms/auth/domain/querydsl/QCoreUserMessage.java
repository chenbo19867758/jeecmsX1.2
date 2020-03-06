package com.jeecms.auth.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.auth.domain.CoreUserMessage;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoreUserMessage is a Querydsl query type for CoreUserMessage
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCoreUserMessage extends EntityPathBase<CoreUserMessage> {

    private static final long serialVersionUID = -792383753L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoreUserMessage coreUserMessage = new QCoreUserMessage("coreUserMessage");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> messageId = createNumber("messageId", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final com.jeecms.system.domain.querydsl.QSysMessage sysMessage;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QCoreUserMessage(String variable) {
        this(CoreUserMessage.class, forVariable(variable), INITS);
    }

    public QCoreUserMessage(Path<? extends CoreUserMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoreUserMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoreUserMessage(PathMetadata metadata, PathInits inits) {
        this(CoreUserMessage.class, metadata, inits);
    }

    public QCoreUserMessage(Class<? extends CoreUserMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sysMessage = inits.isInitialized("sysMessage") ? new com.jeecms.system.domain.querydsl.QSysMessage(forProperty("sysMessage"), inits.get("sysMessage")) : null;
    }

}

