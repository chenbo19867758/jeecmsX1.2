package com.jeecms.weibo.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.weibo.domain.WeiboAppConfig;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWeiboAppConfig is a Querydsl query type for WeiboAppConfig
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWeiboAppConfig extends EntityPathBase<WeiboAppConfig> {

    private static final long serialVersionUID = -1571367075L;

    public static final QWeiboAppConfig weiboAppConfig = new QWeiboAppConfig("weiboAppConfig");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    public final StringPath appSecret = createString("appSecret");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath description = createString("description");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QWeiboAppConfig(String variable) {
        super(WeiboAppConfig.class, forVariable(variable));
    }

    public QWeiboAppConfig(Path<? extends WeiboAppConfig> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWeiboAppConfig(PathMetadata metadata) {
        super(WeiboAppConfig.class, metadata);
    }

}

