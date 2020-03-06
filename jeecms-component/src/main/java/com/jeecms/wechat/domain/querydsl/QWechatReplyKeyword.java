package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatReplyKeyword;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWechatReplyKeyword is a Querydsl query type for WechatReplyKeyword
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatReplyKeyword extends EntityPathBase<WechatReplyKeyword> {

    private static final long serialVersionUID = -818055991L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWechatReplyKeyword wechatReplyKeyword = new QWechatReplyKeyword("wechatReplyKeyword");

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

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final QWechatReplyContent wechatReplyContent;

    public final StringPath wordkeyEq = createString("wordkeyEq");

    public final StringPath wordkeyLike = createString("wordkeyLike");

    public QWechatReplyKeyword(String variable) {
        this(WechatReplyKeyword.class, forVariable(variable), INITS);
    }

    public QWechatReplyKeyword(Path<? extends WechatReplyKeyword> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWechatReplyKeyword(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWechatReplyKeyword(PathMetadata metadata, PathInits inits) {
        this(WechatReplyKeyword.class, metadata, inits);
    }

    public QWechatReplyKeyword(Class<? extends WechatReplyKeyword> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.wechatReplyContent = inits.isInitialized("wechatReplyContent") ? new QWechatReplyContent(forProperty("wechatReplyContent"), inits.get("wechatReplyContent")) : null;
    }

}

