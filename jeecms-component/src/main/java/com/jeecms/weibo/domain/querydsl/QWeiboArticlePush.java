package com.jeecms.weibo.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.weibo.domain.WeiboArticlePush;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWeiboArticlePush is a Querydsl query type for WeiboArticlePush
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWeiboArticlePush extends EntityPathBase<WeiboArticlePush> {

    private static final long serialVersionUID = -1899884214L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWeiboArticlePush weiboArticlePush = new QWeiboArticlePush("weiboArticlePush");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath articleSourceUrl = createString("articleSourceUrl");

    public final StringPath articleWeiboUrl = createString("articleWeiboUrl");

    public final com.jeecms.content.domain.querydsl.QContent content;

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> pushResult = createNumber("pushResult", Integer.class);

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final StringPath title = createString("title");

    public final StringPath uid = createString("uid");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final QWeiboInfo weiboInfo;

    public QWeiboArticlePush(String variable) {
        this(WeiboArticlePush.class, forVariable(variable), INITS);
    }

    public QWeiboArticlePush(Path<? extends WeiboArticlePush> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWeiboArticlePush(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWeiboArticlePush(PathMetadata metadata, PathInits inits) {
        this(WeiboArticlePush.class, metadata, inits);
    }

    public QWeiboArticlePush(Class<? extends WeiboArticlePush> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new com.jeecms.content.domain.querydsl.QContent(forProperty("content"), inits.get("content")) : null;
        this.weiboInfo = inits.isInitialized("weiboInfo") ? new QWeiboInfo(forProperty("weiboInfo")) : null;
    }

}

