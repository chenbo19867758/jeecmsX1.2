package com.jeecms.interact.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.interact.domain.UserInteractionRecord;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserInteractionRecord is a Querydsl query type for UserInteractionRecord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserInteractionRecord extends EntityPathBase<UserInteractionRecord> {

    private static final long serialVersionUID = -42966396L;

    public static final QUserInteractionRecord userInteractionRecord = new QUserInteractionRecord("userInteractionRecord");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final NumberPath<Integer> operateType = createNumber("operateType", Integer.class);

    public final NumberPath<Integer> resourceId = createNumber("resourceId", Integer.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public final StringPath userName = createString("userName");

    public QUserInteractionRecord(String variable) {
        super(UserInteractionRecord.class, forVariable(variable));
    }

    public QUserInteractionRecord(Path<? extends UserInteractionRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserInteractionRecord(PathMetadata metadata) {
        super(UserInteractionRecord.class, metadata);
    }

}

