package com.jeecms.member.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.member.domain.MemberLevel;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberLevel is a Querydsl query type for MemberLevel
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMemberLevel extends EntityPathBase<MemberLevel> {

    private static final long serialVersionUID = -1636501838L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberLevel memberLevel = new QMemberLevel("memberLevel");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> integralMax = createNumber("integralMax", Integer.class);

    public final NumberPath<Integer> integralMin = createNumber("integralMin", Integer.class);

    public final NumberPath<Integer> levelIcon = createNumber("levelIcon", Integer.class);

    public final StringPath levelName = createString("levelName");

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData logoResource;

    public final StringPath remark = createString("remark");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final ListPath<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser> users = this.<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser>createList("users", com.jeecms.auth.domain.CoreUser.class, com.jeecms.auth.domain.querydsl.QCoreUser.class, PathInits.DIRECT2);

    public QMemberLevel(String variable) {
        this(MemberLevel.class, forVariable(variable), INITS);
    }

    public QMemberLevel(Path<? extends MemberLevel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberLevel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberLevel(PathMetadata metadata, PathInits inits) {
        this(MemberLevel.class, metadata, inits);
    }

    public QMemberLevel(Class<? extends MemberLevel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.logoResource = inits.isInitialized("logoResource") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("logoResource"), inits.get("logoResource")) : null;
    }

}

