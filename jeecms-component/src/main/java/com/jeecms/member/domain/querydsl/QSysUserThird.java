package com.jeecms.member.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.member.domain.SysUserThird;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSysUserThird is a Querydsl query type for SysUserThird
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysUserThird extends EntityPathBase<SysUserThird> {

    private static final long serialVersionUID = 1894938471L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysUserThird sysUserThird = new QSysUserThird("sysUserThird");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.jeecms.auth.domain.querydsl.QCoreUser member;

    public final NumberPath<Integer> memberId = createNumber("memberId", Integer.class);

    public final StringPath openId = createString("openId");

    public final StringPath thirdId = createString("thirdId");

    public final StringPath thirdTypeCode = createString("thirdTypeCode");

    public final StringPath thirdUsername = createString("thirdUsername");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath username = createString("username");

    public QSysUserThird(String variable) {
        this(SysUserThird.class, forVariable(variable), INITS);
    }

    public QSysUserThird(Path<? extends SysUserThird> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysUserThird(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysUserThird(PathMetadata metadata, PathInits inits) {
        this(SysUserThird.class, metadata, inits);
    }

    public QSysUserThird(Class<? extends SysUserThird> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("member"), inits.get("member")) : null;
    }

}

