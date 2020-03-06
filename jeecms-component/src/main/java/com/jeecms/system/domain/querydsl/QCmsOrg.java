package com.jeecms.system.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.system.domain.CmsOrg;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCmsOrg is a Querydsl query type for CmsOrg
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCmsOrg extends EntityPathBase<CmsOrg> {

    private static final long serialVersionUID = 1875940616L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCmsOrg cmsOrg = new QCmsOrg("cmsOrg");

    public final com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain _super;

    public final StringPath address = createString("address");

    public final SetPath<CmsOrg, QCmsOrg> child = this.<CmsOrg, QCmsOrg>createSet("child", CmsOrg.class, QCmsOrg.class, PathInits.DIRECT2);

    //inherited
    public final ListPath<com.jeecms.common.base.domain.AbstractTreeDomain<?, ?>, com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain> children;

    //inherited
    public final ListPath<com.jeecms.common.base.domain.AbstractTreeDomain<?, ?>, com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain> childs;

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.util.Date> createTime;

    //inherited
    public final StringPath createUser;

    public final SetPath<com.jeecms.system.domain.CmsDataPerm, QCmsDataPerm> dataPerms = this.<com.jeecms.system.domain.CmsDataPerm, QCmsDataPerm>createSet("dataPerms", com.jeecms.system.domain.CmsDataPerm.class, QCmsDataPerm.class, PathInits.DIRECT2);

    //inherited
    public final BooleanPath hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isVirtual = createBoolean("isVirtual");

    //inherited
    public final NumberPath<Integer> lft;

    public final ListPath<com.jeecms.auth.domain.CoreMenu, com.jeecms.auth.domain.querydsl.QCoreMenu> menus = this.<com.jeecms.auth.domain.CoreMenu, com.jeecms.auth.domain.querydsl.QCoreMenu>createList("menus", com.jeecms.auth.domain.CoreMenu.class, com.jeecms.auth.domain.querydsl.QCoreMenu.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath orgFax = createString("orgFax");

    public final StringPath orgLeader = createString("orgLeader");

    public final StringPath orgNum = createString("orgNum");

    public final StringPath orgRemark = createString("orgRemark");

    public final QCmsOrg parent;

    //inherited
    public final NumberPath<Integer> parentId;

    public final ListPath<com.jeecms.system.domain.CmsDataPermCfg, QCmsDataPermCfg> permCfgs = this.<com.jeecms.system.domain.CmsDataPermCfg, QCmsDataPermCfg>createList("permCfgs", com.jeecms.system.domain.CmsDataPermCfg.class, QCmsDataPermCfg.class, PathInits.DIRECT2);

    public final StringPath phone = createString("phone");

    //inherited
    public final NumberPath<Integer> rgt;

    public final ListPath<com.jeecms.auth.domain.CoreRole, com.jeecms.auth.domain.querydsl.QCoreRole> roles = this.<com.jeecms.auth.domain.CoreRole, com.jeecms.auth.domain.querydsl.QCoreRole>createList("roles", com.jeecms.auth.domain.CoreRole.class, com.jeecms.auth.domain.querydsl.QCoreRole.class, PathInits.DIRECT2);

    public final SetPath<com.jeecms.system.domain.CmsSite, QCmsSite> sites = this.<com.jeecms.system.domain.CmsSite, QCmsSite>createSet("sites", com.jeecms.system.domain.CmsSite.class, QCmsSite.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Integer> sortNum;

    public final NumberPath<Integer> sortWeight = createNumber("sortWeight", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime;

    //inherited
    public final StringPath updateUser;

    public final ListPath<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser> users = this.<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser>createList("users", com.jeecms.auth.domain.CoreUser.class, com.jeecms.auth.domain.querydsl.QCoreUser.class, PathInits.DIRECT2);

    public QCmsOrg(String variable) {
        this(CmsOrg.class, forVariable(variable), INITS);
    }

    public QCmsOrg(Path<? extends CmsOrg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCmsOrg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCmsOrg(PathMetadata metadata, PathInits inits) {
        this(CmsOrg.class, metadata, inits);
    }

    public QCmsOrg(Class<? extends CmsOrg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain(type, metadata, inits);
        this.children = _super.children;
        this.childs = _super.childs;
        this.createTime = _super.createTime;
        this.createUser = _super.createUser;
        this.hasDeleted = _super.hasDeleted;
        this.lft = _super.lft;
        this.parent = inits.isInitialized("parent") ? new QCmsOrg(forProperty("parent"), inits.get("parent")) : null;
        this.parentId = _super.parentId;
        this.rgt = _super.rgt;
        this.sortNum = _super.sortNum;
        this.updateTime = _super.updateTime;
        this.updateUser = _super.updateUser;
    }

}

