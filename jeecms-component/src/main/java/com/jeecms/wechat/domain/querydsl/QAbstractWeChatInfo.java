package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.AbstractWeChatInfo;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAbstractWeChatInfo is a Querydsl query type for AbstractWeChatInfo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAbstractWeChatInfo extends EntityPathBase<AbstractWeChatInfo> {

    private static final long serialVersionUID = -190052934L;

    public static final QAbstractWeChatInfo abstractWeChatInfo = new QAbstractWeChatInfo("abstractWeChatInfo");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath alias = createString("alias");

    public final StringPath appId = createString("appId");

    public final StringPath categories = createString("categories");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath funcInfo = createString("funcInfo");

    public final StringPath globalId = createString("globalId");

    public final NumberPath<Short> grantType = createNumber("grantType", Short.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final StringPath headImg = createString("headImg");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath network = createString("network");

    public final StringPath principalName = createString("principalName");

    public final StringPath qrcodeUrl = createString("qrcodeUrl");

    public final NumberPath<Integer> siteId = createNumber("siteId", Integer.class);

    public final NumberPath<Short> type = createNumber("type", Short.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final ListPath<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser> users = this.<com.jeecms.auth.domain.CoreUser, com.jeecms.auth.domain.querydsl.QCoreUser>createList("users", com.jeecms.auth.domain.CoreUser.class, com.jeecms.auth.domain.querydsl.QCoreUser.class, PathInits.DIRECT2);

    public final NumberPath<Short> verifyStatus = createNumber("verifyStatus", Short.class);

    public final StringPath wechatName = createString("wechatName");

    public final NumberPath<Short> wechatType = createNumber("wechatType", Short.class);

    public QAbstractWeChatInfo(String variable) {
        super(AbstractWeChatInfo.class, forVariable(variable));
    }

    public QAbstractWeChatInfo(Path<? extends AbstractWeChatInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractWeChatInfo(PathMetadata metadata) {
        super(AbstractWeChatInfo.class, metadata);
    }

}

