package com.jeecms.wechat.domain.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.jeecms.wechat.domain.MiniprogramMember;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QMiniprogramMember is a Querydsl query type for MiniprogramMember
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMiniprogramMember extends EntityPathBase<MiniprogramMember> {

    private static final long serialVersionUID = 1374314467L;

    public static final QMiniprogramMember miniprogramMember = new QMiniprogramMember("miniprogramMember");

    public final com.jeecms.common.base.domain.querydsl.QAbstractDomain _super = new com.jeecms.common.base.domain.querydsl.QAbstractDomain(this);

    public final StringPath appId = createString("appId");

    //inherited
    public final DateTimePath<java.util.Date> createTime = _super.createTime;

    //inherited
    public final StringPath createUser = _super.createUser;

    //inherited
    public final BooleanPath hasDeleted = _super.hasDeleted;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> storeId = createNumber("storeId", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updateTime = _super.updateTime;

    //inherited
    public final StringPath updateUser = _super.updateUser;

    public final StringPath wechatId = createString("wechatId");

    public QMiniprogramMember(String variable) {
        super(MiniprogramMember.class, forVariable(variable));
    }

    public QMiniprogramMember(Path<? extends MiniprogramMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMiniprogramMember(PathMetadata metadata) {
        super(MiniprogramMember.class, metadata);
    }

}

