package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.CmsDataPerm;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCmsDataPerm is a Querydsl query type for CmsDataPerm
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCmsDataPerm extends EntityPathBase<CmsDataPerm> {

    private static final long serialVersionUID = 1671447062L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCmsDataPerm cmsDataPerm = new QCmsDataPerm("cmsDataPerm");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final com.jeecms.channel.domain.querydsl.QChannel dataChannel;

    public final NumberPath<Integer> dataId = createNumber("dataId", Integer.class);

    public final QCmsOrg dataOrg;

    public final NumberPath<Short> dataType = createNumber("dataType", Short.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Short> operation = createNumber("operation", Short.class);

    public final QCmsOrg org;

    public final NumberPath<Integer> orgId = createNumber("orgId", Integer.class);

    public final com.jeecms.auth.domain.querydsl.QCoreRole role;

    public final NumberPath<Integer> roleId = createNumber("roleId", Integer.class);

    public final QCmsSite site;

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final com.jeecms.auth.domain.querydsl.QCoreUser user;

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QCmsDataPerm(String variable) {
        this(CmsDataPerm.class, forVariable(variable), INITS);
    }

    public QCmsDataPerm(Path<? extends CmsDataPerm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCmsDataPerm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCmsDataPerm(PathMetadata metadata, PathInits inits) {
        this(CmsDataPerm.class, metadata, inits);
    }

    public QCmsDataPerm(Class<? extends CmsDataPerm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dataChannel = inits.isInitialized("dataChannel") ? new com.jeecms.channel.domain.querydsl.QChannel(forProperty("dataChannel"), inits.get("dataChannel")) : null;
        this.dataOrg = inits.isInitialized("dataOrg") ? new QCmsOrg(forProperty("dataOrg"), inits.get("dataOrg")) : null;
        this.org = inits.isInitialized("org") ? new QCmsOrg(forProperty("org"), inits.get("org")) : null;
        this.role = inits.isInitialized("role") ? new com.jeecms.auth.domain.querydsl.QCoreRole(forProperty("role"), inits.get("role")) : null;
        this.site = inits.isInitialized("site") ? new QCmsSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("user"), inits.get("user")) : null;
    }

}

