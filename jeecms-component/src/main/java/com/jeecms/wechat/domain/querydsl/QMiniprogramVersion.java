package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.MiniprogramVersion;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QMiniprogramVersion is a Querydsl query type for MiniprogramVersion
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMiniprogramVersion extends EntityPathBase<MiniprogramVersion> {

    private static final long serialVersionUID = -943198033L;

    public static final QMiniprogramVersion miniprogramVersion = new QMiniprogramVersion("miniprogramVersion");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath action = createString("action");

    public final StringPath appId = createString("appId");

    public final NumberPath<Integer> auditid = createNumber("auditid", Integer.class);

    public final DateTimePath<java.util.Date> auditTime = createDateTime("auditTime", java.util.Date.class);

    public final StringPath codeDesc = createString("codeDesc");

    public final StringPath codeVersion = createString("codeVersion");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath failReason = createString("failReason");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final NumberPath<Integer> storeId = createNumber("storeId", Integer.class);

    public final NumberPath<Integer> templateId = createNumber("templateId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Integer> versionType = createNumber("versionType", Integer.class);

    public QMiniprogramVersion(String variable) {
        super(MiniprogramVersion.class, forVariable(variable));
    }

    public QMiniprogramVersion(Path<? extends MiniprogramVersion> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMiniprogramVersion(PathMetadata metadata) {
        super(MiniprogramVersion.class, metadata);
    }

}

