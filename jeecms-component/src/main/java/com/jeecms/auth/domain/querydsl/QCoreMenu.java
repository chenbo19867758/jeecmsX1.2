package com.jeecms.auth.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.auth.domain.CoreMenu;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoreMenu is a Querydsl query type for CoreMenu
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCoreMenu extends EntityPathBase<CoreMenu> {

    private static final long serialVersionUID = -784374652L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoreMenu coreMenu = new QCoreMenu("coreMenu");

    public final com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain _super;

    public final ListPath<com.jeecms.auth.domain.CoreApi, QCoreApi> apis = this.<com.jeecms.auth.domain.CoreApi, QCoreApi>createList("apis", com.jeecms.auth.domain.CoreApi.class, QCoreApi.class, PathInits.DIRECT2);

    //inherited
    public final ListPath<com.jeecms.common.base.domain.AbstractTreeDomain<?, ?>, com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain> childs;

    public final StringPath component = createString("component");

    //inherited
    public final DateTimePath<java.util.Date> createTime;

    //inherited
    public final StringPath createUser;

    //inherited
    public final BooleanPath hasDeleted;

    public final BooleanPath hidden = createBoolean("hidden");

    public final StringPath icon = createString("icon");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isAuth = createBoolean("isAuth");

    //inherited
    public final NumberPath<Integer> lft;

    public final StringPath menuName = createString("menuName");

    public final NumberPath<Short> menuType = createNumber("menuType", Short.class);

    public final StringPath name = createString("name");

    public final ListPath<com.jeecms.system.domain.CmsOrg, com.jeecms.system.domain.querydsl.QCmsOrg> orgs = this.<com.jeecms.system.domain.CmsOrg, com.jeecms.system.domain.querydsl.QCmsOrg>createList("orgs", com.jeecms.system.domain.CmsOrg.class, com.jeecms.system.domain.querydsl.QCmsOrg.class, PathInits.DIRECT2);

    public final QCoreMenu parent;

    //inherited
    public final NumberPath<Integer> parentId;

    public final StringPath path = createString("path");

    public final StringPath redirect = createString("redirect");

    //inherited
    public final NumberPath<Integer> rgt;

    public final ListPath<com.jeecms.auth.domain.CoreRole, QCoreRole> roles = this.<com.jeecms.auth.domain.CoreRole, QCoreRole>createList("roles", com.jeecms.auth.domain.CoreRole.class, QCoreRole.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Integer> sortNum;

    //inherited
    public final DateTimePath<java.util.Date> updateTime;

    //inherited
    public final StringPath updateUser;

    public final ListPath<com.jeecms.auth.domain.CoreUser, QCoreUser> users = this.<com.jeecms.auth.domain.CoreUser, QCoreUser>createList("users", com.jeecms.auth.domain.CoreUser.class, QCoreUser.class, PathInits.DIRECT2);

    public QCoreMenu(String variable) {
        this(CoreMenu.class, forVariable(variable), INITS);
    }

    public QCoreMenu(Path<? extends CoreMenu> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoreMenu(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoreMenu(PathMetadata metadata, PathInits inits) {
        this(CoreMenu.class, metadata, inits);
    }

    public QCoreMenu(Class<? extends CoreMenu> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.jeecms.common.base.domain.querydsl.QAbstractTreeDomain(type, metadata, inits);
        this.childs = _super.childs;
        this.createTime = _super.createTime;
        this.createUser = _super.createUser;
        this.hasDeleted = _super.hasDeleted;
        this.lft = _super.lft;
        this.parent = inits.isInitialized("parent") ? new QCoreMenu(forProperty("parent")) : null;
        this.parentId = _super.parentId;
        this.rgt = _super.rgt;
        this.sortNum = _super.sortNum;
        this.updateTime = _super.updateTime;
        this.updateUser = _super.updateUser;
    }

}

