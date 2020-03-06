package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.SysLinkType;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSysLinkType is a Querydsl query type for SysLinkType
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysLinkType extends EntityPathBase<SysLinkType> {

    private static final long serialVersionUID = 32108596L;

    public static final QSysLinkType sysLinkType = new QSysLinkType("sysLinkType");

    public final com.jeecms.common.base.domain.querydsl.QAbstractSortDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractSortDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final NumberPath<Integer> sortNum = _super.sortNum;

    public final StringPath typeName = createString("typeName");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QSysLinkType(String variable) {
        super(SysLinkType.class, forVariable(variable));
    }

    public QSysLinkType(Path<? extends SysLinkType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSysLinkType(PathMetadata metadata) {
        super(SysLinkType.class, metadata);
    }

}

