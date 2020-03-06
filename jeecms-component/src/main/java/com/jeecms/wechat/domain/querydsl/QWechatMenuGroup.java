package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatMenuGroup;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWechatMenuGroup is a Querydsl query type for WechatMenuGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatMenuGroup extends EntityPathBase<WechatMenuGroup> {

    private static final long serialVersionUID = 642680694L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWechatMenuGroup wechatMenuGroup = new QWechatMenuGroup("wechatMenuGroup");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    public final com.jeecms.system.domain.querydsl.QArea area;

    public final StringPath areaCode = createString("areaCode");

    public final StringPath clientDictCode = createString("clientDictCode");

    public final StringPath countryName = createString("countryName");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath languageDictCode = createString("languageDictCode");

    public final StringPath menuGroupName = createString("menuGroupName");

    public final NumberPath<Integer> menuGroupType = createNumber("menuGroupType", Integer.class);

    public final NumberPath<Integer> menuId = createNumber("menuId", Integer.class);

    public final ListPath<com.jeecms.wechat.domain.WechatMenu, QWechatMenu> menuList = this.<com.jeecms.wechat.domain.WechatMenu, QWechatMenu>createList("menuList", com.jeecms.wechat.domain.WechatMenu.class, QWechatMenu.class, PathInits.DIRECT2);

    public final StringPath sexDictCode = createString("sexDictCode");

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath tagId = createString("tagId");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final QWechatFansTag wTag;

    public QWechatMenuGroup(String variable) {
        this(WechatMenuGroup.class, forVariable(variable), INITS);
    }

    public QWechatMenuGroup(Path<? extends WechatMenuGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWechatMenuGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWechatMenuGroup(PathMetadata metadata, PathInits inits) {
        this(WechatMenuGroup.class, metadata, inits);
    }

    public QWechatMenuGroup(Class<? extends WechatMenuGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.area = inits.isInitialized("area") ? new com.jeecms.system.domain.querydsl.QArea(forProperty("area"), inits.get("area")) : null;
        this.wTag = inits.isInitialized("wTag") ? new QWechatFansTag(forProperty("wTag")) : null;
    }

}

