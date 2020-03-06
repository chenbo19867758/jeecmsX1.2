package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatReplyContent;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWechatReplyContent is a Querydsl query type for WechatReplyContent
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatReplyContent extends EntityPathBase<WechatReplyContent> {

    private static final long serialVersionUID = 947882841L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWechatReplyContent wechatReplyContent = new QWechatReplyContent("wechatReplyContent");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath description = createString("description");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final StringPath hqMusicUrll = createString("hqMusicUrll");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isEnable = createBoolean("isEnable");

    public final NumberPath<Integer> mediaId = createNumber("mediaId", Integer.class);

    public final StringPath msgType = createString("msgType");

    public final StringPath musicUrl = createString("musicUrl");

    public final StringPath ruleName = createString("ruleName");

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    public final NumberPath<Integer> thumbMediaId = createNumber("thumbMediaId", Integer.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final QWechatMaterial wechatMaterial;

    public final QWechatReplyClick wechatReplyClick;

    public final ListPath<com.jeecms.wechat.domain.WechatReplyKeyword, QWechatReplyKeyword> wechatReplyKeywordList = this.<com.jeecms.wechat.domain.WechatReplyKeyword, QWechatReplyKeyword>createList("wechatReplyKeywordList", com.jeecms.wechat.domain.WechatReplyKeyword.class, QWechatReplyKeyword.class, PathInits.DIRECT2);

    public QWechatReplyContent(String variable) {
        this(WechatReplyContent.class, forVariable(variable), INITS);
    }

    public QWechatReplyContent(Path<? extends WechatReplyContent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWechatReplyContent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWechatReplyContent(PathMetadata metadata, PathInits inits) {
        this(WechatReplyContent.class, metadata, inits);
    }

    public QWechatReplyContent(Class<? extends WechatReplyContent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.wechatMaterial = inits.isInitialized("wechatMaterial") ? new QWechatMaterial(forProperty("wechatMaterial")) : null;
        this.wechatReplyClick = inits.isInitialized("wechatReplyClick") ? new QWechatReplyClick(forProperty("wechatReplyClick"), inits.get("wechatReplyClick")) : null;
    }

}

