package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatMaterial;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWechatMaterial is a Querydsl query type for WechatMaterial
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatMaterial extends EntityPathBase<WechatMaterial> {

    private static final long serialVersionUID = -877160623L;

    public static final QWechatMaterial wechatMaterial = new QWechatMaterial("wechatMaterial");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath materialJson = createString("materialJson");

    public final StringPath mediaId = createString("mediaId");

    public final StringPath mediaType = createString("mediaType");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Long> wechatUpdateTime = createNumber("wechatUpdateTime", Long.class);

    public QWechatMaterial(String variable) {
        super(WechatMaterial.class, forVariable(variable));
    }

    public QWechatMaterial(Path<? extends WechatMaterial> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWechatMaterial(PathMetadata metadata) {
        super(WechatMaterial.class, metadata);
    }

}

