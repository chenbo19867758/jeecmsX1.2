package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysLogBackup;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysLogBackup is a Querydsl query type for SysLogBackup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysLogBackup extends EntityPathBase<SysLogBackup> {

    private static final long serialVersionUID = -217713594L;

    public static final QSysLogBackup sysLogBackup = new QSysLogBackup("sysLogBackup");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath backupFileUrl = createString("backupFileUrl");

    public final StringPath backupName = createString("backupName");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Integer> dataCount = createNumber("dataCount", Integer.class);

    public final DatePath<java.util.Date> endTime = createDate("endTime", java.util.Date.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath remark = createString("remark");

    public final DatePath<java.util.Date> startTime = createDate("startTime", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysLogBackup(String variable) {
        super(SysLogBackup.class, forVariable(variable));
    }

    public QSysLogBackup(Path<? extends SysLogBackup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysLogBackup(PathMetadata metadata) {
        super(SysLogBackup.class, metadata);
    }

}

