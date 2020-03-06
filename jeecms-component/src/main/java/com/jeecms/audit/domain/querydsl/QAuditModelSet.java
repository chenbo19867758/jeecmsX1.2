package com.jeecms.audit.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.audit.domain.AuditModelSet;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuditModelSet is a Querydsl query type for AuditModelSet
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAuditModelSet extends EntityPathBase<AuditModelSet> {

    private static final long serialVersionUID = -2069706369L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuditModelSet auditModelSet = new QAuditModelSet("auditModelSet");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<com.jeecms.audit.domain.AuditModelItem, QAuditModelItem> items = this.<com.jeecms.audit.domain.AuditModelItem, QAuditModelItem>createList("items", com.jeecms.audit.domain.AuditModelItem.class, QAuditModelItem.class, PathInits.DIRECT2);

    public final com.jeecms.content.domain.querydsl.QCmsModel model;

    public final NumberPath<Integer> modelId = createNumber("modelId", Integer.class);

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QAuditModelSet(String variable) {
        this(AuditModelSet.class, forVariable(variable), INITS);
    }

    public QAuditModelSet(Path<? extends AuditModelSet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuditModelSet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuditModelSet(PathMetadata metadata, PathInits inits) {
        this(AuditModelSet.class, metadata, inits);
    }

    public QAuditModelSet(Class<? extends AuditModelSet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.model = inits.isInitialized("model") ? new com.jeecms.content.domain.querydsl.QCmsModel(forProperty("model"), inits.get("model")) : null;
    }

}

