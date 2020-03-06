package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.CmsDataPermCfg;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCmsDataPermCfg is a Querydsl query type for CmsDataPermCfg
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCmsDataPermCfg extends EntityPathBase<CmsDataPermCfg> {

    private static final long serialVersionUID = -1771338130L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCmsDataPermCfg cmsDataPermCfg = new QCmsDataPermCfg("cmsDataPermCfg");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath newChannelOpe = createString("newChannelOpe");

    public final StringPath newChannelOpeContent = createString("newChannelOpeContent");

    public final BooleanPath newMenuOwner = createBoolean("newMenuOwner");

    public final StringPath newSiteOpe = createString("newSiteOpe");

    public final BooleanPath newSiteOwner = createBoolean("newSiteOwner");

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

    public QCmsDataPermCfg(String variable) {
        this(CmsDataPermCfg.class, forVariable(variable), INITS);
    }

    public QCmsDataPermCfg(Path<? extends CmsDataPermCfg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCmsDataPermCfg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCmsDataPermCfg(PathMetadata metadata, PathInits inits) {
        this(CmsDataPermCfg.class, metadata, inits);
    }

    public QCmsDataPermCfg(Class<? extends CmsDataPermCfg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.org = inits.isInitialized("org") ? new QCmsOrg(forProperty("org"), inits.get("org")) : null;
        this.role = inits.isInitialized("role") ? new com.jeecms.auth.domain.querydsl.QCoreRole(forProperty("role"), inits.get("role")) : null;
        this.site = inits.isInitialized("site") ? new QCmsSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("user"), inits.get("user")) : null;
    }

}

