package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentRelation;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentRelation is a Querydsl query type for ContentRelation
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentRelation extends EntityPathBase<ContentRelation> {

    private static final long serialVersionUID = 920997890L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentRelation contentRelation = new QContentRelation("contentRelation");

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

    public final QContent relationContent;

    public final NumberPath<Integer> relationContentId = createNumber("relationContentId", Integer.class);

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    public final NumberPath<Integer> sortWeight = createNumber("sortWeight", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QContentRelation(String variable) {
        this(ContentRelation.class, forVariable(variable), INITS);
    }

    public QContentRelation(Path<? extends ContentRelation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentRelation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentRelation(PathMetadata metadata, PathInits inits) {
        this(ContentRelation.class, metadata, inits);
    }

    public QContentRelation(Class<? extends ContentRelation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new QContent(forProperty("content"), inits.get("content")) : null;
        this.relationContent = inits.isInitialized("relationContent") ? new QContent(forProperty("relationContent"), inits.get("relationContent")) : null;
    }

}

