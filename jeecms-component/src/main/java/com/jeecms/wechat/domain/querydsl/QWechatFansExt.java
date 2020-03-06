package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatFansExt;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWechatFansExt is a Querydsl query type for WechatFansExt
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatFansExt extends EntityPathBase<WechatFansExt> {

    private static final long serialVersionUID = 681378519L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWechatFansExt wechatFansExt = new QWechatFansExt("wechatFansExt");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> commentCount = createNumber("commentCount", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final QWechatFans fans;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> mesCount = createNumber("mesCount", Integer.class);

    public final StringPath openid = createString("openid");

    public final NumberPath<Integer> topCommentCount = createNumber("topCommentCount", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QWechatFansExt(String variable) {
        this(WechatFansExt.class, forVariable(variable), INITS);
    }

    public QWechatFansExt(Path<? extends WechatFansExt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWechatFansExt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWechatFansExt(PathMetadata metadata, PathInits inits) {
        this(WechatFansExt.class, metadata, inits);
    }

    public QWechatFansExt(Class<? extends WechatFansExt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fans = inits.isInitialized("fans") ? new QWechatFans(forProperty("fans"), inits.get("fans")) : null;
    }

}

