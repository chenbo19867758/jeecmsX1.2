package com.jeecms.audit.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.audit.domain.AuditStrategy;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuditStrategy is a Querydsl query type for AuditStrategy
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAuditStrategy extends EntityPathBase<AuditStrategy> {

    private static final long serialVersionUID = 337141881L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuditStrategy auditStrategy = new QAuditStrategy("auditStrategy");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final ListPath<com.jeecms.audit.domain.AuditChannelSet, QAuditChannelSet> channelSets = this.<com.jeecms.audit.domain.AuditChannelSet, QAuditChannelSet>createList("channelSets", com.jeecms.audit.domain.AuditChannelSet.class, QAuditChannelSet.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isPicture = createBoolean("isPicture");

    public final BooleanPath isText = createBoolean("isText");

    public final StringPath name = createString("name");

    public final StringPath pictureScene = createString("pictureScene");

    public final com.jeecms.system.domain.querydsl.QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final BooleanPath status = createBoolean("status");

    public final StringPath textScene = createString("textScene");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QAuditStrategy(String variable) {
        this(AuditStrategy.class, forVariable(variable), INITS);
    }

    public QAuditStrategy(Path<? extends AuditStrategy> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuditStrategy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuditStrategy(PathMetadata metadata, PathInits inits) {
        this(AuditStrategy.class, metadata, inits);
    }

    public QAuditStrategy(Class<? extends AuditStrategy> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new com.jeecms.system.domain.querydsl.QCmsSite(forProperty("site"), inits.get("site")) : null;
    }

}

