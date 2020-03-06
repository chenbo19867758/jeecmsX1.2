package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.AbstractWeChatToken;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractWeChatToken is a Querydsl query type for AbstractWeChatToken
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAbstractWeChatToken extends EntityPathBase<AbstractWeChatToken> {

    private static final long serialVersionUID = -1586480531L;

    public static final QAbstractWeChatToken abstractWeChatToken = new QAbstractWeChatToken("abstractWeChatToken");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final DateTimePath<java.util.Date> acceccTokenCreateTime = createDateTime("acceccTokenCreateTime", java.util.Date.class);

    public final StringPath appId = createString("appId");

    public final StringPath authorizerAccessToken = createString("authorizerAccessToken");

    public final StringPath authorizerRefreshToken = createString("authorizerRefreshToken");

    public final StringPath componentAccessToken = createString("componentAccessToken");

    public final StringPath componentVerifyTicket = createString("componentVerifyTicket");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QAbstractWeChatToken(String variable) {
        super(AbstractWeChatToken.class, forVariable(variable));
    }

    public QAbstractWeChatToken(Path<? extends AbstractWeChatToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractWeChatToken(PathMetadata metadata) {
        super(AbstractWeChatToken.class, metadata);
    }

}

