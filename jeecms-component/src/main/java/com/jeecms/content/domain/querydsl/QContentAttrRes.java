package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentAttrRes;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentAttrRes is a Querydsl query type for ContentAttrRes
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentAttrRes extends EntityPathBase<ContentAttrRes> {

    private static final long serialVersionUID = 619652649L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentAttrRes contentAttrRes = new QContentAttrRes("contentAttrRes");

    public final QContentAttr contentAttr;

    public final NumberPath<Integer> contentAttrId = createNumber("contentAttrId", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> resId = createNumber("resId", Integer.class);

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData resourcesSpaceData;

    public final com.jeecms.system.domain.querydsl.QSysSecret secret;

    public final NumberPath<Integer> secretId = createNumber("secretId", Integer.class);

    public QContentAttrRes(String variable) {
        this(ContentAttrRes.class, forVariable(variable), INITS);
    }

    public QContentAttrRes(Path<? extends ContentAttrRes> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentAttrRes(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentAttrRes(PathMetadata metadata, PathInits inits) {
        this(ContentAttrRes.class, metadata, inits);
    }

    public QContentAttrRes(Class<? extends ContentAttrRes> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contentAttr = inits.isInitialized("contentAttr") ? new QContentAttr(forProperty("contentAttr"), inits.get("contentAttr")) : null;
        this.resourcesSpaceData = inits.isInitialized("resourcesSpaceData") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("resourcesSpaceData"), inits.get("resourcesSpaceData")) : null;
        this.secret = inits.isInitialized("secret") ? new com.jeecms.system.domain.querydsl.QSysSecret(forProperty("secret")) : null;
    }

}

