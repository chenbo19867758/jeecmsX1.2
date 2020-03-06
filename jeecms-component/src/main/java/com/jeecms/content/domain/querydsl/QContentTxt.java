package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentTxt;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentTxt is a Querydsl query type for ContentTxt
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentTxt extends EntityPathBase<ContentTxt> {

    private static final long serialVersionUID = 2103984298L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentTxt contentTxt = new QContentTxt("contentTxt");

    public final StringPath attrKey = createString("attrKey");

    public final StringPath attrTxt = createString("attrTxt");

    public final QContent content;

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QContentTxt(String variable) {
        this(ContentTxt.class, forVariable(variable), INITS);
    }

    public QContentTxt(Path<? extends ContentTxt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentTxt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentTxt(PathMetadata metadata, PathInits inits) {
        this(ContentTxt.class, metadata, inits);
    }

    public QContentTxt(Class<? extends ContentTxt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new QContent(forProperty("content"), inits.get("content")) : null;
    }

}

