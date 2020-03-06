package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysUserSecret;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSysUserSecret is a Querydsl query type for SysUserSecret
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysUserSecret extends EntityPathBase<SysUserSecret> {

    private static final long serialVersionUID = 1492956219L;

    public static final QSysUserSecret sysUserSecret = new QSysUserSecret("sysUserSecret");

    public final com.jeecms.common.base.domain.querydsl.QAbstractSortDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractSortDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> remark = createNumber("remark", Integer.class);

    //inherited
    public final NumberPath<Integer> sortNum = _super.sortNum;

    public final ListPath<com.jeecms.system.domain.SysSecret, QSysSecret> sysSecrets = this.<com.jeecms.system.domain.SysSecret, QSysSecret>createList("sysSecrets", com.jeecms.system.domain.SysSecret.class, QSysSecret.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysUserSecret(String variable) {
        super(SysUserSecret.class, forVariable(variable));
    }

    public QSysUserSecret(Path<? extends SysUserSecret> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysUserSecret(PathMetadata metadata) {
        super(SysUserSecret.class, metadata);
    }

}

