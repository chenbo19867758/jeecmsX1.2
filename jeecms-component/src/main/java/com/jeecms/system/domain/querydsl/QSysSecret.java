package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysSecret;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysSecret is a Querydsl query type for SysSecret
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysSecret extends EntityPathBase<SysSecret> {

    private static final long serialVersionUID = 719327376L;

    public static final QSysSecret sysSecret = new QSysSecret("sysSecret");

    public final com.jeecms.common.base.domain.querydsl.QAbstractSortDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractSortDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath remark = createString("remark");

    public final NumberPath<Integer> secretType = createNumber("secretType", Integer.class);

    //inherited
    public final NumberPath<Integer> sortNum = _super.sortNum;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysSecret(String variable) {
        super(SysSecret.class, forVariable(variable));
    }

    public QSysSecret(Path<? extends SysSecret> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysSecret(PathMetadata metadata) {
        super(SysSecret.class, metadata);
    }

}

