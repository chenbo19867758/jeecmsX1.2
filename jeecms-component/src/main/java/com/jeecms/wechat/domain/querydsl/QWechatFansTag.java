package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.WechatFansTag;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWechatFansTag is a Querydsl query type for WechatFansTag
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWechatFansTag extends EntityPathBase<WechatFansTag> {

    private static final long serialVersionUID = 681392208L;

    public static final QWechatFansTag wechatFansTag = new QWechatFansTag("wechatFansTag");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath tagName = createString("tagName");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final NumberPath<Integer> userCount = createNumber("userCount", Integer.class);

    public QWechatFansTag(String variable) {
        super(WechatFansTag.class, forVariable(variable));
    }

    public QWechatFansTag(Path<? extends WechatFansTag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWechatFansTag(PathMetadata metadata) {
        super(WechatFansTag.class, metadata);
    }

}

