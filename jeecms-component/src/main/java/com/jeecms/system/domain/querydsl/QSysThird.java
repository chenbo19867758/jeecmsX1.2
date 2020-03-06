package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysThird;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysThird is a Querydsl query type for SysThird
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysThird extends EntityPathBase<SysThird> {

    private static final long serialVersionUID = -1222703225L;

    public static final QSysThird sysThird = new QSysThird("sysThird");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    public final StringPath appKey = createString("appKey");

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isEnable = createBoolean("isEnable");

    public final StringPath reMark = createString("reMark");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysThird(String variable) {
        super(SysThird.class, forVariable(variable));
    }

    public QSysThird(Path<? extends SysThird> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysThird(PathMetadata metadata) {
        super(SysThird.class, metadata);
    }

}

