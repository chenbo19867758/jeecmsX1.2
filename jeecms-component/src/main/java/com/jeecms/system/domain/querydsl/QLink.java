package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.Link;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLink is a Querydsl query type for Link
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QLink extends EntityPathBase<Link> {

    private static final long serialVersionUID = 2111711079L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLink link = new QLink("link");

    public final com.jeecms.common.base.domain.querydsl.QAbstractSortDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractSortDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isEnable = createBoolean("isEnable");

    public final NumberPath<Integer> linkLogo = createNumber("linkLogo", Integer.class);

    public final StringPath linkName = createString("linkName");

    public final QSysLinkType linkType;

    public final NumberPath<Integer> linkTypeId = createNumber("linkTypeId", Integer.class);

    public final StringPath linkUrl = createString("linkUrl");

    public final StringPath remark = createString("remark");

    public final com.jeecms.resource.domain.querydsl.QResourcesSpaceData resourcesSpaceData;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final NumberPath<Integer> sortNum = _super.sortNum;

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QLink(String variable) {
        this(Link.class, forVariable(variable), INITS);
    }

    public QLink(Path<? extends Link> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLink(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLink(PathMetadata metadata, PathInits inits) {
        this(Link.class, metadata, inits);
    }

    public QLink(Class<? extends Link> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.linkType = inits.isInitialized("linkType") ? new QSysLinkType(forProperty("linkType")) : null;
        this.resourcesSpaceData = inits.isInitialized("resourcesSpaceData") ? new com.jeecms.resource.domain.querydsl.QResourcesSpaceData(forProperty("resourcesSpaceData"), inits.get("resourcesSpaceData")) : null;
    }

}

