package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentCountWeek;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QContentCountWeek is a Querydsl query type for ContentCountWeek
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentCountWeek extends EntityPathBase<ContentCountWeek> {

    private static final long serialVersionUID = -154357667L;

    public static final QContentCountWeek contentCountWeek = new QContentCountWeek("contentCountWeek");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> commentsWeek = createNumber("commentsWeek", Integer.class);

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    public final DateTimePath<java.util.Date> countDay = createDateTime("countDay", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Integer> downloadsWeek = createNumber("downloadsWeek", Integer.class);

    public final NumberPath<Integer> downsWeek = createNumber("downsWeek", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Integer> upsWeek = createNumber("upsWeek", Integer.class);

    public final NumberPath<Integer> viewsWeek = createNumber("viewsWeek", Integer.class);

    public QContentCountWeek(String variable) {
        super(ContentCountWeek.class, forVariable(variable));
    }

    public QContentCountWeek(Path<? extends ContentCountWeek> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentCountWeek(PathMetadata metadata) {
        super(ContentCountWeek.class, metadata);
    }

}

