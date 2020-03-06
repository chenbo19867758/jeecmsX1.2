package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatReplyClick;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWechatReplyClick is a Querydsl query type for WechatReplyClick
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatReplyClick extends EntityPathBase<WechatReplyClick> {

    private static final long serialVersionUID = -1353296792L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWechatReplyClick wechatReplyClick = new QWechatReplyClick("wechatReplyClick");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> replyContentId = createNumber("replyContentId", Integer.class);

    public final NumberPath<Integer> replyType = createNumber("replyType", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final QWechatReplyContent wechatReplyContent;

    public QWechatReplyClick(String variable) {
        this(WechatReplyClick.class, forVariable(variable), INITS);
    }

    public QWechatReplyClick(Path<? extends WechatReplyClick> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWechatReplyClick(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWechatReplyClick(PathMetadata metadata, PathInits inits) {
        this(WechatReplyClick.class, metadata, inits);
    }

    public QWechatReplyClick(Class<? extends WechatReplyClick> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.wechatReplyContent = inits.isInitialized("wechatReplyContent") ? new QWechatReplyContent(forProperty("wechatReplyContent"), inits.get("wechatReplyContent")) : null;
    }

}

