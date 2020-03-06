package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysLog;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysLog is a Querydsl query type for SysLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysLog extends EntityPathBase<SysLog> {

    private static final long serialVersionUID = -1949880988L;

    public static final QSysLog sysLog = new QSysLog("sysLog");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath browser = createString("browser");

    public final StringPath clientIp = createString("clientIp");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Integer> eventType = createNumber("eventType", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final StringPath httpStatusCode = createString("httpStatusCode");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> logCategory = createNumber("logCategory", Integer.class);

    public final NumberPath<Integer> logLevel = createNumber("logLevel", Integer.class);

    public final NumberPath<Integer> logType = createNumber("logType", Integer.class);

    public final StringPath method = createString("method");

    public final NumberPath<Integer> operateType = createNumber("operateType", Integer.class);

    public final StringPath os = createString("os");

    public final StringPath paramData = createString("paramData");

    public final StringPath remark = createString("remark");

    public final NumberPath<Integer> requestResult = createNumber("requestResult", Integer.class);

    public final StringPath returmTime = createString("returmTime");

    public final StringPath returnData = createString("returnData");

    public final StringPath sessionId = createString("sessionId");

    public final StringPath subEventType = createString("subEventType");

    public final NumberPath<Integer> timeConsuming = createNumber("timeConsuming", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath uri = createString("uri");

    public final StringPath userAgent = createString("userAgent");

    public final StringPath username = createString("username");

    public QSysLog(String variable) {
        super(SysLog.class, forVariable(variable));
    }

    public QSysLog(Path<? extends SysLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysLog(PathMetadata metadata) {
        super(SysLog.class, metadata);
    }

}

