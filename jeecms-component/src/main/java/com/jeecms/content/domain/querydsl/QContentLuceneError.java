package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentLuceneError;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QContentLuceneError is a Querydsl query type for ContentLuceneError
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentLuceneError extends EntityPathBase<ContentLuceneError> {

    private static final long serialVersionUID = 824979648L;

    public static final QContentLuceneError contentLuceneError = new QContentLuceneError("contentLuceneError");

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Short> luceneOp = createNumber("luceneOp", Short.class);

    public QContentLuceneError(String variable) {
        super(ContentLuceneError.class, forVariable(variable));
    }

    public QContentLuceneError(Path<? extends ContentLuceneError> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentLuceneError(PathMetadata metadata) {
        super(ContentLuceneError.class, metadata);
    }

}

