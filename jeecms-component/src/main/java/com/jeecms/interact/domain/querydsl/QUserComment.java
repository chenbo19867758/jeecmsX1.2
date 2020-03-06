package com.jeecms.interact.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.interact.domain.UserComment;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserComment is a Querydsl query type for UserComment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserComment extends EntityPathBase<UserComment> {

    private static final long serialVersionUID = 1493253408L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserComment userComment = new QUserComment("userComment");

    public final com.jeecms.common.base.domain.querydsl.QAbstractSortDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractSortDomain(this);

    public final ListPath<UserComment, QUserComment> children = this.<UserComment, QUserComment>createList("children", UserComment.class, QUserComment.class, PathInits.DIRECT2);

    public final BooleanPath commentFlag = createBoolean("commentFlag");

    public final StringPath commentText = createString("commentText");

    public final com.jeecms.content.domain.querydsl.QContent content;

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Integer> downCount = createNumber("downCount", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final BooleanPath isReply = createBoolean("isReply");

    public final BooleanPath isReport = createBoolean("isReport");

    public final BooleanPath isTop = createBoolean("isTop");

    public final QUserComment parent;

    public final NumberPath<Integer> parentId = createNumber("parentId", Integer.class);

    public final QUserComment replyAdminComment;

    public final NumberPath<Integer> replyAdminCommentId = createNumber("replyAdminCommentId", Integer.class);

    public final QUserComment replyComment;

    public final NumberPath<Integer> replyCommentId = createNumber("replyCommentId", Integer.class);

    public final DateTimePath<java.util.Date> replyTime = createDateTime("replyTime", java.util.Date.class);

    public final com.jeecms.system.domain.querydsl.QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final NumberPath<Integer> sortNum = _super.sortNum;

    public final NumberPath<Short> status = createNumber("status", Short.class);

    public final NumberPath<Integer> upCount = createNumber("upCount", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final com.jeecms.auth.domain.querydsl.QCoreUser user;

    public final ListPath<com.jeecms.interact.domain.UserCommentReport, QUserCommentReport> userCommentReports = this.<com.jeecms.interact.domain.UserCommentReport, QUserCommentReport>createList("userCommentReports", com.jeecms.interact.domain.UserCommentReport.class, QUserCommentReport.class, PathInits.DIRECT2);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final StringPath visitorArea = createString("visitorArea");

    public QUserComment(String variable) {
        this(UserComment.class, forVariable(variable), INITS);
    }

    public QUserComment(Path<? extends UserComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserComment(PathMetadata metadata, PathInits inits) {
        this(UserComment.class, metadata, inits);
    }

    public QUserComment(Class<? extends UserComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new com.jeecms.content.domain.querydsl.QContent(forProperty("content"), inits.get("content")) : null;
        this.parent = inits.isInitialized("parent") ? new QUserComment(forProperty("parent"), inits.get("parent")) : null;
        this.replyAdminComment = inits.isInitialized("replyAdminComment") ? new QUserComment(forProperty("replyAdminComment"), inits.get("replyAdminComment")) : null;
        this.replyComment = inits.isInitialized("replyComment") ? new QUserComment(forProperty("replyComment"), inits.get("replyComment")) : null;
        this.site = inits.isInitialized("site") ? new com.jeecms.system.domain.querydsl.QCmsSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("user"), inits.get("user")) : null;
    }

}

