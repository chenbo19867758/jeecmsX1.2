package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysOss;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysOss is a Querydsl query type for SysOss
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysOss extends EntityPathBase<SysOss> {

    private static final long serialVersionUID = -1949877969L;

    public static final QSysOss sysOss = new QSysOss("sysOss");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath accessDomain = createString("accessDomain");

    public final StringPath appId = createString("appId");

    public final StringPath appKey = createString("appKey");

    public final StringPath bucketArea = createString("bucketArea");

    public final StringPath bucketName = createString("bucketName");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath endPoint = createString("endPoint");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ossName = createString("ossName");

    public final NumberPath<Short> ossType = createNumber("ossType", Short.class);

    public final StringPath secretId = createString("secretId");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysOss(String variable) {
        super(SysOss.class, forVariable(variable));
    }

    public QSysOss(Path<? extends SysOss> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysOss(PathMetadata metadata) {
        super(SysOss.class, metadata);
    }

}

