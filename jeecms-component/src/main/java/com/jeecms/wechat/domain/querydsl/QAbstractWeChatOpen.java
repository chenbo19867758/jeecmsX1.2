package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.AbstractWeChatOpen;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractWeChatOpen is a Querydsl query type for AbstractWeChatOpen
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAbstractWeChatOpen extends EntityPathBase<AbstractWeChatOpen> {

    private static final long serialVersionUID = -189872298L;

    public static final QAbstractWeChatOpen abstractWeChatOpen = new QAbstractWeChatOpen("abstractWeChatOpen");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    public final StringPath appSecret = createString("appSecret");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath messageDecryptKey = createString("messageDecryptKey");

    public final StringPath messageValidateToken = createString("messageValidateToken");

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public QAbstractWeChatOpen(String variable) {
        super(AbstractWeChatOpen.class, forVariable(variable));
    }

    public QAbstractWeChatOpen(Path<? extends AbstractWeChatOpen> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractWeChatOpen(PathMetadata metadata) {
        super(AbstractWeChatOpen.class, metadata);
    }

}

