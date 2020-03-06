package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentCountMonth;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QContentCountMonth is a Querydsl query type for ContentCountMonth
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentCountMonth extends EntityPathBase<ContentCountMonth> {

    private static final long serialVersionUID = -499048649L;

    public static final QContentCountMonth contentCountMonth = new QContentCountMonth("contentCountMonth");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final NumberPath<Integer> commentsMonth = createNumber("commentsMonth", Integer.class);

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    public final DateTimePath<java.util.Date> countDay = createDateTime("countDay", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Integer> downloadsMonth = createNumber("downloadsMonth", Integer.class);

    public final NumberPath<Integer> downsMonth = createNumber("downsMonth", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Integer> upsMonth = createNumber("upsMonth", Integer.class);

    public final NumberPath<Integer> viewsMonth = createNumber("viewsMonth", Integer.class);

    public QContentCountMonth(String variable) {
        super(ContentCountMonth.class, forVariable(variable));
    }

    public QContentCountMonth(Path<? extends ContentCountMonth> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentCountMonth(PathMetadata metadata) {
        super(ContentCountMonth.class, metadata);
    }

}

