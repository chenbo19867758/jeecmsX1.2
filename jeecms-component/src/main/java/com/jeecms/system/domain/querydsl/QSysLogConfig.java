package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysLogConfig;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysLogConfig is a Querydsl query type for SysLogConfig
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysLogConfig extends EntityPathBase<SysLogConfig> {

    private static final long serialVersionUID = -175832634L;

    public static final QSysLogConfig sysLogConfig = new QSysLogConfig("sysLogConfig");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath dangerSmsTmpId = createString("dangerSmsTmpId");

    public final NumberPath<Integer> dangerValue = createNumber("dangerValue", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath noticeEmailList = createString("noticeEmailList");

    public final StringPath noticeSmsList = createString("noticeSmsList");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath warnSmsTmpId = createString("warnSmsTmpId");

    public final NumberPath<Integer> warnValue = createNumber("warnValue", Integer.class);

    public QSysLogConfig(String variable) {
        super(SysLogConfig.class, forVariable(variable));
    }

    public QSysLogConfig(Path<? extends SysLogConfig> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysLogConfig(PathMetadata metadata) {
        super(SysLogConfig.class, metadata);
    }

}

