package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentVersion;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentVersion is a Querydsl query type for ContentVersion
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentVersion extends EntityPathBase<ContentVersion> {

    private static final long serialVersionUID = 1646128658L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentVersion contentVersion = new QContentVersion("contentVersion");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final QContent content;

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath remark = createString("remark");

    public final StringPath txt = createString("txt");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath versionCode = createString("versionCode");

    public QContentVersion(String variable) {
        this(ContentVersion.class, forVariable(variable), INITS);
    }

    public QContentVersion(Path<? extends ContentVersion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentVersion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentVersion(PathMetadata metadata, PathInits inits) {
        this(ContentVersion.class, metadata, inits);
    }

    public QContentVersion(Class<? extends ContentVersion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new QContent(forProperty("content"), inits.get("content")) : null;
    }

}

