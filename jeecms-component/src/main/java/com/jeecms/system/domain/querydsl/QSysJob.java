package com.jeecms.system.domain.querydsl;

import com.jeecms.system.domain.SysJob;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QSysJob is a Querydsl query type for SysJob
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysJob extends EntityPathBase<SysJob> {

    private static final long serialVersionUID = -1949882915L;

    public static final QSysJob sysJob = new QSysJob("sysJob");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath channelIds = createString("channelIds");

    public final StringPath classPath = createString("classPath");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath cron = createString("cron");

    public final StringPath cronName = createString("cronName");

    public final NumberPath<Integer> cronType = createNumber("cronType", Integer.class);

    public final NumberPath<Integer> execCycleType = createNumber("execCycleType", Integer.class);

    public final StringPath groupName = createString("groupName");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> intervalNum = createNumber("intervalNum", Integer.class);

    public final NumberPath<Integer> intervalType = createNumber("intervalType", Integer.class);

    public final BooleanPath isAll = createBoolean("isAll");

    public final StringPath params = createString("params");

    public final StringPath remark = createString("remark");

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final DateTimePath<java.util.Date> startTime = createDateTime("startTime", java.util.Date.class);

    public final BooleanPath status = createBoolean("status");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysJob(String variable) {
        super(SysJob.class, forVariable(variable));
    }

    public QSysJob(Path<? extends SysJob> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysJob(PathMetadata metadata) {
        super(SysJob.class, metadata);
    }

}

