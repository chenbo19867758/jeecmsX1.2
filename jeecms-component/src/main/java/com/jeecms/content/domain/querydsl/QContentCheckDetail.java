package com.jeecms.content.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.content.domain.ContentCheckDetail;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QContentCheckDetail is a Querydsl query type for ContentCheckDetail
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentCheckDetail extends EntityPathBase<ContentCheckDetail> {

    private static final long serialVersionUID = 284075955L;

    public static final QContentCheckDetail contentCheckDetail = new QContentCheckDetail("contentCheckDetail");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath checkBanContent = createString("checkBanContent");

    public final StringPath checkErrorContent = createString("checkErrorContent");

    public final StringPath checkMark = createString("checkMark");

    public final NumberPath<Integer> contentId = createNumber("contentId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Integer> fieldErrorNum = createNumber("fieldErrorNum", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QContentCheckDetail(String variable) {
        super(ContentCheckDetail.class, forVariable(variable));
    }

    public QContentCheckDetail(Path<? extends ContentCheckDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentCheckDetail(PathMetadata metadata) {
        super(ContentCheckDetail.class, metadata);
    }

}

