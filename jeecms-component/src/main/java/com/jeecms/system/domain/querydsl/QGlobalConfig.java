package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.GlobalConfig;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QGlobalConfig is a Querydsl query type for GlobalConfig
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGlobalConfig extends EntityPathBase<GlobalConfig> {

    private static final long serialVersionUID = 1350477618L;

    public static final QGlobalConfig globalConfig = new QGlobalConfig("globalConfig");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final MapPath<String, String, StringPath> attrs = this.<String, String, StringPath>createMap("attrs", String.class, String.class, StringPath.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QGlobalConfig(String variable) {
        super(GlobalConfig.class, forVariable(variable));
    }

    public QGlobalConfig(Path<? extends GlobalConfig> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGlobalConfig(PathMetadata metadata) {
        super(GlobalConfig.class, metadata);
    }

}

