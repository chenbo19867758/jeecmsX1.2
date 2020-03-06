package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentAnnotation;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QContentAnnotation is a Querydsl query type for ContentAnnotation
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentAnnotation extends EntityPathBase<ContentAnnotation> {

    private static final long serialVersionUID = -1002186827L;

    public static final QContentAnnotation contentAnnotation = new QContentAnnotation("contentAnnotation");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> annoId = createNumber("annoId", Integer.class);

    public final NumberPath<Integer> annoPosition = createNumber("annoPosition", Integer.class);

    public final StringPath annotation = createString("annotation");

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QContentAnnotation(String variable) {
        super(ContentAnnotation.class, forVariable(variable));
    }

    public QContentAnnotation(Path<? extends ContentAnnotation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentAnnotation(PathMetadata metadata) {
        super(ContentAnnotation.class, metadata);
    }

}

