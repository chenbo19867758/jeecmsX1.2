package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysHotWordCategory;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSysHotWordCategory is a Querydsl query type for SysHotWordCategory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysHotWordCategory extends EntityPathBase<SysHotWordCategory> {

    private static final long serialVersionUID = -391547595L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysHotWordCategory sysHotWordCategory = new QSysHotWordCategory("sysHotWordCategory");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> applyScope = createNumber("applyScope", Integer.class);

    public final StringPath cateName = createString("cateName");

    public final ListPath<com.jeecms.channel.domain.Channel, com.jeecms.channel.domain.querydsl.QChannel> channels = this.<com.jeecms.channel.domain.Channel, com.jeecms.channel.domain.querydsl.QChannel>createList("channels", com.jeecms.channel.domain.Channel.class, com.jeecms.channel.domain.querydsl.QChannel.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysHotWordCategory(String variable) {
        this(SysHotWordCategory.class, forVariable(variable), INITS);
    }

    public QSysHotWordCategory(Path<? extends SysHotWordCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysHotWordCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysHotWordCategory(PathMetadata metadata, PathInits inits) {
        this(SysHotWordCategory.class, metadata, inits);
    }

    public QSysHotWordCategory(Class<? extends SysHotWordCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QCmsSite(forProperty("site"), inits.get("site")) : null;
    }

}

