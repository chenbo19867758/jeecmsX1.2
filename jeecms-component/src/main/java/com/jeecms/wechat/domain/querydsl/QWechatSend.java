package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatSend;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWechatSend is a Querydsl query type for WechatSend
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatSend extends EntityPathBase<WechatSend> {

    private static final long serialVersionUID = 1703782066L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWechatSend wechatSend = new QWechatSend("wechatSend");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> materialId = createNumber("materialId", Integer.class);

    public final StringPath materialJson = createString("materialJson");

    public final StringPath msgDataId = createString("msgDataId");

    public final DateTimePath<java.util.Date> sendDate = createDateTime("sendDate", java.util.Date.class);

    public final NumberPath<Integer> sendHour = createNumber("sendHour", Integer.class);

    public final NumberPath<Integer> sendMinute = createNumber("sendMinute", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final NumberPath<Integer> tagId = createNumber("tagId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final QWechatMaterial wechatMaterial;

    public QWechatSend(String variable) {
        this(WechatSend.class, forVariable(variable), INITS);
    }

    public QWechatSend(Path<? extends WechatSend> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWechatSend(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWechatSend(PathMetadata metadata, PathInits inits) {
        this(WechatSend.class, metadata, inits);
    }

    public QWechatSend(Class<? extends WechatSend> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.wechatMaterial = inits.isInitialized("wechatMaterial") ? new QWechatMaterial(forProperty("wechatMaterial")) : null;
    }

}

