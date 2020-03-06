package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatMenu;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWechatMenu is a Querydsl query type for WechatMenu
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatMenu extends EntityPathBase<WechatMenu> {

    private static final long serialVersionUID = 1703603337L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWechatMenu wechatMenu = new QWechatMenu("wechatMenu");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final ListPath<WechatMenu, QWechatMenu> childs = this.<WechatMenu, QWechatMenu>createList("childs", WechatMenu.class, QWechatMenu.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath linkUrl = createString("linkUrl");

    public final QWechatMaterial material;

    public final StringPath mediaId = createString("mediaId");

    public final QWechatMenuGroup menuGroup;

    public final NumberPath<Integer> menuGroupId = createNumber("menuGroupId", Integer.class);

    public final StringPath menuKey = createString("menuKey");

    public final StringPath menuName = createString("menuName");

    public final StringPath menuType = createString("menuType");

    public final StringPath miniprogramAppid = createString("miniprogramAppid");

    public final StringPath miniprogramPagepath = createString("miniprogramPagepath");

    public final QWechatMenu parent;

    public final NumberPath<Integer> parentId = createNumber("parentId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final QWechatReplyContent wechatReplyContent;

    public QWechatMenu(String variable) {
        this(WechatMenu.class, forVariable(variable), INITS);
    }

    public QWechatMenu(Path<? extends WechatMenu> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWechatMenu(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWechatMenu(PathMetadata metadata, PathInits inits) {
        this(WechatMenu.class, metadata, inits);
    }

    public QWechatMenu(Class<? extends WechatMenu> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.material = inits.isInitialized("material") ? new QWechatMaterial(forProperty("material")) : null;
        this.menuGroup = inits.isInitialized("menuGroup") ? new QWechatMenuGroup(forProperty("menuGroup"), inits.get("menuGroup")) : null;
        this.parent = inits.isInitialized("parent") ? new QWechatMenu(forProperty("parent"), inits.get("parent")) : null;
        this.wechatReplyContent = inits.isInitialized("wechatReplyContent") ? new QWechatReplyContent(forProperty("wechatReplyContent"), inits.get("wechatReplyContent")) : null;
    }

}

