package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysAccessRecord;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysAccessRecord is a Querydsl query type for SysAccessRecord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysAccessRecord extends EntityPathBase<SysAccessRecord> {

    private static final long serialVersionUID = 2003455157L;

    public static final QSysAccessRecord sysAccessRecord = new QSysAccessRecord("sysAccessRecord");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath accessBrowser = createString("accessBrowser");

    public final StringPath accessCity = createString("accessCity");

    public final StringPath accessCountry = createString("accessCountry");

    public final StringPath accessDevice = createString("accessDevice");

    public final StringPath accessIp = createString("accessIp");

    public final StringPath accessProvince = createString("accessProvince");

    public final NumberPath<Short> accessSourceClient = createNumber("accessSourceClient", Short.class);

    public final StringPath accessUrl = createString("accessUrl");

    public final StringPath cookieId = createString("cookieId");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final NumberPath<Short> deviceType = createNumber("deviceType", Short.class);

    public final StringPath engineName = createString("engineName");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isLogin = createBoolean("isLogin");

    public final NumberPath<Integer> loginUserId = createNumber("loginUserId", Integer.class);

    public final StringPath loginUserName = createString("loginUserName");

    public final BooleanPath newVisitor = createBoolean("newVisitor");

    public final StringPath sessionId = createString("sessionId");

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final NumberPath<Integer> sorceUrlType = createNumber("sorceUrlType", Integer.class);

    public final StringPath sourceDomain = createString("sourceDomain");

    public final StringPath sourceUrl = createString("sourceUrl");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysAccessRecord(String variable) {
        super(SysAccessRecord.class, forVariable(variable));
    }

    public QSysAccessRecord(Path<? extends SysAccessRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysAccessRecord(PathMetadata metadata) {
        super(SysAccessRecord.class, metadata);
    }

}

