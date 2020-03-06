package com.jeecms.audit.domain.querydsl;

import com.jeecms.audit.domain.AuditModelItem;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QAuditModelItem is a Querydsl query type for AuditModelItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAuditModelItem extends EntityPathBase<AuditModelItem> {

    private static final long serialVersionUID = 263328150L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuditModelItem auditModelItem = new QAuditModelItem("auditModelItem");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> auditModelId = createNumber("auditModelId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.jeecms.content.domain.querydsl.QCmsModelItem modelItem;

    public final NumberPath<Integer> modelItemId = createNumber("modelItemId", Integer.class);

    public final StringPath modelItemField = createString("modelItemField");

    public final QAuditModelSet modelSet;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QAuditModelItem(String variable) {
        this(AuditModelItem.class, forVariable(variable), INITS);
    }

    public QAuditModelItem(Path<? extends AuditModelItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuditModelItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuditModelItem(PathMetadata metadata, PathInits inits) {
        this(AuditModelItem.class, metadata, inits);
    }

    public QAuditModelItem(Class<? extends AuditModelItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.modelItem = inits.isInitialized("modelItem") ? new com.jeecms.content.domain.querydsl.QCmsModelItem(forProperty("modelItem"), inits.get("modelItem")) : null;
        this.modelSet = inits.isInitialized("modelSet") ? new QAuditModelSet(forProperty("modelSet"), inits.get("modelSet")) : null;
    }

}

