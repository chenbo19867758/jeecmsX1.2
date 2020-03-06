package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SystemMessagePush;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSystemMessagePush is a Querydsl query type for SystemMessagePush
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSystemMessagePush extends EntityPathBase<SystemMessagePush> {

    private static final long serialVersionUID = -1268786235L;

    public static final QSystemMessagePush systemMessagePush = new QSystemMessagePush("systemMessagePush");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appKey = createString("appKey");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isEnable = createBoolean("isEnable");

    public final StringPath masterSecret = createString("masterSecret");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSystemMessagePush(String variable) {
        super(SystemMessagePush.class, forVariable(variable));
    }

    public QSystemMessagePush(Path<? extends SystemMessagePush> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSystemMessagePush(PathMetadata metadata) {
        super(SystemMessagePush.class, metadata);
    }

}

