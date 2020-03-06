package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatComment;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWechatComment is a Querydsl query type for WechatComment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatComment extends EntityPathBase<WechatComment> {

    private static final long serialVersionUID = -1581396235L;

    public static final QWechatComment wechatComment = new QWechatComment("wechatComment");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    public final DateTimePath<java.util.Date> commentTime = createDateTime("commentTime", java.util.Date.class);

    public final BooleanPath commentType = createBoolean("commentType");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath msgDataId = createString("msgDataId");

    public final NumberPath<Integer> msgDataIndex = createNumber("msgDataIndex", Integer.class);

    public final StringPath openid = createString("openid");

    public final StringPath replyContent = createString("replyContent");

    public final DateTimePath<java.util.Date> replyTime = createDateTime("replyTime", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath userCommentId = createString("userCommentId");

    public QWechatComment(String variable) {
        super(WechatComment.class, forVariable(variable));
    }

    public QWechatComment(Path<? extends WechatComment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWechatComment(PathMetadata metadata) {
        super(WechatComment.class, metadata);
    }

}

