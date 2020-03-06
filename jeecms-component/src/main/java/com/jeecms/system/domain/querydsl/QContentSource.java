package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.ContentSource;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QContentSource is a Querydsl query type for ContentSource
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentSource extends EntityPathBase<ContentSource> {

    private static final long serialVersionUID = -975758041L;

    public static final QContentSource contentSource = new QContentSource("contentSource");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isDefault = createBoolean("isDefault");

    public final BooleanPath isOpenTarget = createBoolean("isOpenTarget");

    public final StringPath sourceLink = createString("sourceLink");

    public final StringPath sourceName = createString("sourceName");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QContentSource(String variable) {
        super(ContentSource.class, forVariable(variable));
    }

    public QContentSource(Path<? extends ContentSource> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentSource(PathMetadata metadata) {
        super(ContentSource.class, metadata);
    }

}

