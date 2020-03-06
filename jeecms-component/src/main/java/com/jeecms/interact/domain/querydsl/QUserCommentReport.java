package com.jeecms.interact.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.interact.domain.UserCommentReport;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserCommentReport is a Querydsl query type for UserCommentReport
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserCommentReport extends EntityPathBase<UserCommentReport> {

    private static final long serialVersionUID = -556259724L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserCommentReport userCommentReport = new QUserCommentReport("userCommentReport");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> commentId = createNumber("commentId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final NumberPath<Integer> replyUserId = createNumber("replyUserId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final com.jeecms.auth.domain.querydsl.QCoreUser user;

    public final QUserComment userComment;

    public QUserCommentReport(String variable) {
        this(UserCommentReport.class, forVariable(variable), INITS);
    }

    public QUserCommentReport(Path<? extends UserCommentReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserCommentReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserCommentReport(PathMetadata metadata, PathInits inits) {
        this(UserCommentReport.class, metadata, inits);
    }

    public QUserCommentReport(Class<? extends UserCommentReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("user"), inits.get("user")) : null;
        this.userComment = inits.isInitialized("userComment") ? new QUserComment(forProperty("userComment"), inits.get("userComment")) : null;
    }

}

