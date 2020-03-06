package com.jeecms.auth.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.auth.domain.CoreRole;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoreRole is a Querydsl query type for CoreRole
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCoreRole extends EntityPathBase<CoreRole> {

    private static final long serialVersionUID = -784216165L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoreRole coreRole = new QCoreRole("coreRole");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final SetPath<com.jeecms.system.domain.CmsDataPerm, com.jeecms.system.domain.querydsl.QCmsDataPerm> dataPerms = this.<com.jeecms.system.domain.CmsDataPerm, com.jeecms.system.domain.querydsl.QCmsDataPerm>createSet("dataPerms", com.jeecms.system.domain.CmsDataPerm.class, com.jeecms.system.domain.querydsl.QCmsDataPerm.class, PathInits.DIRECT2);

    public final StringPath description = createString("description");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<com.jeecms.auth.domain.CoreMenu, QCoreMenu> menus = this.<com.jeecms.auth.domain.CoreMenu, QCoreMenu>createList("menus", com.jeecms.auth.domain.CoreMenu.class, QCoreMenu.class, PathInits.DIRECT2);

    public final com.jeecms.system.domain.querydsl.QCmsOrg org;

    public final NumberPath<Integer> orgid = createNumber("orgid", Integer.class);

    public final ListPath<com.jeecms.system.domain.CmsDataPermCfg, com.jeecms.system.domain.querydsl.QCmsDataPermCfg> permCfgs = this.<com.jeecms.system.domain.CmsDataPermCfg, com.jeecms.system.domain.querydsl.QCmsDataPermCfg>createList("permCfgs", com.jeecms.system.domain.CmsDataPermCfg.class, com.jeecms.system.domain.querydsl.QCmsDataPermCfg.class, PathInits.DIRECT2);

    public final StringPath roleName = createString("roleName");

    public final SetPath<com.jeecms.system.domain.CmsSite, com.jeecms.system.domain.querydsl.QCmsSite> sites = this.<com.jeecms.system.domain.CmsSite, com.jeecms.system.domain.querydsl.QCmsSite>createSet("sites", com.jeecms.system.domain.CmsSite.class, com.jeecms.system.domain.querydsl.QCmsSite.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final ListPath<com.jeecms.auth.domain.CoreUser, QCoreUser> users = this.<com.jeecms.auth.domain.CoreUser, QCoreUser>createList("users", com.jeecms.auth.domain.CoreUser.class, QCoreUser.class, PathInits.DIRECT2);

    public QCoreRole(String variable) {
        this(CoreRole.class, forVariable(variable), INITS);
    }

    public QCoreRole(Path<? extends CoreRole> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoreRole(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoreRole(PathMetadata metadata, PathInits inits) {
        this(CoreRole.class, metadata, inits);
    }

    public QCoreRole(Class<? extends CoreRole> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.org = inits.isInitialized("org") ? new com.jeecms.system.domain.querydsl.QCmsOrg(forProperty("org"), inits.get("org")) : null;
    }

}

