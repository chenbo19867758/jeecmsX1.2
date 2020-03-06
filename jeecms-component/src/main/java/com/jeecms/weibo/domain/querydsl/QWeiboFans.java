package com.jeecms.weibo.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.weibo.domain.WeiboFans;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWeiboFans is a Querydsl query type for WeiboFans
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWeiboFans extends EntityPathBase<WeiboFans> {

    private static final long serialVersionUID = 1554470118L;

    public static final QWeiboFans weiboFans = new QWeiboFans("weiboFans");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final BooleanPath allowAllActMsg = createBoolean("allowAllActMsg");

    public final BooleanPath allowAllComment = createBoolean("allowAllComment");

    public final StringPath avatarHd = createString("avatarHd");

    public final StringPath avatarLarge = createString("avatarLarge");

    public final StringPath blogUrl = createString("blogUrl");

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    public final StringPath description = createString("description");

    public final StringPath domain = createString("domain");

    public final NumberPath<Integer> favouritesCount = createNumber("favouritesCount", Integer.class);

    public final NumberPath<Integer> followersCount = createNumber("followersCount", Integer.class);

    public final NumberPath<Integer> friendsCount = createNumber("friendsCount", Integer.class);

    public final StringPath gender = createString("gender");

    public final BooleanPath geoEnabled = createBoolean("geoEnabled");

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath idstr = createString("idstr");

    public final StringPath lang = createString("lang");

    public final StringPath location = createString("location");

    public final StringPath name = createString("name");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final StringPath profileUrl = createString("profileUrl");

    public final StringPath screenName = createString("screenName");

    public final NumberPath<Integer> statusesCount = createNumber("statusesCount", Integer.class);

    public final NumberPath<Long> uid = createNumber("uid", Long.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final BooleanPath verified = createBoolean("verified");

    public final StringPath verifiedReason = createString("verifiedReason");

    public final StringPath weihao = createString("weihao");

    public QWeiboFans(String variable) {
        super(WeiboFans.class, forVariable(variable));
    }

    public QWeiboFans(Path<? extends WeiboFans> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWeiboFans(PathMetadata metadata) {
        super(WeiboFans.class, metadata);
    }

}

