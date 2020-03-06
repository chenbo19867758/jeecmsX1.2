package com.jeecms.member.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.member.domain.UserCollection;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserCollection is a Querydsl query type for UserCollection
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserCollection extends EntityPathBase<UserCollection> {

    private static final long serialVersionUID = 449867297L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserCollection userCollection = new QUserCollection("userCollection");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final com.jeecms.content.domain.querydsl.QContent content;

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final com.jeecms.auth.domain.querydsl.QCoreUser user;

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QUserCollection(String variable) {
        this(UserCollection.class, forVariable(variable), INITS);
    }

    public QUserCollection(Path<? extends UserCollection> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserCollection(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserCollection(PathMetadata metadata, PathInits inits) {
        this(UserCollection.class, metadata, inits);
    }

    public QUserCollection(Class<? extends UserCollection> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new com.jeecms.content.domain.querydsl.QContent(forProperty("content"), inits.get("content")) : null;
        this.user = inits.isInitialized("user") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("user"), inits.get("user")) : null;
    }

}

