package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatFans;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWechatFans is a Querydsl query type for WechatFans
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatFans extends EntityPathBase<WechatFans> {

    private static final long serialVersionUID = 1703390954L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWechatFans wechatFans = new QWechatFans("wechatFans");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    public final StringPath city = createString("city");

    public final StringPath country = createString("country");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final QWechatFansExt fansExt;

    public final NumberPath<Integer> groupid = createNumber("groupid", Integer.class);

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final StringPath headimgurl = createString("headimgurl");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isBlackList = createBoolean("isBlackList");

    public final StringPath language = createString("language");

    public final NumberPath<Integer> memberId = createNumber("memberId", Integer.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath openid = createString("openid");

    public final StringPath province = createString("province");

    public final StringPath qrScene = createString("qrScene");

    public final StringPath qrSceneStr = createString("qrSceneStr");

    public final StringPath remark = createString("remark");

    public final NumberPath<Integer> sex = createNumber("sex", Integer.class);

    public final NumberPath<Integer> subscribe = createNumber("subscribe", Integer.class);

    public final StringPath subscribeScene = createString("subscribeScene");

    public final NumberPath<Long> subscribeTime = createNumber("subscribeTime", Long.class);

    public final StringPath tagidList = createString("tagidList");

    public final StringPath unionid = createString("unionid");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final com.jeecms.auth.domain.querydsl.QCoreUser user;

    public final StringPath username = createString("username");

    public final QAbstractWeChatInfo weChatInfo;

    public QWechatFans(String variable) {
        this(WechatFans.class, forVariable(variable), INITS);
    }

    public QWechatFans(Path<? extends WechatFans> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWechatFans(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWechatFans(PathMetadata metadata, PathInits inits) {
        this(WechatFans.class, metadata, inits);
    }

    public QWechatFans(Class<? extends WechatFans> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fansExt = inits.isInitialized("fansExt") ? new QWechatFansExt(forProperty("fansExt"), inits.get("fansExt")) : null;
        this.user = inits.isInitialized("user") ? new com.jeecms.auth.domain.querydsl.QCoreUser(forProperty("user"), inits.get("user")) : null;
        this.weChatInfo = inits.isInitialized("weChatInfo") ? new QAbstractWeChatInfo(forProperty("weChatInfo")) : null;
    }

}

