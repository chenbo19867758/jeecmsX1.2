package com.jeecms.member.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.member.domain.MemberMessage;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberMessage is a Querydsl query type for MemberMessage
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMemberMessage extends EntityPathBase<MemberMessage> {

    private static final long serialVersionUID = 164906933L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberMessage memberMessage = new QMemberMessage("memberMessage");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> memberId = createNumber("memberId", Integer.class);

    public final NumberPath<Integer> messageId = createNumber("messageId", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final com.jeecms.system.domain.querydsl.QSysMessage sysMessage;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QMemberMessage(String variable) {
        this(MemberMessage.class, forVariable(variable), INITS);
    }

    public QMemberMessage(Path<? extends MemberMessage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberMessage(PathMetadata metadata, PathInits inits) {
        this(MemberMessage.class, metadata, inits);
    }

    public QMemberMessage(Class<? extends MemberMessage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sysMessage = inits.isInitialized("sysMessage") ? new com.jeecms.system.domain.querydsl.QSysMessage(forProperty("sysMessage"), inits.get("sysMessage")) : null;
    }

}

