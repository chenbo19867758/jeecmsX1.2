package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentRecord;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentRecord is a Querydsl query type for ContentRecord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentRecord extends EntityPathBase<ContentRecord> {

    private static final long serialVersionUID = -1031697449L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentRecord contentRecord = new QContentRecord("contentRecord");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final QContent content;

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    public final NumberPath<Integer> contentRecordId = createNumber("contentRecordId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final StringPath ip = createString("ip");

    public final StringPath operateType = createString("operateType");

    public final StringPath opreateRemark = createString("opreateRemark");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath userName = createString("userName");

    public QContentRecord(String variable) {
        this(ContentRecord.class, forVariable(variable), INITS);
    }

    public QContentRecord(Path<? extends ContentRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentRecord(PathMetadata metadata, PathInits inits) {
        this(ContentRecord.class, metadata, inits);
    }

    public QContentRecord(Class<? extends ContentRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new QContent(forProperty("content"), inits.get("content")) : null;
    }

}

