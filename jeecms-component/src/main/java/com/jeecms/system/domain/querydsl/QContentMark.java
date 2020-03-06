package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.ContentMark;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QContentMark is a Querydsl query type for ContentMark
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentMark extends EntityPathBase<ContentMark> {

    private static final long serialVersionUID = -1417965863L;

    public static final QContentMark contentMark = new QContentMark("contentMark");

    public final com.jeecms.common.base.domain.querydsl.QAbstractSortDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractSortDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath markName = createString("markName");

    public final NumberPath<Integer> markType = createNumber("markType", Integer.class);

    //inherited
    public final NumberPath<Integer> sortNum = _super.sortNum;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QContentMark(String variable) {
        super(ContentMark.class, forVariable(variable));
    }

    public QContentMark(Path<? extends ContentMark> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentMark(PathMetadata metadata) {
        super(ContentMark.class, metadata);
    }

}

