package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatSendArticle;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWechatSendArticle is a Querydsl query type for WechatSendArticle
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatSendArticle extends EntityPathBase<WechatSendArticle> {

    private static final long serialVersionUID = 655241700L;

    public static final QWechatSendArticle wechatSendArticle = new QWechatSendArticle("wechatSendArticle");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> maxUserCommentId = createNumber("maxUserCommentId", Integer.class);

    public final StringPath msgDataId = createString("msgDataId");

    public final NumberPath<Integer> msgDataIndex = createNumber("msgDataIndex", Integer.class);

    public final StringPath title = createString("title");
    
    public final NumberPath<Integer> open = createNumber("open", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QWechatSendArticle(String variable) {
        super(WechatSendArticle.class, forVariable(variable));
    }

    public QWechatSendArticle(Path<? extends WechatSendArticle> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWechatSendArticle(PathMetadata metadata) {
        super(WechatSendArticle.class, metadata);
    }

}

