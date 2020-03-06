package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysIptables;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysIptables is a Querydsl query type for SysIptables
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysIptables extends EntityPathBase<SysIptables> {

    private static final long serialVersionUID = 1179147308L;

    public static final QSysIptables sysIptables = new QSysIptables("sysIptables");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath allowLoginHours = createString("allowLoginHours");

    public final StringPath allowLoginWeek = createString("allowLoginWeek");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath inNetworkIpJson = createString("inNetworkIpJson");

    public final BooleanPath isEnable = createBoolean("isEnable");

    public final StringPath limitDomain = createString("limitDomain");

    public final NumberPath<Integer> limitInNetworkModel = createNumber("limitInNetworkModel", Integer.class);

    public final NumberPath<Integer> limitOutNetworkModel = createNumber("limitOutNetworkModel", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath workNetworkIp = createString("workNetworkIp");

    public QSysIptables(String variable) {
        super(SysIptables.class, forVariable(variable));
    }

    public QSysIptables(Path<? extends SysIptables> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysIptables(PathMetadata metadata) {
        super(SysIptables.class, metadata);
    }

}

