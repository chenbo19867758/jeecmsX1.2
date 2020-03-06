package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.ContentTag;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentTag is a Querydsl query type for ContentTag
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentTag extends EntityPathBase<ContentTag> {

    private static final long serialVersionUID = -322828786L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentTag contentTag = new QContentTag("contentTag");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final ListPath<com.jeecms.content.domain.Content, com.jeecms.content.domain.querydsl.QContent> contentList = this.<com.jeecms.content.domain.Content, com.jeecms.content.domain.querydsl.QContent>createList("contentList", com.jeecms.content.domain.Content.class, com.jeecms.content.domain.querydsl.QContent.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> refCounter = createNumber("refCounter", Integer.class);

    public final QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final StringPath tagName = createString("tagName");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QContentTag(String variable) {
        this(ContentTag.class, forVariable(variable), INITS);
    }

    public QContentTag(Path<? extends ContentTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentTag(PathMetadata metadata, PathInits inits) {
        this(ContentTag.class, metadata, inits);
    }

    public QContentTag(Class<? extends ContentTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QCmsSite(forProperty("site"), inits.get("site")) : null;
    }

}

