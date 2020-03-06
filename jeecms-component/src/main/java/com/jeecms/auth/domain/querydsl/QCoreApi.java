package com.jeecms.auth.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.auth.domain.CoreApi;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoreApi is a Querydsl query type for CoreApi
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCoreApi extends EntityPathBase<CoreApi> {

    private static final long serialVersionUID = 944517717L;

    public static final QCoreApi coreApi = new QCoreApi("coreApi");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath apiName = createString("apiName");

    public final StringPath apiUrl = createString("apiUrl");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<com.jeecms.auth.domain.CoreMenu, QCoreMenu> menus = this.<com.jeecms.auth.domain.CoreMenu, QCoreMenu>createList("menus", com.jeecms.auth.domain.CoreMenu.class, QCoreMenu.class, PathInits.DIRECT2);

    public final StringPath perms = createString("perms");

    public final NumberPath<Short> requestMethod = createNumber("requestMethod", Short.class);

    public final NumberPath<Integer> sortNum = createNumber("sortNum", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath useScene = createString("useScene");

    public QCoreApi(String variable) {
        super(CoreApi.class, forVariable(variable));
    }

    public QCoreApi(Path<? extends CoreApi> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoreApi(PathMetadata metadata) {
        super(CoreApi.class, metadata);
    }

}

